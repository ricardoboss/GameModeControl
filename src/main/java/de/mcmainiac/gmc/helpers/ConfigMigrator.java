package de.mcmainiac.gmc.helpers;

import org.bukkit.configuration.file.FileConfiguration;

class ConfigMigrator {
    static final int VERSION = 2;

    static boolean migrate(int currentVersion, FileConfiguration configuration) {
        // Run any migrations needed for new versions
        if (currentVersion < 2) {
            // version 2 changes 'mcstats' to 'bstats'

            // rename 'mcstats' to 'bstats'
            renameKey(configuration, "options.mcstats", "options.bstats");
        }

        // for future version: if (currentVersion < 3) { ... } ...

        // update the version
        configuration.set("version", VERSION);

        return true;
    }

    @SuppressWarnings("SameParameterValue")
    private static void renameKey(FileConfiguration configuration, String oldPath, String newPath) {
        var prevValue = configuration.getBoolean(oldPath);

        configuration.set(oldPath, null);
        configuration.set(newPath, prevValue);
    }
}
