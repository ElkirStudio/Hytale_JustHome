# JustHome

A home management plugin for Hytale servers.

**Made by razday - Elkir Studio**

## Features

- `/sethome <name>` - Set a home at your current location
- `/home <name>` - Teleport to a saved home
- `/homes` - List all your homes
- `/delhome <name>` - Delete a home

## Configuration

All configuration files are created in `mods/JustHome/` folder:

### config.json
```json
{
    "maxHomes": 5,
    "language": "en"
}
```

- `maxHomes` - Maximum number of homes per player
- `language` - Language code (`en` for English, `fr` for French)

### Language Files

- `lang_en.json` - English messages
- `lang_fr.json` - French messages

You can customize all messages by editing these files.

## Installation

1. Install [Kotale](https://github.com/helight/kotale) (required dependency)
2. Download `JustHome-1.0.0-all.jar`
3. Place it in your server's `mods/` folder
4. Restart the server

## Building

```bash
./gradlew :mods:home:shadowJar
```

The JAR will be in `build/libs/JustHome-x.x.x-all.jar`

## Dependencies

- Kotale (https://github.com/helightdev/kotale/releases)
- Kotlin Serialization (https://github.com/helightdev/kotale/releases)

## License

MIT
