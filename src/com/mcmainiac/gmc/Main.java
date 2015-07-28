package com.mcmainiac.gmc;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;
import com.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import com.mcmainiac.gmc.excpetions.PlayerNotFoundException;
import com.mcmainiac.gmc.helpers.CGM;
import com.mcmainiac.gmc.helpers.CGM.ControlledGameMode;
import com.mcmainiac.gmc.helpers.Config;
import com.mcmainiac.gmc.helpers.Help;
import com.mcmainiac.gmc.helpers.MessageColor;
import com.mcmainiac.gmc.helpers.Updater;

/**
 * GameModeControl V1.3.1
 * 
 * Helps you and your admins to control
 * game modes faster and more accurate
 * than ever before.
 * 
 * {@link} http://bit.ly/MC-GMC
 * @version V1.3.1
 * @author MCMainiac
 */
public class Main extends JavaPlugin implements Listener {
	private static final String pre = "§7[§2G§aM§fC§7] §r";
	private static CommandSender console = Bukkit.getConsoleSender();
	
	private HashMap<String, Permission> permissions = new HashMap<String, Permission>(); // A map for all the permissions (fast access to dynamic permissions)
	private HashMap<Player, Boolean[]> otgm = new HashMap<Player, Boolean[]>(); // A map to save, which players are able to change their gamemode
	
	public static Config config;

	//--------------
	// Plugin commands
	//--------------
	@Override
	public void onEnable() {
		try {
			log("Initializing GMC §2" + this.getDescription().getVersion());
			Main.config = new Config(this);
			Bukkit.getPluginManager().registerEvents(this, this);
			
			// Permissions
			permissions.put("gmi", new Permission("gmc.gmi", PermissionDefault.TRUE));
			permissions.put("gmh", new Permission("gmc.gmh", PermissionDefault.TRUE));
			permissions.put("forcegm", new Permission("gmc.forcegm", PermissionDefault.TRUE));
			permissions.put("gmr", new Permission("gmc.gmr", PermissionDefault.OP));
			permissions.put("gmonce", new Permission("gmc.gmonce", PermissionDefault.OP));
			
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
			
			for (Player p : Bukkit.getOnlinePlayers()) { // if the server has been reloaded, this loop adds all player to the change-gm-one-time-map
				otgm.put(p, new Boolean[]{false, false, false, false}); // default is false for every gamemode
			}
			
			// Auto-Updater
			checkForUpdates(config.getBoolean("options.auto-update"));
			
			log("GMC §2enabled§7 successfully!");
		} catch(IOException ioe) {
			log("GMC crashed while initializing!", MessageColor.ERROR);
			ioe.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
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

			case "gmonce":
				return OneTimeGamemode(sender, args);
			
			case "gmh": return Help(sender, args);
			case "gmi": return Info(sender, args);
			case "gmr": return Reload(sender);
			default: return false;
			}
		} catch (InvalidParameterException ipe) {
			log("An invalid parameter was given!", MessageColor.ERROR);
			log("Please check your config!", MessageColor.ERROR);
		} catch (GameModeNotFoundException gme) {
			log("The specified GameMode was not found!", MessageColor.ERROR);
			gme.printStackTrace();
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		try {
			if (config.getBoolean("options.force-gamemode.enable")) {
				if (e.getPlayer().hasPermission("options.force-gamemode")) {
					e.getPlayer().setGameMode(CGM.getGamemodeByIdOrName(config.get("options.force-gamemode.mode")));
				} else
					log("Didn't change " + e.getPlayer().getName() + "'s gamemode");
			}
		} catch(GameModeNotFoundException gme) {
			log("Error: please check your config at 'options.force-gamemode.mode'", MessageColor.ERROR);
			gme.printStackTrace();
		}
		otgm.put(e.getPlayer(), new Boolean[]{false, false, false, false}); // add the player to the change-gm-one-time-map
		log("Player " + e.getPlayer().getName() + " has gamemode " + e.getPlayer().getGameMode().toString().toUpperCase());
		if (e.getPlayer().getName() == "MCMainiac") broadcast("Hey MCMainiac! Author of GameModeControl!"); // this is our little secret :)
	}
	
