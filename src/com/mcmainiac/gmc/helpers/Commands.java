package com.mcmainiac.gmc.helpers;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.ImmutableMap;
import com.mcmainiac.gmc.Main;
import com.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import com.mcmainiac.gmc.excpetions.PlayerNotFoundException;
import com.mcmainiac.gmc.utils.CGM;
import com.mcmainiac.gmc.utils.CGM.ControlledGameMode;
import com.mcmainiac.gmc.utils.MessageColor;

public class Commands implements Listener {
	private static HashMap<Player, Boolean[]> otgm = new HashMap<Player, Boolean[]>(); // A map to save, which players are able to change their game mode
	private static List<resetGameMode> resetgmTasks = new ArrayList<resetGameMode>();
	private static Main plugin = null;
	private static Commands instance = null;
	
	public static void setPlugin(Main main) {
		Commands.plugin = main;
	}

	public static void resetPlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) { // if the server has been reloaded, this loop adds all players to the change-gm-one-time-map
			otgm.put(p, new Boolean[]{false, false, false, false}); // default is false for every game mode
		}
		
		List<resetGameMode> _resetgmTasks = Commands.resetgmTasks;
		for (int i = 0; i < _resetgmTasks.size(); i++) {
			_resetgmTasks.get(i).run(); // execute the task before removing it
			Commands.resetgmTasks.remove(i); // remove the task from the list
		}
	}
	
	public static Listener getInstance() {
		if (Commands.instance == null) Commands.instance = new Commands();
		return instance;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		try {
			if (Main.config.getBoolean("options.force-gamemode.enable")) {
				if (e.getPlayer().hasPermission("options.force-gamemode")) {
					e.getPlayer().setGameMode(CGM.getGamemodeByIdOrName(Main.config.get("options.force-gamemode.mode")));
				} else
					Main.log("Didn't change " + e.getPlayer().getName() + "'s gamemode");
			}
		} catch(GameModeNotFoundException gme) {
			Main.log("Error: please check your config at 'options.force-gamemode.mode'", MessageColor.ERROR);
			gme.printStackTrace();
		}
		otgm.put(e.getPlayer(), new Boolean[]{false, false, false, false}); // add the player to the change-gm-one-time-map
		Main.log("Player " + e.getPlayer().getName() + " has gamemode " + e.getPlayer().getGameMode().toString().toUpperCase());
	}
	
	//--------------
	// Commands
	//--------------
	public static boolean Gamemode(CommandSender sender, String[] args)
			throws InvalidParameterException, GameModeNotFoundException {
		ControlledGameMode cgm;
		try {
			cgm = CGM.getCGMByIdOrName(args[0]);
		} catch (GameModeNotFoundException gme) {
			Main.send(sender, Main.config.getString("Other.gamemode not found"), ImmutableMap.<String, String>builder().put("$gm", args[0].toUpperCase()).build());
			return false;
		}

		if (sender instanceof Player)
			if (args[1] == null) // check if there is a player specified
				if (sender.hasPermission(Permissions.get(args[args.length-1] + ".self")) || // Either you have permission to set your own game mode
						sender.hasPermission(Permissions.get(args[args.length-1])) || // or you have the global permission to set this game mode.
						otgm.get(sender)[cgm.getId()]) { // or the player has been allowed to change his/her game mode via the /gmonce command
					CGM.set((Player)sender, cgm);
					otgm.put((Player) sender, new Boolean[]{false, false, false, false}); // reset the one-time-gm-map for this player
				}
				else
					Main.send((Player)sender, Main.config.getString("Other.no permission")); // Oops you don't have permission to do that
			else
				try {
					if (sender.hasPermission(Permissions.get(args[args.length-1] + ".others")) || // the same over here, except that you 
						sender.hasPermission(Permissions.get(args[args.length-1])))
							CGM.set(Main.getPlayerByName(args[1]), (Player)sender, cgm);
					else
						Main.send(sender, Main.config.getString("Other.no permission"));
				} catch (PlayerNotFoundException e) {
					Main.send(sender, Main.config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[1]).build());
				}
		else
			if (args[1] == null) return false; // we know it's the console or a command block here, so there MUST be a player specified
			else
				try {
					CGM.set(Main.getPlayerByName(args[1]), sender, cgm); // so just change the game mode (console has god mode activated)
				} catch (PlayerNotFoundException e) {
					Main.send(sender, Main.config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[1]).build());
				}
		return true;
	}
	
	public static boolean Reload(CommandSender sender) {
		if (!sender.hasPermission(Permissions.get("gmr"))) { Main.send(sender, Main.config.getString("Other.no permission")); return true; }
		Main.log("Reloading Config...");
		Main.log("---------");
		
		Main.config.reload();
		
		// notify new settings
		if (Main.config.getBoolean("options.auto-update")) {
			Main.log("Auto-update enabled!");
			plugin.checkForUpdates(false);
		} else
			Main.log("Auto-update disabled!");
		Main.log("---------");
		
		if (Main.config.getBoolean("options.force-gamemode.enable")) {
			try {
				ControlledGameMode cgm = CGM.getCGMByIdOrName(Main.config.getString("options.force-gamemode.mode"));
				plugin.getServer().setDefaultGameMode(cgm.getGamemode());
				Main.log("Forcing gamemode " + cgm.getName() + " on player join");
			} catch (InvalidParameterException | GameModeNotFoundException e) {
				Main.log("You specified a wrong parameter for 'options.force-gamemode.mode'!", MessageColor.ERROR);
				Main.log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);
				Main.config.set("options.force-gamemode.mode", 0);
				plugin.saveConfig();
				plugin.reloadConfig();
			}
		} else
			Main.log("Forcing gamemode is disabled!");
		Main.log("---------");
		
		if (sender instanceof Player) Main.log("Config reloaded");
		sender.sendMessage(Main.pre + "Config reloaded");
		return true;
	}
	
	public static boolean Info(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.get("gmi"))) { Main.send(sender, Main.config.getString("Other.no permission")); return true; }
		sender.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
		sender.sendMessage("§aVersion§7: §f" + plugin.getDescription().getVersion());
		sender.sendMessage("§aAuthor§7: §fMCMainiac");
		sender.sendMessage("§aWebsite§7: §5§nhttp://bit.ly/MC-GMC");
		sender.sendMessage("§f-----------------------------");
		return true;
	}
	
	public static boolean Help(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.get("gmh"))) { Main.send(sender, Main.config.getString("Other.no permission")); return true; }
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
	
	public static boolean OneTimeGamemode(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.get("gmonce"))) { Main.send(sender, Main.config.getString("Other.no permission")); return true; }
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
				
				Player p = Main.getPlayerByName(args[0]);
				otgm.put(p, new Boolean[]{survival, creative, adventure, spectator});
				sender.sendMessage("§6" + args[0] + " §fhas the permission to change his/her game mode to:");
				String message = "";
				for (ControlledGameMode cgm : ControlledGameMode.values())
					if (otgm.get(Main.getPlayerByName(args[0]))[cgm.getId()])
						message += cgm.getMessageColor() + cgm.getName() + "§r, ";
				
				message = message.substring(0, (message.length() - 4 > 0 ? message.length() - 4 : message.length()));
				sender.sendMessage(message);
				p.sendMessage("§fYou may change you game mode to:");
				p.sendMessage(message);
			} catch (PlayerNotFoundException e) {
				Main.send(sender, Main.config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[0]).build());
			}
			return true;
		} else
			return false;
	}
	
	public static boolean TemporaryGamemode(CommandSender sender, String[] args) {
		if (!sender.hasPermission(Permissions.get("gmtemp"))) { Main.send(sender, Main.config.getString("Other.no permission")); return true; }
		if (args.length > 1) {
			try {
				Player p = Main.getPlayerByName(args[0]);
				GameMode oldgm = p.getGameMode();
				ControlledGameMode cgm = CGM.getCGMByIdOrName(args[1]);
				CGM.set(p, cgm);
				
				resetGameMode rgm = Commands.instance.new resetGameMode(p, oldgm);
				Commands.resetgmTasks.add(rgm);
				p.getServer().getScheduler().scheduleSyncDelayedTask(Commands.plugin, rgm, Long.parseLong(args[2])*20); // convert seconds to ticks (20 ticks = 1 second, if the server is running at normal speed (no lags))
				
				sender.sendMessage("§6" + p.getName() + "§f's gamemode mode is now " + cgm.getMessageColor() + cgm.getName() + " §ffor §a" + args[2] + " §fseconds");
			} catch (PlayerNotFoundException e) {
				Main.send(sender, Main.config.getString("Other.player not found"), ImmutableMap.<String, String>builder().put("$player", args[0]).build());
			} catch (GameModeNotFoundException e) {
				Main.send(sender, Main.config.getString("Other.gamemode not found"), ImmutableMap.<String, String>builder().put("$gm", args[0].toUpperCase()).build());
			}
			return true;	
		} else
			return false;
	}

	private class resetGameMode implements Runnable {
		private Player p;
		private GameMode oldgm;
		
		public resetGameMode(Player p, GameMode oldgm) {
			this.p = p;
			this.oldgm = oldgm;
		}
		
		@Override
		public void run() {
			try {
				CGM.set(p, CGM.getCGMByGamemode(oldgm)); // reset the old gamemode of the player
			} catch (GameModeNotFoundException e) { /* Ignore */ }
		}
	}
}
