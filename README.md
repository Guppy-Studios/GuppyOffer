# GuppyOffer

GuppyOffer is a Minecraft plugin that creates an offering system where players can drop specific items in designated WorldGuard regions to receive random rewards.

## Features

- WorldGuard integration for region-specific offerings
- Configurable valid offering items
- Chance-based reward system
- Customizable messages and sounds
- Permission-based reload command

## Dependencies

- WorldGuard

## Configuration Guide

### config.yml Explained
```yaml
# World and region configuration
worlds:
  world:                # The name of your Minecraft world (e.g., "world", "world_nether")
    region: "guppy_shrine"  # The WorldGuard region ID where offerings can be made

# Valid offering items
offerings:
  DIAMOND:             # Minecraft material name (must be exact)
    chance: 100        # This value determines how common this offering type is
                      # Higher numbers mean more common (relative to other offerings)

# Rewards configuration
rewards:
  common_reward:       # Unique identifier for the reward
    name: "Common Reward"   # Display name shown to players
    command: "give {player} diamond 1"  # Command executed when reward is given
                                       # {player} is replaced with the player's name
    chance: 70        # Probability weight for this reward
                      # Higher numbers mean more likely to be chosen

# Sound effects
sounds:
  accepted-offering:   # Sound played when an offering is accepted
    sound: "ENTITY_PLAYER_LEVELUP"  # Minecraft sound name
    volume: 1.0       # Sound volume (0.0 to 1.0)
    pitch: 1.0        # Sound pitch (0.5 to 2.0)
```

### messages.yml Explained
```yaml
messages:
  reward-given: "<green>You received {item-name}!"  # Message shown when a reward is given
                # <green> is a MiniMessage color code
                # {item-name} is replaced with the reward name
```

## Permissions

- `guppyoffer.reload` - Allows use of the `/guppyoffer` reload command

## Commands

- `/guppyoffer` - Reloads the plugin configuration

## Usage Guide

1. Set up a WorldGuard region:
   - Use WorldEdit to select an area
   - Create a region with: `/rg define guppy_shrine`
   - Make sure the region name matches your config.yml

2. Configure items and rewards:
   - Add valid offering items in config.yml
   - Set up rewards with their commands and chances
   - All material names must be valid Minecraft material names

3. Making offerings:
   - Players drop configured items in the WorldGuard region
   - Valid offerings are consumed and grant random rewards
   - Invalid offerings are returned to the player

## Support

For issues, bug reports, or feature requests, please open an issue on our GitHub repository.


# Updated README License Section
## License

This project is proprietary software with restricted permissions. Users may view and modify the code for personal use, but redistribution in any form is prohibited. See the LICENSE file for full terms.
