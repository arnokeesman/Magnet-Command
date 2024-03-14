# Magnet Command
This mod adds a magnet command to the game which allows you to pull items towards you.
### Modes
- Off (default) - The magnet is not active
- Range - The magnet pull items within a range towards you (currently 3 blocks)
- OnBreak - Blocks you break will be directly put in your inventory, if it doesn't fit, the item just drops like normal
  
### The commands
| Command           | Description                   | Permission Node     | OP level |
|-------------------|-------------------------------|---------------------|----------|
| `/magnet Range`   | Set magnet mode to Range      | `magnet.mode.range` | 2*       |
| `/magnet OnBreak` | Set magnet mode to OnBreak    | `magnet.mode.break` | 2*       |
| `/magnet Off`     | Disable magnet behavior       | none                | 0        |
| `/magnet info`    | Shows some info about the mod | none                | 0        |

&ast; can be changed in config

### Config
The config is located at `/config/magnet-command.json`.

| Option                                | Description                                                                   | Default value |
|---------------------------------------|-------------------------------------------------------------------------------|---------------|
| `permissionLevel`                     | The default permission/OP level for all modes                                 | 2             |
| `modes.*.enabled`                     | Enable/disable modes                                                          | true          |
| `modes.Range.range`                   | The range in blocks for Range mode                                            | 3             |
| `modes.Range.mode`                    | Movement mode for the magnet. Options: Pull, Teleport                         | "Pull"        |
| `modes.Range.pullStrengthMultiplier`  | Increase or decrease the pull speed for items                                 | 1.0           |
| `modes.Range.skipCanPickUpCheck`      | Skipping this check makes it so that items are pulled towards you immediately | false         |
| `modes.OnBreak.dropLocation`          | Where to drop items if they don't fit in inventory. Options: Block, Player    | "Block"       |
| `modes.OnBreak.includeContainerItems` | Enable/disable injecting items from containers like chests and furnaces       | true          |