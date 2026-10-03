# Geco

Geco enriches Minecraft's natural world with handmade ebony trees and rare marble formations, bringing new materials to exploration and building while preserving vanilla terrain generation.

**Minecraft 1.21.1 · Fabric & NeoForge · Java 21**

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/geco) · [Discord](https://discord.gg/4vD9WuT2As) · [Report an issue](https://github.com/UpperMoon0/Geco/issues)

![Geco building blocks](assets/blocks.png)

## Features

- **Ebony trees:** Several handmade tree shapes generate in savanna biomes. Collect saplings to grow your own trees.
- **Ebony wood:** Logs, wood, stripped variants, planks, stairs, slabs, fences, fence gates, doors, trapdoors, buttons, and pressure plates, plus leaves and saplings.
- **Cream Marble and Multicolor Marble:** Rare Overworld deposits with natural, polished, polished brick, polished tile, and smooth finishes. Every finish includes slabs, stairs, and walls.

Geco adds its features to normal Minecraft terrain on both loaders. In existing worlds, explore newly generated chunks to find natural ebony trees and marble deposits.

## Installation

1. Install Fabric Loader or NeoForge for **Minecraft 1.21.1**, using **Java 21**.
2. Download the matching Geco file from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/geco).
3. Place Geco and its required dependencies in your `mods` folder:
   - **Both loaders:** Architectury API 13.0.8 or newer for Minecraft 1.21.1.
   - **Fabric:** Fabric API for Minecraft 1.21.1; Fabric Loader 0.16.14 or newer.
   - **NeoForge:** NeoForge 21.1.172 or newer within the 21.1 series.
4. Launch the game using your chosen loader profile.

For a dedicated server, install the matching mod and dependencies on the server as well as on clients.

## Building from Source

Use JDK 21 and the included Gradle wrapper from the repository root.

Linux/macOS:

```sh
bash gradlew build
```

Windows PowerShell:

```powershell
.\gradlew.bat build
```

The loader-specific production JARs are written to:

- `fabric/build/libs/geco-fabric-<version>.jar`
- `neoforge/build/libs/geco-neoforge-<version>.jar`

Use these JARs for installation. The `common` module and artifacts marked `dev`, `dev-shadow`, or `sources` are development outputs.

## Development & Validation

Run `bash gradlew testFast build` (or `.\gradlew.bat testFast build` on Windows) for the JVM tests, critical coverage checks, and both loader builds.

Resource and packaged-JAR checks:

```sh
python -m unittest discover -s scripts -p 'test_*.py'
python scripts/check_resources.py --jars
```

Client development launches are available through `:fabric:runClient` and `:neoforge:runClient`.

Natural ebony trees and saplings use the same handmade templates. Placement checks protect occupied cells, fluids, and block entities; obstructed growth preserves the sapling.

Worlds from older unreleased builds can load their legacy generator as vanilla noise terrain. Previously generated empty chunks are not rebuilt automatically. See the [world-generation validation and upgrade notes](docs/vanilla-reference-and-validation.md) for details.

See [testing and release configuration](docs/testing-and-releases.md) for Python requirements, loader runtime tests, CI coverage, and CurseForge publication. Version-specific release notes live in [changelogs](changelogs/), and the player-facing CurseForge description is maintained in [CURSEFORGE.md](CURSEFORGE.md).

## Contributing & Support

Suggestions, bug reports, and pull requests are welcome. Join the [Discord community](https://discord.gg/4vD9WuT2As) or [open a GitHub issue](https://github.com/UpperMoon0/Geco/issues).

For bug reports, include your Minecraft version, loader, Geco version, relevant logs, and steps to reproduce the problem.

## License

Geco is licensed under the [MIT License](LICENSE).
