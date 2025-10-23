[ISSUES]: https://github.com/PssbleTrngle/RegistryDump/issues
[DOWNLOAD]: https://modrinth.com/project/registry-dump/versions
[MODRINTH]: https://modrinth.com/project/registry-dump

<!-- modrinth_exclude.start -->

# Registry Dump <img src="https://raw.githubusercontent.com/PssbleTrngle/RegistryDump/1.21.x/common/src/main/resources/assets/registry_dump/icon.svg" align="right" height="128" />

[![Release](https://img.shields.io/github/v/release/PssbleTrngle/RegistryDump?label=Version&sort=semver)][DOWNLOAD]
[![Issues](https://img.shields.io/github/issues/PssbleTrngle/RegistryDump?label=Issues)][ISSUES]
[![Modrinth](https://img.shields.io/modrinth/dt/UAzbcRGX?color=green&logo=modrinth&logoColor=green)][MODRINTH]

<!-- modrinth_exclude.end -->

Dump all IDs of known registry entries into JSON files.

```
/dump registry
```

A specific registry can also be specified, by default all registries are exported.

```
/dump registry minecraft:item
```

The JSON files are created under `dump` within your minecraft directory and take the following shape:

```
dump/[registry key]/[namespace].json
```

For example

```
dump/banner_pattern/minecraft.json
dump/item/brazier.json
dump/worldgen/biome/minecraft.json
dump/worldgen/biome/environmental.json
```

The content of these files is a simple JSON array containing all keys withing that registry/namespace combo.
