package de.mcmainiac.gmc.helpers;

import de.mcmainiac.gmc.Main;
import org.bukkit.configuration.file.FileConfiguration;

class ConfigMigrator {
    static final int VERSION = 2;

    static boolean migrate(int currentVersion, FileConfiguration configuration) {
        // Run any migrations needed for new versions
        if (currentVersion < 2) {
            // version 2 changes 'mcstats' to 'bstats'

            // rename 'mcstats' to 'bstats'
            var success = renameKey(configuration, "options.mcstats", "options.bstats");
            if (!success)
                return false;
        }

        // for future version: if (currentVersion < 3) { ... } ...

        // update the version
        configuration.set("version", VERSION);

        return true;
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean renameKey(FileConfiguration configuration, String oldPath, String newPath) {
        var prevValue = configuration.get(oldPath);
        if (prevValue == null) {
            if (Main.debug)
                Main.log("Cannot rename key: no value is present for key " + oldPath);

            return false;
        }

        configuration.set(oldPath, null);
        configuration.set(newPath, prevValue);

        return (configuration.get(oldPath) == null) && (configuration.get(newPath).equals(prevValue));
    }
}
