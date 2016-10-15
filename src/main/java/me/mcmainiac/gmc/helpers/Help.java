package me.mcmainiac.gmc.helpers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Help {
	private static final int max_pages = 2;

	static void Show(CommandSender sender, int page) {
		Send(sender, new String[]{
			"\u00A7f-----------------------------",
			"\u00A72Game\u00A7aMode\u00A7fControl\u00A77 - \u00A75Help",
			"\u00A7f-----------------------------",
			"\u00A72<>\u00A7f: \u00A76required\u00A7f; \u00A77[]\u00A7f: \u00A76optional",
		});
		if (!(sender instanceof Player)) page = 1;
		switch (page) {
			case 1:
				Send(sender, new String[]{
						"\u00A77- \u00A7a/gamemode\u00A7f; \u00A7a/gm \u00A72<id> \u00A77[player]",
						"\u00A77- \u00A7a/gm0\u00A7f; \u00A7a/survival \u00A77[player]",
						"\u00A77- \u00A7a/gm1\u00A7f; \u00A7a/creative \u00A77[player]",
						"\u00A77- \u00A7a/gm2\u00A7f; \u00A7a/adventure \u00A77[player]",
						"\u00A77- \u00A7a/gm3\u00A7f; \u00A7a/spectator \u00A77[player]",
					});
				if (sender instanceof Player) break;
			case 2:
				Send(sender, new String[]{
						"\u00A77- \u00A7a/gmonce \u00A72<player> \u00A77[survival | 0]",
						"   \u00A77[creative | 1] [adventure | 2]",
						"   \u00A77[spectator | 3]",
						"\u00A77- \u00A7a/gmtemp \u00A72<player> <id> <seconds>",
						"\u00A77- \u00A7a/gmh \u00A77[page | command]",
						"\u00A77- \u00A7a/gmi",
						"\u00A77- \u00A7a/gmr",
						"\u00A77If you need help for a specific",
						"\u00A77command, use \u00A7a/gmh <command>\u00A77.",
					});
				break;
			default:
				sender.sendMessage("\u00A7cAvailable pages: \u00A76\u00A7o1\u00A77\u00A7o - \u00A76\u00A7o2");
		}
		sender.sendMessage("\u00A7f-----------------------------");
		if (sender instanceof Player)
			sender.sendMessage("\u00A77\u00A7oShowing page \u00A7f\u00A7o" + page + " \u00A77\u00A7oof \u00A7f\u00A7o" + max_pages);
	}

	static void Command(CommandSender sender, String command) {
		Send(sender, new String[]{
			"\u00A7f-----------------------------",
			"\u00A72Game\u00A7aMode\u00A7fControl\u00A77 - \u00A75/" + command,
			"\u00A7f-----------------------------",
		});
		switch(command) {
		case "gamemode":
		case "gm":
			Send(sender, new String[]{
				"\u00A7a/gamemode \u00A72<gamemode id> \u00A77[player]",
				"\u00A7a/gm \u00A72<gamemode id> \u00A77[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player.",
				"",
				"\u00A77Valid values for \u00A72gamemode id\u00A77:",
				" \u00A760\u00A77, \u00A761\u00A77, \u00A762\u00A77, \u00A763\u00A77, \u00A76survival\u00A77, \u00A76creative\u00A77,",
				" \u00A76adventure\u00A77, \u00A76spectator",
			}); break;
		case "survival":
		case "gm0":
			Send(sender, new String[]{
				"\u00A7a/survival \u00A77[player]",
				"\u00A7a/gm0 \u00A77[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player to survival."
			}); break;
		case "creative":
		case "gm1":
			Send(sender, new String[]{
				"\u00A7a/creative \u00A77[player]",
				"\u00A7a/gm1 \u00A77[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player to creative."
			}); break;
		case "adventure":
		case "gm2":
			Send(sender, new String[]{
				"\u00A7a/adventure \u00A77[player]",
				"\u00A7a/gm2 \u00A77[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player to adventure."
			}); break;
		case "spectator":
		case "gm3":
			Send(sender, new String[]{
				"\u00A7a/spectator \u00A77[player]",
				"\u00A7a/gm3 \u00A77[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player to spectator."
			}); break;
		case "gmonce":
			Send(sender, new String[]{
				"\u00A7a/gmonce \u00A72<player> \u00A77[survival | 0]",
				"   \u00A77[creative | 1] [adventure | 2]",
				"   \u00A77[spectator | 3]",
				"",
				" Allow a player to change his/her game",
				" mode only one time to one of those, you",
				" specified."
			}); break;
		case "gmtemp":
			Send(sender, new String[]{
				"\u00A7a/gmtemp \u00A72<player> <gamemode id>",
				"   \u00A72<seconds>",
				"",
				" Change the game mode of a player",
				" only temporary.",
				"",
				"\u00A77Valid values for \u00A72gamemode id\u00A77:",
				" \u00A760\u00A77, \u00A761\u00A77, \u00A762\u00A77, \u00A763\u00A77, \u00A76survival\u00A77, \u00A76creative\u00A77,",
				" \u00A76adventure\u00A77, \u00A76spectator",
			}); break;
		case "gmh":
			Send(sender, new String[]{
				"\u00A7a/gmh \u00A77[page | command]",
				"",
				" Show a list of GMC commands or",
				" help for a specific command."
			}); break;
		case "gmi":
			Send(sender, new String[]{
				"\u00A7a/gmi",
				"",
				" Show information about GMC."
			}); break;
		case "gmr":
			Send(sender, new String[]{
				"\u00A7a/gmr",
				"",
				" Reload the config."
			}); break;
		default:
			sender.sendMessage("\u00A7cUnknown command: " + command);
			break;
		}
		sender.sendMessage("\u00A7f-----------------------------");
	}

	private static void Send(CommandSender sender, String[] messages) {
		for (String message : messages) {
			sender.sendMessage(message);
		}
	}
}