	//--------------
	// Commands
	//--------------
	private boolean Gamemode(CommandSender sender, String[] args)
			throws InvalidParameterException, GameModeNotFoundException {
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
						sender.hasPermission(permissions.get(args[args.length-1])) || // or you have the global permission to set this game mode.
						otgm.get(sender)[cgm.getId()]) { // or the player has been allowed to change his/her game mode via the /gmonce command
					CGM.set((Player)sender, cgm);
					otgm.put((Player) sender, new Boolean[]{false, false, false, false}); // reset the one-time-gm-map for this player
				}
				else
					send((Player)sender, config.getString("Other.no permission")); // Oops you don't have permission to do that
			else
				try {
					if (sender.hasPermission(permissions.get(args[args.length-1] + ".others")) || // the same over here, except that you 
						sender.hasPermission(permissions.get(args[args.length-1])))
							CGM.set(getPlayerByName(args[1]), (Player)sender, cgm);
					else
						send(sender, config.getString("Other.no permission"));
				} catch (PlayerNotFoundException e) {
					send(sender, config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[1]).build());
				}
		else
			if (args[1] == null) return false; // we know it's the console or a command block here, so there MUST be a player specified
			else
				try {
					CGM.set(getPlayerByName(args[1]), sender, cgm); // so just change the game mode (console has god mode activated)
				} catch (PlayerNotFoundException e) {
					send(sender, config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[1]).build());
				}
		return true;
	}
	
	private boolean Reload(CommandSender sender) {
		if (!sender.hasPermission(permissions.get("gmr"))) { send(sender, config.getString("Other.no permission")); return true; }
		config.reload();
		
		// notify new settings
		if (config.getBoolean("options.auto-update")) {
			log("Auto-update §aenabled§7!");
			checkForUpdates(true);
			log("---------");
		}
		
		if (config.getBoolean("options.force-gamemode.enable")) {
			try {
				ControlledGameMode cgm = CGM.getCGMByIdOrName(config.getString("options.force-gamemode.mode"));
				this.getServer().setDefaultGameMode(cgm.getGamemode());
				log("Forcing gamemode " + cgm.getMessageColor() + cgm.getName() + " §7on player join");
			} catch (InvalidParameterException | GameModeNotFoundException e) {
				log("You specified a wrong parameter for 'options.force-gamemode.mode'!", MessageColor.ERROR);
				log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);
				config.set("options.force-gamemode.mode", 0);
				this.saveConfig();
				this.reloadConfig();
			}
			log("---------");
		}
		
		if (sender instanceof Player) log("Config §3reloaded");
		sender.sendMessage(pre + "Config §3reloaded");
		return true;
	}
	
	private boolean Info(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permissions.get("gmi"))) { send(sender, config.getString("Other.no permission")); return true; }
		sender.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
		sender.sendMessage("§aVersion§7: §9[Beta] §f" + this.getDescription().getVersion());
		sender.sendMessage("§aAuthor§7: §fMCMainiac");
		sender.sendMessage("§aWebsite§7: §5§nhttp://bit.ly/MC-GMC");
		sender.sendMessage("§f-----------------------------");
		return true;
	}
	
	private boolean Help(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permissions.get("gmh"))) { send(sender, config.getString("Other.no permission")); return true; }
		if (args.length == 0)
			Help.Show(sender, 1);
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
	
	private boolean OneTimeGamemode(CommandSender sender, String[] args) {
		if (!sender.hasPermission(permissions.get("gmonce"))) { send(sender, config.getString("Other.no permission")); return true; }
		if (args.length > 0) {
			try {
				boolean survival = (args.length == 1 ? true : false), 
						creative = (args.length == 1 ? true : false), 
						adventure = (args.length == 1 ? true : false), 
						spectator = (args.length == 1 ? true : false); // default for all game modes is false
				
				for (int i = 1; i < args.length; i++) { // go through all parameters and check, which game modes have been enabled by the operator
					if (args[i].equalsIgnoreCase("survival")) survival = true;
					if (args[i].equalsIgnoreCase("creative")) creative = true;
					if (args[i].equalsIgnoreCase("adventure")) adventure = true;
					if (args[i].equalsIgnoreCase("spectator")) spectator = true;
				}
				
				otgm.put(getPlayerByName(args[0]), new Boolean[]{survival, creative, adventure, spectator});
				sender.sendMessage("§6" + args[0] + " §fhas the permission to change his/her game mode to:");
				String message = "";
				for (ControlledGameMode cgm : ControlledGameMode.values())
					if (otgm.get(getPlayerByName(args[0]))[cgm.getId()])
						message += cgm.getMessageColor() + cgm.getName() + "§r, ";
				
				sender.sendMessage(message.substring(0, (message.length() - 4 > 0 ? message.length() - 4 : message.length())));
			} catch (PlayerNotFoundException e) {
				send(sender, config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[0]).build());
			}
			return true;
		} else
			return false;
	}
	
	//--------------
	// Utilities
	//--------------
	public void checkForUpdates(boolean update) {
		log("Checking for a newer version...");
		
		Updater.UpdateType updateType;
		if (update)
			updateType = Updater.UpdateType.DEFAULT;
		else
			updateType = Updater.UpdateType.NO_DOWNLOAD;
		
		Updater updater = new Updater(this, 71110, this.getFile(), updateType, true);
		
		switch(updater.getResult()) {
		case NO_UPDATE:
			log("No update was found. §2GMC " + this.getDescription().getVersion() + " §7is up to date.");
			break;
		case SUCCESS:
			log("The newest version §a" + updater.getLatestName() + " §7has been downloaded and will be loaded the next time the server restarts/reloads.");
			break;
		case UPDATE_AVAILABLE:
			log("There is a newer version available: §2" + updater.getLatestName() + "§7, but since");
			log("auto-update is §4disabled§7, nothing was downloaded.");
			break;
		case DISABLED:
			log("Auto-update is §cdisabled§7.");
			break;
		default:
			log("Something went wrong while updating!", MessageColor.ERROR);
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Player getPlayerByName(String name) throws PlayerNotFoundException {
		Player p = Bukkit.getServer().getPlayer(name);
		if (p != null) return p;
		else throw new PlayerNotFoundException("Player not found: '" + name + "'");
	}

	public static void log(String log) {
		log(log, MessageColor.INFO);
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
		
		if (cs instanceof Player) cs.sendMessage(message);
		else log(message);
	}
	
	public static void broadcast(String message) {
		message = replaceColorCodes(message);
		Bukkit.broadcastMessage(pre + (message != null ? message : "null"));
	}

	public static String replaceColorCodes(String message) {
		message = message.replaceAll("&([0-9a-f])", "§$1");
		message = message.replaceAll("&([k-o])", "§$1");
		message = message.replaceAll("&r", "§r");
		return message;
	}
}
