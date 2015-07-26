package com.mcmainiac.gmc;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcmainiac.gmc.CGM.ControlledGameMode;
import com.mcmainiac.gmc.excpetions.GameModeNotFoundException;

public class Config {
	private JavaPlugin plugin;
	private FileConfiguration config; 
	
	public Config(JavaPlugin plugin) throws IOException {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
		this.config = plugin.getConfig();
		this.config.options().copyDefaults(true);
		if (config.getBoolean("force-gamemode.enable"))
			try {
				ControlledGameMode cgm = CGM.getCGMByIdOrName(config.getString("force-gamemode.mode"));
				this.plugin.getServer().setDefaultGameMode(cgm.getGamemode());
				Main.log("Forcing gamemode " + cgm.getMessageColor() + cgm.getName() + "§r on player join");
			} catch (InvalidParameterException | GameModeNotFoundException e) {
				Main.log("You specified a wrong parameter for 'force-gamemode.mode'!", MessageColor.ERROR);
				Main.log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);
				config.set("force-gamemode.mode", 0);
				this.plugin.saveConfig();
				this.config = this.plugin.getConfig();
			}
		Main.log("Config loaded");
	}
	
	public void reload() {
		this.plugin.getServer().reload();
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
}
