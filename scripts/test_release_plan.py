"""Cheap release-decision tests; no remote writes or credentials."""
import contextlib
import io
import json
import os
import pathlib
import tempfile
import unittest
from unittest import mock

import release


class ReleasePlanTests(unittest.TestCase):
    COMMIT = "a" * 40

    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.root = pathlib.Path(self.temp.name)
        (self.root / "changelogs").mkdir()
        (self.root / "changelogs/v0.2.0.md").write_text("Release notes\n")
        self.addCleanup(self.temp.cleanup)
        self.stack = contextlib.ExitStack()
        self.addCleanup(self.stack.close)
        self.stack.enter_context(mock.patch.object(release, "ROOT", self.root))
        self.stack.enter_context(mock.patch.object(release, "identity",
                                                  return_value=("0.2.0", self.COMMIT, "v0.2.0")))
        self.stack.enter_context(mock.patch.dict(os.environ, {"BEFORE_SHA": "b" * 40,
            "GITHUB_EVENT_NAME": "push", "GITHUB_OUTPUT": str(self.root / "outputs")}))
        self.previous = "0.1.0"
        self.tag = None
        def git(*args, **kwargs):
            if args[0] == "show":
                return f"mod_version={self.previous}\nminecraft_version=1.21.1"
            if args[0] == "rev-parse":
                return self.tag
            raise AssertionError(args)
        self.stack.enter_context(mock.patch.object(release, "git", side_effect=git))

    def plan(self):
        with contextlib.redirect_stdout(io.StringIO()):
            release.plan()
        return dict(line.split("=", 1) for line in (self.root / "outputs").read_text().splitlines())

    def test_new_version_releases(self):
        self.assertEqual("true", self.plan()["release"])

    def test_ordinary_commit_at_existing_version_is_ci_only(self):
        self.previous = "0.2.0"
        self.tag = "c" * 40
        self.assertEqual("false", self.plan()["release"])

    def test_unreleased_same_version_can_retry(self):
        self.previous = "0.2.0"
        self.assertEqual("true", self.plan()["release"])

    def test_manual_retry_accepts_same_commit_tag(self):
        os.environ["GITHUB_EVENT_NAME"] = "workflow_dispatch"
        self.previous = "0.2.0"
        self.tag = self.COMMIT
        self.assertEqual("true", self.plan()["release"])

    def test_manual_retry_rejects_different_commit_tag(self):
        os.environ["GITHUB_EVENT_NAME"] = "workflow_dispatch"
        self.tag = "c" * 40
        with self.assertRaisesRegex(ValueError, "another commit"):
            self.plan()

    def test_version_bump_with_existing_tag_is_rejected(self):
        self.tag = "c" * 40
        with self.assertRaisesRegex(ValueError, "another commit"):
            self.plan()

    def test_release_requires_changelog(self):
        (self.root / "changelogs/v0.2.0.md").unlink()
        with self.assertRaisesRegex(ValueError, "changelog"):
            self.plan()


if __name__ == "__main__":
    unittest.main()
