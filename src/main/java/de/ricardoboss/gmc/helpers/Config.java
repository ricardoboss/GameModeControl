package de.ricardoboss.gmc.helpers;

import de.ricardoboss.gmc.Main;
import de.ricardoboss.gmc.excpetions.GameModeNotFoundException;
import de.ricardoboss.gmc.utils.CGM;
import de.ricardoboss.gmc.utils.CGM.ControlledGameMode;
import de.ricardoboss.gmc.utils.MessageColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.security.InvalidParameterException;

public class Config {
    public final boolean loaded;

    private final Main plugin;
    private FileConfiguration config;

    public Config(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        // tell config to copy default values
        config.options().copyDefaults(true);

        // save config if it doesn't exist
        plugin.saveDefaultConfig();

        // check for debug mode
        if (getBoolean(BooleanPaths.OPTIONS_DEBUG)) {
            Main.debug = true;
            Main.log("Debug mode enabled! Hi dev!");
        }

        // determine config version, migrate if necessary
        var versionString = getString(StringPaths.VERSION, null);
        int configVersion;

        // if no version is stored in the config...
        if (versionString == null || versionString.length() == 0) {
            // ...assume the config was just created and save it
            save();

            // store latest version
            configVersion = ConfigMigrator.VERSION;
        } else
            configVersion = Integer.parseInt(versionString);

        if (Main.debug)
            Main.log("Config version: " + configVersion);

        if (configVersion < ConfigMigrator.VERSION) {
            Main.log("Migrating config.yml to the latest version...");

            // migrate the old config
            boolean success;

            try {
                success = ConfigMigrator.migrate(configVersion, config);
            } catch (Exception e) {
                if (Main.debug)
                    e.printStackTrace();

                success = false;
            }

            if (!success) {
                Main.log("Config was not successfully migrated!", MessageColor.ERROR);
                Main.log("Please update your config.yml manually!", MessageColor.ERROR);
                Main.log("GMC gets disabled!", MessageColor.WARNING);

                plugin.getPluginLoader().disablePlugin(plugin);

                loaded = false;

                return;
            }

            Main.log("Migration successful!");
        }

        // check the 'forcegm' setting
        if (getBoolean(BooleanPaths.OPTIONS_FORCEGM))
            try {
                ControlledGameMode cgm = CGM.getControlledGamemodeByIdOrName(getString(StringPaths.OPTIONS_FORCEGM_MODE));
                this.plugin.getServer().setDefaultGameMode(cgm.getGamemode());
                Main.log("Forcing gamemode " + cgm.getMessageColor() + cgm.getName() + MessageColor.WHITE + " on player join");
            } catch (InvalidParameterException | GameModeNotFoundException e) {
                Main.log("You specified a wrong parameter for '" + StringPaths.OPTIONS_FORCEGM_MODE.getPath() + "'!", MessageColor.ERROR);
                Main.log("Using the default gamemode " + CGM.getMessageColor(ControlledGameMode.SURVIVAL) + "SURVIVAL", MessageColor.ERROR);

                setString(StringPaths.OPTIONS_FORCEGM_MODE, ControlledGameMode.SURVIVAL.toString().toLowerCase());
                save();
            }

        loaded = true;
    }

    void reload() {
        if (Main.debug)
            Main.log("Reloading config file...");

        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    void save() {
        if (Main.debug)
            Main.log("Saving config file...");

        plugin.saveConfig();
        config = plugin.getConfig();
    }

    @SuppressWarnings("WeakerAccess")
    public String getString(StringPaths path, String def) {
        return config.getString(path.getPath(), def);
    }

    public String getString(StringPaths path) {
        return config.getString(path.getPath());
    }

    public boolean getBoolean(BooleanPaths path) {
        return config.getBoolean(path.getPath());
    }

    @SuppressWarnings("SameParameterValue")
    void setString(StringPaths path, String value) {
        config.set(path.getPath(), value);
    }

    @SuppressWarnings("unused")
    public enum StringPaths implements Paths {
        OPTIONS_FORCEGM_MODE("options.force-gamemode.mode"),
        SURVIVAL_SELF("survival.self"),
        SURVIVAL_FROM("survival.from"),
        SURVIVAL_TO("survival.to"),
        CREATIVE_SELF("creative.self"),
        CREATIVE_FROM("creative.from"),
        CREATIVE_TO("creative.to"),
        ADVENTURE_SELF("adventure.self"),
        ADVENTURE_FROM("adventure.from"),
        ADVENTURE_TO("adventure.to"),
        SPECTATOR_SELF("spectator.self"),
        SPECTATOR_FROM("spectator.from"),
        SPECTATOR_TO("spectator.to"),
        OTHER_PLAYER_NOT_FOUND("other.player not found"),
        OTHER_GAMEMODE_NOT_FOUND("other.gamemode not found"),
        OTHER_NO_PERMISSION("other.no permission"),
        OTHER_OTGM_ERROR("other.one time gamemode.error"),
        OTHER_OTGM_MESSAGE("other.one time gamemode.message"),
        OTHER_OTGM_ALLOWED("other.one time gamemode.allowed"),
        OTHER_GMTEMP_TO("other.gmtemp.to"),
        OTHER_GMTEMP_FROM("other.gmtemp.from"),
        VERSION("version");

        private final String path;

        StringPaths(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum BooleanPaths implements Paths {
        OPTIONS_FORCEGM("options.force-gamemode.enable"),
        OPTIONS_AUTO_UPDATE("options.auto-update"),
        OPTIONS_BSTATS("options.bstats"),
        OPTIONS_DEBUG("options.debug");

        private final String path;

        BooleanPaths(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public interface Paths {
        static Paths fromString(String path) throws IllegalArgumentException {
            path = path.toLowerCase();
            for (var strPath : StringPaths.values())
                if (strPath.getPath().equals(path))
                    return strPath;

            for (var boolPath : BooleanPaths.values())
                if (boolPath.getPath().equals(path))
                    return boolPath;

            throw new IllegalArgumentException("Could not find path type for: '" + path + "'!");
        }
    }
}
