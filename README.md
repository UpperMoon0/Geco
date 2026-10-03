# Geco Mod

Visit on CurseForge: https://www.curseforge.com/minecraft/mc-mods/geco

This mod begins a journey to enrich Minecraft's natural world. Discover new geological formations and botanical diversity. Currently introducing the Ebony Tree, along with its unique wood and associated blocks, offering new building and crafting possibilities for your world.

![Blocks](assets/blocks.png)

## Features

*   **Cream Marble**: A new stone type with various decorative blocks like bricks, polished, smooth, and tiles, along with their respective slabs, stairs, and walls.
*   **Multicolor Marble**: Another new stone type with similar decorative block variations as Cream Marble.
*   **Ebony Wood**: A new wood type, including logs, stripped logs, wood, stripped wood, planks, slabs, stairs, fences, fence gates, doors, trapdoors, buttons, and pressure plates.

## Building the Project

This project uses Gradle. To build the mod, navigate to the root directory of the project in your terminal and run:

```bash
./gradlew build
```

This will compile the mod and generate the JAR files in the `build/libs/` directory for each supported loader (Fabric and NeoForge).

## Installation

1.  **Download the Mod**: Obtain the appropriate JAR file for your desired Minecraft version and mod loader (Fabric or NeoForge) from the `build/libs/` directory after building, or from the official release page.
2.  **Install Mod Loader**: Ensure you have the correct version of Fabric Loader or NeoForge installed for your Minecraft client.
3.  **Place the Mod**: Put the downloaded `.jar` file into your Minecraft `mods` folder.
4.  **Launch Game**: Start Minecraft with the installed mod loader profile.

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## World generation and handmade trees

Geco adds rare large marble formations through configured and placed features.
Vanilla terrain generation is preserved on both loaders.

Ebony saplings and natural savanna trees paste the same original NBT schematics
(models 1-4). Model 5 remains excluded. Growth checks occupied cells and preserves
the sapling when placement is obstructed or a write fails.

See [vanilla references, validation and upgrade notes](docs/vanilla-reference-and-validation.md).

## Tests and releases

Run `bash gradlew testFast build` for layered JVM tests, coverage and both loader JARs.
See [testing and release configuration](docs/testing-and-releases.md) for resource checks, runtime tests and CurseForge publishing to project **1296676**.

Install Architectury API alongside Geco on both loaders, and Fabric API on Fabric.
