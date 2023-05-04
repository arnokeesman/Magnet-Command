# Magnet Command
This mod adds a magnet command to the game which allows you to pull items towards you.
### Current modes
- Off (default) - The magnet is not active
- Range - The magnet pull items within a range towards you (currently 3 blocks)
- OnBreak - Blocks you break will be directly put in your inventory, if it doesn't fit, the item just drops like normal
  
### Users are not allowed to use the magnet by default
To enable the magnet you need permission level 2 or access to the permission node for the mode you're enabling.
| Mode    | Permission Node     |
|---------|---------------------|
| Range   | `magnet.mode.range` |
| OnBreak | `magnet.mode.break` |

### Planned features
- A config
- Config option to configure the radius of Range mode
- Config option to set the required permission level
