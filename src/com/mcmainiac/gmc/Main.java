package com.mcmainiac.gmc;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;
import com.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import com.mcmainiac.gmc.excpetions.PlayerNotFoundException;
import com.mcmainiac.gmc.helpers.Commands;
import com.mcmainiac.gmc.helpers.Config;
import com.mcmainiac.gmc.helpers.Metrics;
import com.mcmainiac.gmc.helpers.Updater;
import com.mcmainiac.gmc.utils.MessageColor;

/**
 * GameModeControl V1.3.7<br>
 * 
 * <p>Helps you and your admins to control
 * game modes faster and more accurate
 * than ever before.</p>
 * 
 * {@link} <a href="http://bit.ly/MC-GMC">bit.ly/MC-GMC</a>
 * @author MCMainiac
 */
public class Main extends JavaPlugin {
	public static final String pre = "§7[GMC] §r";
	public static HashMap<String, Permission> permissions = new HashMap<String, Permission>(); // A map for all the permissions (fast access to dynamic permissions)
	public static Config config;

	private static CommandSender console = Bukkit.getConsoleSender();

	//--------------
	// Plugin commands
	//--------------
	@Override
	public void onEnable() {
		try {
			log("Initializing GMC v" + this.getDescription().getVersion());
			Main.config = new Config(this);
			Bukkit.getPluginManager().registerEvents(Commands.getInstance(), this);
			
			// Permissions
			permissions.put("gmi", new Permission("gmc.gmi", PermissionDefault.TRUE));
			permissions.put("gmh", new Permission("gmc.gmh", PermissionDefault.TRUE));
			permissions.put("forcegm", new Permission("gmc.forcegm", PermissionDefault.TRUE));
			permissions.put("gmr", new Permission("gmc.gmr", PermissionDefault.OP));
			permissions.put("gmonce", new Permission("gmc.gmonce", PermissionDefault.OP));
			permissions.put("gmtemp", new Permission("gmc.gmtemp", PermissionDefault.OP));
			
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

			Commands.setPlugin(this);
			Commands.resetPlayers();
			
			// Auto-Updater
			checkForUpdates(config.getBoolean("options.auto-update"));
			
			// plugin metrics (mcstats.org)
			if (config.getBoolean("options.mcstats")) {
				try {
					Metrics metrics = new Metrics(this);
					metrics.start();
				} catch (IOException e) {
					log("Failed to enable plugin metrics!", MessageColor.ERROR);
				}				
			}
		} catch(IOException ioe) {
			log("GMC crashed while initializing!", MessageColor.ERROR);
			ioe.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		Commands.resetPlayers();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		try {
			switch (commandLabel.toLowerCase()) {
			case "gamemode":
			case "gm":
				return (args.length > 0 ? Commands.Gamemode(sender, new String[]{args[0], (args.length > 1 ? args[1] : null), "gamemode"}) : false);
			case "gm0":
			case "survival":
				return Commands.Gamemode(sender, new String[]{"0", (args.length > 0 ? args[0] : null), "survival"});
			case "gm1":
			case "creative":
				return Commands.Gamemode(sender, new String[]{"1", (args.length > 0 ? args[0] : null), "creative"});
			case "gm2":
			case "adventure":
				return Commands.Gamemode(sender, new String[]{"2", (args.length > 0 ? args[0] : null), "adventure"});
			case "gm3":
			case "spectator":
				return Commands.Gamemode(sender, new String[]{"3", (args.length > 0 ? args[0] : null), "spectator"});
			
			case "gmonce":
				return Commands.OneTimeGamemode(sender, args);
			case "gmtemp":
				return Commands.TemporaryGamemode(sender, args);
			
			case "gmh": return Commands.Help(sender, args);
			case "gmi": return Commands.Info(sender, args);
			case "gmr": return Commands.Reload(sender);
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
	
	//--------------
	// Utilities
	//--------------
	public void checkForUpdates(boolean update) {
		Updater.UpdateType updateType;
		if (update)
			updateType = Updater.UpdateType.DEFAULT;
		else
			updateType = Updater.UpdateType.NO_DOWNLOAD;
		
		Updater updater = new Updater(this, 71110, this.getFile(), updateType, true);
		
		switch(updater.getResult()) {
		case NO_UPDATE:
			log("[Updater] No update was found (last version: " + updater.getLatestName() + ").");
			break;
		case SUCCESS:
			log("[Updater] The newest version " + updater.getLatestName() + " has been downloaded and will be");
			log("[Updater] loaded the next time the server restarts/reloads.");
			break;
		case UPDATE_AVAILABLE:
			log("[Updater] There is a newer version available: " + updater.getLatestName() + ", but since");
			log("[Updater] auto-update is disabled, nothing was downloaded.");
			break;
		case DISABLED: break;
		default:
			log("[Updater] Something went wrong while updating!", MessageColor.ERROR);
			break;
		}
	}
	
	public static Player getPlayerByName(String name) throws PlayerNotFoundException {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) return p;
			else broadcast(p.getName() + " =/= " + name);
		}
		throw new PlayerNotFoundException("Player not found: '" + name + "'");
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

	public static void broadcast(int i) {
		Main.broadcast(Integer.toString(i));
	}

	public static String replaceColorCodes(String message) {
		message = message.replaceAll("&([0-9a-f])", "§$1");
		message = message.replaceAll("&([k-o])", "§$1");
		message = message.replaceAll("&r", "§r");
		return message;
	}
}
