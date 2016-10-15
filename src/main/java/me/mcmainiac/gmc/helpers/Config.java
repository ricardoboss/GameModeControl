package me.mcmainiac.gmc.helpers;

import me.mcmainiac.gmc.Main;
import me.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import me.mcmainiac.gmc.utils.CGM;
import me.mcmainiac.gmc.utils.CGM.ControlledGameMode;
import me.mcmainiac.gmc.utils.MessageColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.security.InvalidParameterException;

public class Config {
	private JavaPlugin plugin;
	private FileConfiguration config;

	public Config(JavaPlugin plugin) throws IOException {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
		this.config = plugin.getConfig();
		this.config.options().copyDefaults(true);
		if (config.getBoolean("options.force-gamemode.enable"))
			try {
				ControlledGameMode cgm = CGM.getCGMByIdOrName(config.getString("options.force-gamemode.mode"));
				this.plugin.getServer().setDefaultGameMode(cgm.getGamemode());
				Main.log("Forcing gamemode " + cgm.getMessageColor() + cgm.getName() + "\u00A7r on player join");
			} catch (InvalidParameterException | GameModeNotFoundException e) {
				Main.log("You specified a wrong parameter for 'options.force-gamemode.mode'!", MessageColor.ERROR);
				Main.log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);
				config.set("options.force-gamemode.mode", 0);
				this.plugin.saveConfig();
				this.config = this.plugin.getConfig();
			}
	}

	void reload() {
		this.plugin.reloadConfig();
		this.config = this.plugin.getConfig();
	}

	Object get(String path) {
		return config.get(path);
	}

	public String getString(String path) {
		return config.getString(path);
	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	void set(String string, Object o) {
		config.set(string, o);
	}
}
