package de.ricardoboss.gmc;

import com.google.common.collect.ImmutableMap;
import de.ricardoboss.gmc.excpetions.PlayerNotFoundException;
import de.ricardoboss.gmc.helpers.Commands;
import de.ricardoboss.gmc.helpers.Config;
import de.ricardoboss.gmc.tasks.UpdaterTask;
import de.ricardoboss.gmc.utils.MessageColor;
import de.ricardoboss.gmc.utils.MessageFormat;
import de.ricardoboss.gmc.utils.MetricsCollector;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.InvalidParameterException;

/**
 * <h1>GameModeControl V1.6</h1><br>
 *
 * <p>Helps you and your admins to control
 * game modes faster and more accurate
 * than ever before.</p>
 * <p>
 * {@link} <a href="http://bit.ly/MC-GMC">bit.ly/MC-GMC</a>
 *
 * @author Mizzle0
 */
public class Main extends JavaPlugin {
    public static final String MESSAGE_PREFIX = "\u00A77[GMC] \u00A7r";
    public static final int PLUGIN_ID = 71110;
    private static final CommandSender console = Bukkit.getConsoleSender();
    public static Config config;
    public static boolean debug = false;

    public static Player getPlayerByName(String name) throws PlayerNotFoundException {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getName().equals(name)) return p;

        throw new PlayerNotFoundException(name, "Player not found: '" + name + "'");
    }

    public static void log(String log) {
        log(log, MessageColor.INFO);
    }

    public static void log(String log, MessageColor color) {
        console.sendMessage(MESSAGE_PREFIX + color.toString() + log);
    }

    public static void send(CommandSender cs, String message) {
        if (message.length() == 0)
            return;

        message = replaceColorCodes(message);
        if (cs instanceof Player) cs.sendMessage(message);
        else log(message);
    }

    public static void send(CommandSender cs, String message, ImmutableMap<String, String> context) {
        if (message.length() == 0)
            return;

        message = replaceColorCodes(message);

        for (String target : context.keySet())
            if (message.contains(target)) {
                String first = message.substring(0, message.indexOf(target));
                String last = message.substring(message.indexOf(target) + target.length());
                message = first + context.get(target) + last;
            }

        send(cs, message);
    }

    private static String replaceColorCodes(String message) {
        message = message.replaceAll("&([0-9a-f])", "\u00A7$1");
        message = message.replaceAll("&([k-o])", "\u00A7$1");
        message = message.replaceAll("&r", MessageFormat.RESET.toString());
        return message;
    }

    //--------------
    // Plugin commands
    //--------------
    @Override
    public void onEnable() {
        Main.config = new Config(this);

        if (!Main.config.loaded) {
            if (debug)
                log("Config failed to load. Aborting startup.");

            return;
        }

        if (debug)
            log("Registering events");
        Bukkit.getPluginManager().registerEvents(Commands.getInstance(), this);

        if (debug)
            log("Initializing commands and resetting players");
        Commands.setPlugin(this);
        Commands.resetPlayers();

        // Auto-Updater
        checkForUpdates(config.getBoolean(Config.BooleanPaths.OPTIONS_AUTO_UPDATE));

        // plugin metrics (https://bstats.org/)
        if (config.getBoolean(Config.BooleanPaths.OPTIONS_BSTATS)) {
            if (debug)
                log("Initializing metrics (bstats.org)");

            // disable relocate check since it is imported via maven
            System.setProperty("bstats.relocatecheck", "false");

            // enable metrics by creating a new instance
            Metrics metrics = new Metrics(this, PLUGIN_ID);
            metrics.addCustomChart(
                    new Metrics.AdvancedPie(
                            "most_used_gamemode",
                            MetricsCollector.MOST_USED_GAMEMODE
                    )
            );
        }
    }

    @Override
    public void onDisable() {
        Commands.resetPlayers();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        try {
            switch (command.getName().toLowerCase()) {
                case "gamemode":
                case "gm":
                    return (args.length > 0 && Commands.Gamemode(sender, new String[]{args[0], (args.length > 1 ? args[1] : null), "gamemode"}));
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

                case "gmh":
                    return Commands.Help(sender, args);
                case "gmi":
                    Commands.Info(sender);
                    return true;
                case "gmr":
                    Commands.Reload(sender);
                    return true;

                default:
                    return false;
            }
        } catch (InvalidParameterException ipe) {
            log("An invalid parameter was given!", MessageColor.ERROR);
            log("Please check your config!", MessageColor.ERROR);
        }

        return true;
    }

    //--------------
    // Utilities
    //--------------
    public void checkForUpdates(boolean update) {
        UpdaterTask ut = new UpdaterTask(this, this.getFile(), update);
        Bukkit.getScheduler().runTaskAsynchronously(this, ut);
    }
}
