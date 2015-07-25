package com.mcmainiac.gmc;

import org.bukkit.command.CommandSender;

public class Help {
	public static final int max_pages = 2;
	
	public static void Show(CommandSender sender, int page) {
		Send(sender, new String[]{
			"§f--------------------",
			"§2Game§aMode§fControl§7 - §5Help",
			"§f--------------------",
			"§2<>§f: §6required§f; §7[]§f: §6optional",
		});
		switch(page) {
		case 1:
			Send(sender, new String[]{
				"§7- §a/gamemode§f; §a/gm §2<id | name> §7[player]",
				"§7- §a/gm0§f; §a/survival §7[player]",
				"§7- §a/gm1§f; §a/creative §7[player]",
				"§7- §a/gm2§f; §a/adventure §7[player]",
				"§7- §a/gm3§f; §a/spectator §7[player]",
			}); break;
		case 2:
			Send(sender, new String[]{
				"§7- §a/gmh §7[page | command]",
				"§7- §a/gmi",
				"§7- §a/gmr",
				"§7If you need help for a specific",
				"§7command, use §a/gmh <command>§7.",	
			}); break;
		default:
			sender.sendMessage("§cAvailable pages: §6§o1§7§o - §6§o2");
			break;
		}
		sender.sendMessage("§f--------------------");
		sender.sendMessage("§7§oShowing page §f§o" + page + " §7§oof §f§o" + max_pages);
	}
	
	public static void Command(CommandSender sender, String command) {
		Send(sender, new String[]{
			"§f--------------------",
			"§2Game§aMode§fControl§7 - §5/" + command,
			"§f--------------------",	
		});
		switch(command) {
		case "gamemode":
		case "gm":
			Send(sender, new String[]{
				"§a/gamemode §2<gamemode id> §7[player]",
				"§a/gm §2<gamemode id> §7[player]",
				"",
				" Set your own game mode or the game",
				" mode of a specific player.",
				"",
				"§7Valid values for §2gamemode id§7:",
				" §60§7, §61§7, §62§7, §63§7, §6survival§7, §6creative§7,",
				" §6adventure§7, §6spectator",
			}); break;
		case "survival":
		case "gm0":
			Send(sender, new String[]{
				"§a/survival §7[player]",
				"§a/gm0 §7[player]",
				"",
				" Set you own game mode or the game",
				" mode of a specific player to survival."
			}); break;
		case "creative":
		case "gm1":
			Send(sender, new String[]{
				"§a/creative §7[player]",
				"§a/gm1 §7[player]",
				"",
				" Set you own game mode or the game",
				" mode of a specific player to creative."
			}); break;
		case "adventure":
		case "gm2":
			Send(sender, new String[]{
				"§a/adventure §7[player]",
				"§a/gm2 §7[player]",
				"",
				" Set you own game mode or the game",
				" mode of a specific player to adventure."
			}); break;
		case "spectator":
		case "gm3":
			Send(sender, new String[]{
				"§a/spectator §7[player]",
				"§a/gm3 §7[player]",
				"",
				" Set you own game mode or the game",
				" mode of a specific player to spectator."
			}); break;
		case "gmh":
			Send(sender, new String[]{
				"§a/gmh §7[page | command]",
				"",
				" Show a list of GMC commands or",
				" help for a specific command."
			}); break;
		case "gmi":
			Send(sender, new String[]{
				"§a/gmc",
				"§a/gmi",
				"",
				" Show information about GMC."
			}); break;
		case "gmr":
			Send(sender, new String[]{
				"§a/gmr",
				"",
				" Reload the config and the whole plugin."
			}); break;
		default:
			sender.sendMessage("§cUnknown command: " + command);
			break;
		}
		sender.sendMessage("§f--------------------");
	}
	
	private static void Send(CommandSender sender, String[] messages) {
		for (String message : messages) {
			sender.sendMessage(message);
		}
	}
}
