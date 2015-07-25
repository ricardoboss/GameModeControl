package com.mcmainiac.gmc;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;
import com.mcmainiac.gmc.CGM.ControlledGameMode;

/**
 * GameModeControl V1.3
 * 
 * Helps you and your admins to control
 * game modes faster and more accurate
 * than ever before.
 * 
 * {@link} http://bit.ly/MC-GMC
 * @version V1.3
 * @author MCMainiac
 */
public class Main extends JavaPlugin implements Listener {
	private static final ConsoleCommandSender console = Bukkit.getConsoleSender();
	private static final String pre = "§7[§2G§aM§fC§7] §r";
	
	private HashMap<String, Permission> permissions = new HashMap<String, Permission>();
	
	public static Config config;

	//--------------
	// Plugin commands
	//--------------
	@Override
	public void onEnable() {
		try {
			log("Initializing GMC...");
			Main.config = new Config(this);
			Bukkit.getPluginManager().registerEvents(this, this);
			
			// Permissions
			permissions.put("gmi", new Permission("gmc.gmi", PermissionDefault.TRUE));
			permissions.put("gmh", new Permission("gmc.gmh", PermissionDefault.TRUE));
			permissions.put("gmr", new Permission("gmc.gmr", PermissionDefault.OP));
			
			permissions.put("gm", new Permission("gmc.gm", PermissionDefault.OP));
			permissions.put("gm.self", new Permission("gmc.gm.self", PermissionDefault.OP));
			permissions.put("gm.others", new Permission("gmc.gm.others", PermissionDefault.OP));
			permissions.put("gamemode", new Permission("gmc.gamemode", PermissionDefault.OP));
			permissions.put("gamemode.self", new Permission("gmc.gamemode.self", PermissionDefault.OP));
			permissions.put("gamemode.others", new Permission("gmc.gamemode.others", PermissionDefault.OP));
			permissions.put("survival", new Permission("gmc.survival", PermissionDefault.OP));
			permissions.put("survival.self", new Permission("gmc.survival.self", PermissionDefault.OP));
			permissions.put("survival.others", new Permission("gmc.survival.others", PermissionDefault.OP));
			permissions.put("creative", new Permission("gmc.creative", PermissionDefault.OP));
			permissions.put("creative.self", new Permission("gmc.creative.self", PermissionDefault.OP));
			permissions.put("creative.others", new Permission("gmc.creative.others", PermissionDefault.OP));
			permissions.put("adventure", new Permission("gmc.adventure", PermissionDefault.OP));
			permissions.put("adventure.self", new Permission("gmc.adventure.self", PermissionDefault.OP));
			permissions.put("adventure.others", new Permission("gmc.adventure.others", PermissionDefault.OP));
			permissions.put("spectator", new Permission("gmc.spectator", PermissionDefault.OP));
			permissions.put("spectator.self", new Permission("gmc.spectator.self", PermissionDefault.OP));
			permissions.put("spectator.others", new Permission("gmc.spectator.others", PermissionDefault.OP));
			
			log("GMC §aenabled§7 successfully!");
		} catch(IOException ioe) {
			log("GMC crashed while initializing!", MessageColor.ERROR);
			ioe.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}

	@Override
	public void onDisable() {
		log("Plugin §cdisabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		try {
			switch (commandLabel.toLowerCase()) {
			case "gamemode":
			case "gm":
				return (args.length > 0 ? Gamemode(sender, new String[]{args[0], (args.length > 1 ? args[1] : null), "gamemode"}) : false);
			case "gm0":
			case "survival":
				return Gamemode(sender, new String[]{"0", (args.length > 0 ? args[0] : null), "survival"});
			case "gm1":
			case "creative":
				return Gamemode(sender, new String[]{"1", (args.length > 0 ? args[0] : null), "creative"});
			case "gm2":
			case "adventure":
				return Gamemode(sender, new String[]{"2", (args.length > 0 ? args[0] : null), "adventure"});
			case "gm3":
			case "spectator":
				return Gamemode(sender, new String[]{"3", (args.length > 0 ? args[0] : null), "spectator"});

			case "gmh": return Help(sender, args);
			case "gmi": return Info(sender, args);
			case "gmr": return Reload(sender);
			default: return false;
			}
		} catch (InvalidParameterException ipe) {
			log("Please check your config", MessageColor.ERROR);
		} catch (GameModeNotFoundException gme) {
			log("The specified GameMode was not found!", MessageColor.ERROR);
			log("Are you using the right GMC version for your server?", MessageColor.ERROR);
			gme.printStackTrace();
		} catch (PlayerNotFoundException pe) {
			send(sender, config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", (args.length > 1 ? args[1] : args[0])).build());
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		try {
			if (config.getBoolean("force-gamemode.enable")) {
				if (e.getPlayer().hasPermission("force-gamemode")) {
					e.getPlayer().setGameMode(CGM.getGamemodeByIdOrName(config.get("force-gamemode.mode")));
				} else
					log("Didn't change " + e.getPlayer().getName() + "'s gamemode");
			}
		} catch(GameModeNotFoundException gme) {
			log("Error: please check you config at 'force-gamemode.mode'", MessageColor.ERROR);
			gme.printStackTrace();
		}
		log("Player " + e.getPlayer().getName() + " has gamemode " + e.getPlayer().getGameMode().toString().toUpperCase());
	}
	
	//--------------
	// Commands
	//--------------
	private boolean Gamemode(CommandSender sender, String[] args)
			throws InvalidParameterException, GameModeNotFoundException, PlayerNotFoundException {
		ControlledGameMode cgm;
		try {
			cgm = CGM.getCGMByIdOrName(args[0]);
		} catch (GameModeNotFoundException gme) {
			send(sender, config.getString("Other.gamemode not found"), ImmutableMap.<String, String>builder().put("$gm", args[0].toUpperCase()).build());
			return false;
		}
		if (sender instanceof Player)
			if (args[1] == null) // check if there is a player specified
				if (sender.hasPermission(permissions.get(args[args.length-1] + ".self")) || // Either you have permission to set your own game mode
					sender.hasPermission(permissions.get(args[args.length-1]))) // or you have the global permission to set this game mode.
					CGM.set((Player)sender, cgm);
				else
					send((Player)sender, config.getString("Other.no permission")); // Oops you don't have permission to do that
			else
				if (sender.hasPermission(permissions.get(args[args.length-1] + ".others")) || // the same over here, except that you 
					sender.hasPermission(permissions.get(args[args.length-1]))) // have to have the permission to change someone else's game mode
					CGM.set(getPlayerByName(args[1]), (Player)sender, cgm); // since getPlayer() is only for this session, it's fine to use it
				else
					send((Player)sender, config.getString("Other.no permission"));
		else
			if (args[1] == null) return false; // we know it's the console or a command block here, so there MUST be a player specified
			else CGM.set(getPlayerByName(args[1]), sender, cgm); // so just change the game mode (console has good mode activated)
		return true;
	}
	
	private boolean Reload(CommandSender sender) {
		if (sender.hasPermission(permissions.get("gmr"))) {
			config.reload();
			sender.sendMessage(pre + "Config §3reloaded");
		} else
			send(sender, config.getString("Other.no permission"));
		return true;
	}
	
	private boolean Info(CommandSender sender, String[] args) {
		if (sender.hasPermission(permissions.get("gmi"))) {
			sender.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
			sender.sendMessage("§aVersion§7: §9[Beta] §f1.3");
			sender.sendMessage("§aAuthor§7: §fMCMainiac");
			sender.sendMessage("§aWebsite§7: §5§nhttp://bit.ly/MC-GMC");
			sender.sendMessage("§f-----------------------------");	
		} else
			send(sender, config.getString("Other.no permission"));
		return true;
	}
	
	private boolean Help(CommandSender sender, String[] args) {
		if (args.length == 0)
			if (sender instanceof Player)
				Help.Show(sender, 1);
			else
				for (int i = 1; i < Help.max_pages; i++) // show the console the whole help
					Help.Show(sender, i);
		else if (args.length == 1)
			try {
				Help.Show(sender, Integer.valueOf(args[0]));
			} catch (NumberFormatException e) {
				Help.Command(sender, args[0]);
			}
		else
			return false;
		return true;
	}
	
	//--------------
	// Utilities
	//--------------
	@SuppressWarnings("deprecation")
	public static Player getPlayerByName(String name) throws PlayerNotFoundException {
		Player p = Bukkit.getServer().getPlayer(name);
		if (p != null) return p;
		else throw new PlayerNotFoundException("Player not found: '" + name + "'");
	}
	
	public static void log(Object o) {
		log((o != null ? o.toString() : "null"), MessageColor.INFO);
	}
	
	public static void log(String log) {
		log(log, MessageColor.INFO);
	}

	public static void log(String[] logs) {
		for (String log : logs) {
			log(log, MessageColor.INFO);
		}
	}
	
	public static void log(Object o, MessageColor color) {
		log((o != null ? o.toString() : "null"), color);
	}
	
	public static void log(String log, MessageColor color) {
		switch (color) {
		case BLACK:	console.sendMessage(pre + "§0" + log); break;
		case DARK_BLUE: console.sendMessage(pre + "§1" + log); break;
		case DARK_GREEN: console.sendMessage(pre + "§2" + log); break;
		case DARK_AQUA: console.sendMessage(pre + "§3" + log); break;
		case DARK_RED: console.sendMessage(pre + "§4" + log); break;
		case DARK_PURPLE: console.sendMessage(pre + "§5" + log); break;
		case GOLD:
			case GM_SURVIVAL:
			console.sendMessage(pre + "§6" + log); break;
		case GRAY:
			default:
			console.sendMessage(pre + "§7" + log); break;
		case DARK_GRAY: console.sendMessage(pre + "§8" + log); break;
		case BLUE:
			case GM_ADVENTURE:
			console.sendMessage(pre + "§9" + log); break;
		case GREEN:
			case SUCCESS:
			case GM_SPECTATOR:
			console.sendMessage(pre + "§a" + log); break;
		case AQUA:
			case GM_CREATIVE: 
			console.sendMessage(pre + "§b" + log); break;
		case RED:
			case ERROR:
			console.sendMessage(pre + "§c" + log); break;
		case LIGHT_PURPLE: console.sendMessage(pre + "§d" + log); break;
		case YELLOW:
			case WARNING:
			console.sendMessage(pre + "§e" + log); break;
		case WHITE: console.sendMessage(pre + "§f" + log); break;
		}
	}
	
	public static void log(String[] logs, MessageColor color) {
		for (String log : logs) {
			log(log, color);
		}
	}
	
	public static void send(Player p, String message) {
		message = replaceColorCodes(message);
		p.sendMessage(message);
	}

	public static void send(CommandSender cs, String message) {
		message = replaceColorCodes(message);
		if (cs instanceof Player) cs.sendMessage(message);
		else log(message);
	}

	public static void send(CommandSender cs, String message, ImmutableMap<String, String> context) {
		message = replaceColorCodes(message);
		
		for (String target : context.keySet()) {
			if (message.contains(target)) {
				String first = message.substring(0, message.indexOf(target));
				String last = message.substring(message.indexOf(target) + target.length(), message.length());
				message = first + context.get(target) + last;
			}
		}
		
		if (cs instanceof Player) send((Player)cs, message);
		else log(message);
	}
	
	public static void broadcast(String message) {
		if (message != null) message = message.replaceAll("&([0-9a-fk-or])", "§$1");
		Bukkit.broadcastMessage(pre + (message != null ? message : "null"));
	}

	public static void broadcast(String[] messages) {
		for (String message : messages) {
			broadcast(message);
		}
	}

	public static String replaceColorCodes(String message) {
		message = message.replaceAll("&([0-9a-f])", "§$1");
		message = message.replaceAll("&([k-o])", "§$1");
		message = message.replaceAll("&r", "§r");
		return message;
	}
}

/**
 * It's actually an alias for ChatColor.
 * Just for static SUCCESS, INFO, WARNING, 
 * ERROR if I want to change one of those later.
 * 
 * @version V1.0
 * @author MCMainiac
 */
enum MessageColor {
	SUCCESS,
	INFO,
	WARNING,
	ERROR,
	
	GM_SURVIVAL,
	GM_CREATIVE,
	GM_ADVENTURE,
	GM_SPECTATOR,
	
	BLACK,
	DARK_BLUE,
	DARK_GREEN,
	DARK_AQUA,
	DARK_RED,
	DARK_PURPLE,
	GOLD,
	GRAY,
	DARK_GRAY,
	BLUE,
	GREEN,
	AQUA,
	RED,
	LIGHT_PURPLE,
	YELLOW,
	WHITE;
	
	@Override
	public String toString() {
		switch(this) {
		case BLACK:	return "§0";
		case DARK_BLUE: return "§1";
		case DARK_GREEN: return"§2";
		case DARK_AQUA: return"§3";
		case DARK_RED: return"§4";
		case DARK_PURPLE: return"§5";
		case GOLD:
			case GM_SURVIVAL:
			return"§6";
		case GRAY:
			default:
			return"§7";
		case DARK_GRAY: return"§8";
		case BLUE:
			case GM_ADVENTURE:
			return"§9";
		case GREEN:
			case SUCCESS:
			case GM_SPECTATOR:
			return"§a";
		case AQUA:
			case GM_CREATIVE: 
			return"§b";
		case RED:
			case ERROR:
			return"§c";
		case LIGHT_PURPLE: return"§d";
		case YELLOW:
			case WARNING:
			return"§e";
		case WHITE: return"§f";
		}
	}
}
