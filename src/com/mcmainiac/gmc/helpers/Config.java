package com.mcmainiac.gmc.helpers;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcmainiac.gmc.Main;
import com.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import com.mcmainiac.gmc.helpers.CGM.ControlledGameMode;

public class Config {
	private JavaPlugin plugin;
	private FileConfiguration config; 
	
	public Config(JavaPlugin plugin) throws IOException {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
		this.config = plugin.getConfig();
		this.config.options().copyDefaults(true);
		Main.log("Config loaded");
		if (config.getBoolean("options.force-gamemode.enable"))
			try {
				ControlledGameMode cgm = CGM.getCGMByIdOrName(config.getString("options.force-gamemode.mode"));
				this.plugin.getServer().setDefaultGameMode(cgm.getGamemode());
				Main.log("Forcing gamemode " + cgm.getMessageColor() + cgm.getName() + "§r on player join");
			} catch (InvalidParameterException | GameModeNotFoundException e) {
				Main.log("You specified a wrong parameter for 'options.force-gamemode.mode'!", MessageColor.ERROR);
				Main.log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);
				config.set("options.force-gamemode.mode", 0);
				this.plugin.saveConfig();
				this.config = this.plugin.getConfig();
			}
	}
	
	public void reload() {
		this.plugin.reloadConfig();
		this.config = this.plugin.getConfig();
	}
	
	public Object get(String path) {
		return config.get(path);
	}
	
	public String getString(String path) {
		return config.getString(path);
	}
	
	public int getInt(String path) {
		return config.getInt(path);
	}
	
	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public void set(String string, Object o) {
		config.set(string, o);
	}
}
