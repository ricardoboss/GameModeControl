package de.ricardoboss.gmc.helpers;

import de.ricardoboss.gmc.utils.MessageColor;
import de.ricardoboss.gmc.utils.MessageFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Help {
    private static final int max_pages = 2;

    static void Show(CommandSender sender, int page) {
        Send(sender, new String[]{
                MessageColor.WHITE + "-----------------------------",
                MessageColor.DARK_GREEN + "Game" + MessageColor.GREEN + "Mode" + MessageColor.WHITE + "Control" +
                        MessageColor.GRAY + " - " + MessageColor.DARK_PURPLE + "Help",
                MessageColor.WHITE + "-----------------------------",
                MessageColor.DARK_GREEN + "<>" + MessageColor.WHITE + ": " + MessageColor.GOLD + "required" +
                        MessageColor.WHITE + "; " + MessageColor.GRAY + "[]" + MessageColor.WHITE + ": " +
                        MessageColor.GOLD + "optional" + MessageColor.WHITE + "; " + MessageColor.GRAY + "|" +
                        MessageColor.WHITE + ": " + MessageColor.GOLD + "or"
        });
        if (!(sender instanceof Player)) page = 1;
        switch (page) {
            case 1:
                Send(sender, new String[]{
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gamemode" + MessageColor.WHITE + "; " +
                                MessageColor.GREEN + "/gm " + MessageColor.DARK_GREEN + "<id> " + MessageColor.GRAY +
                                "[player]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gm0" + MessageColor.WHITE + "; " +
                                MessageColor.GREEN + "/survival " + MessageColor.GRAY + "[player]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gm1" + MessageColor.WHITE + "; " +
                                MessageColor.GREEN + "/creative " + MessageColor.GRAY + "[player]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gm2" + MessageColor.WHITE + "; " +
                                MessageColor.GREEN + "/adventure " + MessageColor.GRAY + "[player]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gm3" + MessageColor.WHITE + "; " +
                                MessageColor.GREEN + "/spectator " + MessageColor.GRAY + "[player]",
                });
                if (sender instanceof Player) break;
            case 2:
                Send(sender, new String[]{
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gmonce " + MessageColor.DARK_GREEN +
                                "<player> " + MessageColor.GRAY + "[survival | 0]",
                        "   " + MessageColor.GRAY + "[creative | 1] [adventure | 2]",
                        "   " + MessageColor.GRAY + "[spectator | 3]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gmtemp " + MessageColor.DARK_GREEN +
                                "<player> <id> <seconds>",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gmh " + MessageColor.GRAY + "[page | command]",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gmi",
                        MessageColor.GRAY + "- " + MessageColor.GREEN + "/gmr",
                        MessageColor.GRAY + "If you need help for a specific",
                        MessageColor.GRAY + "command, use " + MessageColor.GREEN + "/gmh <command>" +
                                MessageColor.GRAY + ".",
                });
                break;
            default:
                sender.sendMessage(MessageColor.RED + "Available pages: " + MessageColor.GOLD + MessageFormat.ITALIC +
                        "1" + MessageColor.GRAY + MessageFormat.ITALIC + " - " + MessageColor.GOLD +
                        MessageFormat.ITALIC + "2");
        }
        sender.sendMessage(MessageColor.WHITE + "-----------------------------");
        if (sender instanceof Player)
            sender.sendMessage(MessageColor.GRAY.toString() + MessageFormat.ITALIC + "Showing page " +
                    MessageColor.WHITE + MessageFormat.ITALIC + "" + page + " " + MessageColor.GRAY +
                    MessageFormat.ITALIC + "of " + MessageColor.WHITE + MessageFormat.ITALIC + "" + max_pages);
    }

    static void Command(CommandSender sender, String command) {
        Send(sender, new String[]{
                MessageColor.WHITE + "-----------------------------",
                MessageColor.DARK_GREEN + "Game" + MessageColor.GREEN + "Mode" + MessageColor.WHITE + "Control" +
                        MessageColor.GRAY + " - " + MessageColor.DARK_PURPLE + "/" + command,
                MessageColor.WHITE + "-----------------------------",
        });

        final var availableGameModes = " " +
                MessageColor.GOLD + "0" + MessageColor.GRAY + ", " +
                MessageColor.GOLD + "1" + MessageColor.GRAY + ", " +
                MessageColor.GOLD + "2" + MessageColor.GRAY + ", " +
                MessageColor.GOLD + "3" + MessageColor.GRAY + ", " +
                MessageColor.GOLD + "survival" + MessageColor.GRAY + ", " +
                MessageColor.GOLD + "creative" + MessageColor.GRAY + ",";

        switch (command) {
            case "gamemode":
            case "gm":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gamemode " + MessageColor.DARK_GREEN + "<gamemode id> " + MessageColor.GRAY + "[player]",
                        MessageColor.GREEN + "/gm " + MessageColor.DARK_GREEN + "<gamemode id> " + MessageColor.GRAY + "[player]",
                        "",
                        " Set your own game mode or the game",
                        " mode of a specific player.",
                        "",
                        MessageColor.GRAY + "Valid values for " + MessageColor.DARK_GREEN + "gamemode id" + MessageColor.GRAY + ":",
                        availableGameModes,
                        " " + MessageColor.GOLD + "adventure" + MessageColor.GRAY + ", " + MessageColor.GOLD + "spectator",
                });
                break;
            case "survival":
            case "gm0":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/survival " + MessageColor.GRAY + "[player]",
                        MessageColor.GREEN + "/gm0 " + MessageColor.GRAY + "[player]",
                        "",
                        " Set your own game mode or the game",
                        " mode of a specific player to survival."
                });
                break;
            case "creative":
            case "gm1":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/creative " + MessageColor.GRAY + "[player]",
                        MessageColor.GREEN + "/gm1 " + MessageColor.GRAY + "[player]",
                        "",
                        " Set your own game mode or the game",
                        " mode of a specific player to creative."
                });
                break;
            case "adventure":
            case "gm2":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/adventure " + MessageColor.GRAY + "[player]",
                        MessageColor.GREEN + "/gm2 " + MessageColor.GRAY + "[player]",
                        "",
                        " Set your own game mode or the game",
                        " mode of a specific player to adventure."
                });
                break;
            case "spectator":
            case "gm3":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/spectator " + MessageColor.GRAY + "[player]",
                        MessageColor.GREEN + "/gm3 " + MessageColor.GRAY + "[player]",
                        "",
                        " Set your own game mode or the game",
                        " mode of a specific player to spectator."
                });
                break;
            case "gmonce":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gmonce " + MessageColor.DARK_GREEN + "<player> " + MessageColor.GRAY + "[survival | 0]",
                        "   " + MessageColor.GRAY + "[creative | 1] [adventure | 2]",
                        "   " + MessageColor.GRAY + "[spectator | 3]",
                        "",
                        " Allow a player to change their game",
                        " mode only one time to one of those, you",
                        " specified."
                });
                break;
            case "gmtemp":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gmtemp " + MessageColor.DARK_GREEN + "<player> <gamemode id>",
                        "   " + MessageColor.DARK_GREEN + "<seconds>",
                        "",
                        " Change the game mode of a player",
                        " only temporary.",
                        "",
                        MessageColor.GRAY + "Valid values for " + MessageColor.DARK_GREEN + "gamemode id" + MessageColor.GRAY + ":",
                        availableGameModes,
                        " " + MessageColor.GOLD + "adventure" + MessageColor.GRAY + ", " + MessageColor.GOLD + "spectator",
                });
                break;
            case "gmh":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gmh " + MessageColor.GRAY + "[page | command]",
                        "",
                        " Show a list of GMC commands or",
                        " help for a specific command."
                });
                break;
            case "gmi":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gmi",
                        "",
                        " Show information about GMC."
                });
                break;
            case "gmr":
                Send(sender, new String[]{
                        MessageColor.GREEN + "/gmr",
                        "",
                        " Reload the config."
                });
                break;
            default:
                sender.sendMessage(MessageColor.ERROR + "Unknown command: " + command);
                break;
        }
        sender.sendMessage(MessageColor.WHITE + "-----------------------------");
    }

    private static void Send(CommandSender sender, String[] messages) {
        for (String message : messages) {
            sender.sendMessage(message);
        }
    }
}
