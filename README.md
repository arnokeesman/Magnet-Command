# Magnet Command
This mod adds a magnet command to the game which allows you to pull items towards you.
### Modes
- Off (default) - The magnet is not active
- Range - The magnet pull items within a range towards you (currently 3 blocks)
- OnBreak - Blocks you break will be directly put in your inventory, if it doesn't fit, the item just drops like normal
  
### Users are not allowed to use the magnet by default
To enable the magnet you need permission level 2 or access to the permission node for the mode you're enabling.
| Mode    | Permission Node     |
|---------|---------------------|
| Range   | `magnet.mode.range` |
| OnBreak | `magnet.mode.break` |

### Config
The config is located at `/config/magnet-command.json`.
| Option                       | Description                                                                | Default value |
|------------------------------|----------------------------------------------------------------------------|---------------|
| `permissionLevel`            | The default permission/OP level for all modes                              | 2             |
| `modes.*.enabled`            | Enable/disable modes                                                       | true          |
| `modes.Range.range`          | The range in blocks for Range mode                                         | 3             |
| `modes.Range.mode`           | Movement mode for the magnet. Options: Pull, Teleport                      | "Pull"        |
| `modes.OnRange.dropLocation` | Where to drop items if they don't fit in inventory. Options: Block, Player | "Block"       |