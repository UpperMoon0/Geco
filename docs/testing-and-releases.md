# Layered validation and publishing

Geco targets Minecraft 1.21.1, Java 21, Fabric and NeoForge.

## Validation layers

| Layer | Coverage | Command | Cost |
| --- | --- | --- | --- |
| 1 | Resource graph, malformed resources, metadata, release decisions, provenance, checksums and receipts | python -m unittest discover -s scripts -p 'test_*.py' -v; python scripts/check_resources.py | No JVM; roughly one second locally |
| 2 | Pure transaction commits, stale preflight, failed/dishonest writes, exceptions and rollback | bash gradlew :common:unitTest | Small JVM; no Minecraft bootstrap |
| 2 | Exact vanilla variants, every generated file, tag merging/errors, seeded deposits and template planning | bash gradlew :common:contractTest | One Minecraft bootstrap per test JVM; no server |
| 3 | Both production JARs, bundled code/resources, entrypoints, dependencies, licenses and images | bash gradlew testFast build; python scripts/check_resources.py --jars | Shared build invocation, incremental tasks/build cache |
| 4 | Real registration/reload on both loaders; terrain; exact tree silhouettes/log axes; bonemeal/blocked growth; protected cells; runtime loot; persistent saves and legacy generator migration | python scripts/smoke_server.py fabric --restart; repeat for neoforge | Loaders run in parallel in CI; reopening reuses the same world |

Use Python 3.12+ and install scripts/smoke-requirements.txt for runtime tests. The harness's region-decoding and restart tests run in that layer; the dependency-free layer skips those five tests when NumPy/nbtlib are absent.

The testFast Gradle task runs the two disjoint JVM suites once, generates JaCoCo HTML/XML, and enforces **90% line / 80% branch coverage per class** for:
- PlacementTransaction, TemplateTreePlacement, MarbleFeature
- CanonicalJson and blockstate/model/recipe/loot/tag generators

The gate covers these nine critical classes. The full common-code report remains available; loader initialization, vanilla internals and client rendering are not represented by that percentage. Runtime checks exercise loader integration separately. CI retains reports and server logs, including successful runs.

Datagen package coverage measured on this change: **99.4% lines / 97.6% branches**. The complete generator compares all **421** checked-in files and repeats in the same temporary directory to catch duplicate tag membership. CI additionally runs datagen in independent JVMs and compares the complete path set and hashes.

Cheap contracts pass before the build; the build passes before loader launches. PRs and dev pushes run validation. Main pushes use the release workflow's reusable validation, avoiding duplicate runs. Releases download the validated JAR artifact instead of rebuilding it. Gradle caches are retained; no routine clean or repeated terrain generation is used. Bonemeal pulses stop when growth is observed, with a bounded maximum.

The server harness uses loader development launches against shared production sources/resources; remapped release JARs are separately inspected. It does not claim a graphical client, shader or third-party modpack test.

## Release configuration

CurseForge project: **1296676**. Add repository Actions secret **CURSEFORGE_API_TOKEN** with an author upload token for this project. Missing credentials fail before tag reservation. Never commit tokens.

Required dependencies:
- Fabric: Architectury API (419699) and Fabric API (306612).
- NeoForge: Architectury API (419699).

The publishing action is pinned to Kira-NT/mc-publish commit 52307b03863581dec6b652b83e597aec02ebb075 (v3.3). Both files specify Minecraft 1.21.1, Java 21, their loader, client/server environment and release type.

To release:
1. Bump mod_version to a stable MAJOR.MINOR.PATCH version and add changelogs/vVERSION.md.
2. Merge the reviewed change into main. The workflow validates that exact commit.
3. Validation stages only the two production JARs, manifest.json and SHA256SUMS. The manifest binds hashes to the commit, version and project.
4. Publishing verifies the artifact and tag ownership, reserves vVERSION, and creates a GitHub draft containing verified files.
5. Each successful CurseForge upload records its file ID, loader, commit and hash in a draft-release receipt. Retries verify and skip recorded uploads.
6. GitHub becomes public only after both receipts validate.

JAR timestamps/file order are normalized. Tags belonging to another commit are rejected. Existing releases are downloaded and verified, never silently replaced with different bytes. Ordinary main commits at an already released version validate without republishing. An untagged version can release on the next main push. Manual dispatch or rerunning a failed run retries the same release commit.

A CurseForge upload and GitHub receipt cannot be atomic. Automatic upload retries are disabled: after an ambiguous timeout or a successful upload that lost its receipt, inspect the project's Files page before retrying. If the file exists, recover the receipt with its actual file ID instead of uploading again. scripts/release.py defines the receipt format; its SHA-256 must match the validated manifest. Repair a partial GitHub draft using the original validated artifact. Never retarget a version tag or substitute new bytes.

## Reference projects

Patterns inspected:
- [Endless validation](https://github.com/UpperMoon0/Endless/blob/d79524e689f9e0a588e1a7d0b5685e945c16697a/.github/workflows/validate.yml) and release: cheap harness/metadata tests separated from common tests and loader builds.
- [Celestial Nail release](https://github.com/UpperMoon0/Celestial-Nail/blob/a567fe6bc4fac7baaab714205c08d23aad8ea3db/.github/workflows/release.yml) and validation: exact-commit validation, artifact inspection, checksums/provenance and immutable tags.

Geco adapts these patterns to its two-loader worldgen scope.
