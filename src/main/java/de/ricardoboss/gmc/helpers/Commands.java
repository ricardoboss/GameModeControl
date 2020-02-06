package de.ricardoboss.gmc.helpers;

import com.google.common.collect.ImmutableMap;
import de.ricardoboss.gmc.Main;
import de.ricardoboss.gmc.excpetions.GameModeNotFoundException;
import de.ricardoboss.gmc.excpetions.PlayerNotFoundException;
import de.ricardoboss.gmc.tasks.ResetGameModeTask;
import de.ricardoboss.gmc.utils.CGM;
import de.ricardoboss.gmc.utils.MessageColor;
import de.ricardoboss.gmc.utils.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Commands implements Listener {
    private static final HashMap<Player, Boolean[]> otgm = new HashMap<>(); // A map to save, which players are able to change their game mode
    private static final List<ResetGameModeTask> resetGmTasks = new ArrayList<>();
    private static Main plugin = null;
    private static Commands instance = null;

    // prevent instantiation
    private Commands() {
    }

    public static void setPlugin(Main main) {
        Commands.plugin = main;
    }

    public static void resetPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) { // if the server has been reloaded, this loop adds all players to the change-gm-one-time-map
            otgm.put(p, new Boolean[]{false, false, false, false}); // default is false for every game mode
        }

        var _resetGmTasks = Commands.resetGmTasks.toArray(new ResetGameModeTask[0]);

        // execute the task before removing it
        for (ResetGameModeTask resetGmTask : _resetGmTasks) {
            resetGmTask.run();
        }

        // clear list
        Commands.resetGmTasks.clear();
    }

    public static Commands getInstance() {
        if (Commands.instance == null)
            Commands.instance = new Commands(); // lazy initializer

        return instance;
    }

    //--------------
    // Commands
    //--------------
    public static boolean Gamemode(CommandSender sender, String[] args)
            throws InvalidParameterException {
        CGM.ControlledGameMode cgm;
        try {
            cgm = CGM.getControlledGamemodeByIdOrName(args[0]);
        } catch (GameModeNotFoundException gme) {
            Main.send(
                    sender,
                    Main.config.getString(Config.StringPaths.OTHER_GAMEMODE_NOT_FOUND),
                    ImmutableMap.<String, String>builder()
                            .put("\u0024gm", args[0])
                            .put("\u0024message", gme.getMessage())
                            .build()
            );
            return false;
        }

        if (sender instanceof Player)
            if (args[1] == null) // check if there is a player specified
                if (    // Either you have permission to set your own game mode
                        sender.hasPermission(Permissions.get(Permissions.Perm.fromString(args[args.length - 1] + ".self"))) ||

                                // or you have the global permission to set this game mode.
                                sender.hasPermission(Permissions.get(Permissions.Perm.fromString(args[args.length - 1]))) ||

                                // or the player has been allowed to change their game mode via the /gmonce command
                                otgm.get(sender)[cgm.getId()]) {
                    // set the game mode of the player
                    CGM.set((Player) sender, cgm);

                    // reset the one-time-gm-map for this player
                    otgm.put((Player) sender, new Boolean[]{false, false, false, false});
                } else // Oops you don't have permission to do that
                    Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            else
                try {
                    if (sender.hasPermission(Permissions.get(Permissions.Perm.fromString(args[args.length - 1] + ".others"))) ||
                            sender.hasPermission(Permissions.get(Permissions.Perm.fromString(args[args.length - 1]))))
                        CGM.set(Main.getPlayerByName(args[1]), (Player) sender, cgm);
                    else
                        Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
                } catch (PlayerNotFoundException e) {
                    Main.send(
                            sender,
                            Main.config.getString(Config.StringPaths.OTHER_PLAYER_NOT_FOUND),
                            ImmutableMap.<String, String>builder().put("\u0024player", args[1]).build()
                    );
                }
        else
            // we know it's the console or a command block here, so there MUST be a player specified
            if (args[1] == null)
                return false;
            else
                try {
                    // so just change the game mode (console has god mode activated \o/ )
                    CGM.set(Main.getPlayerByName(args[1]), sender, cgm);
                } catch (PlayerNotFoundException e) {
                    Main.send(
                            sender,
                            Main.config.getString(Config.StringPaths.OTHER_PLAYER_NOT_FOUND),
                            ImmutableMap.<String, String>builder().put("\u0024player", args[1]).build()
                    );
                }
        return true;
    }

    public static void Reload(CommandSender sender) {
        if (!sender.hasPermission(Permissions.get(Permissions.Perm.GMR))) {
            Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            return;
        }

        Main.log("Reloading Config...");

        Main.config.reload();

        if (Main.config.getBoolean(Config.BooleanPaths.OPTIONS_DEBUG)) {
            Main.debug = true;
            Main.log("Debug mode is enabled! Hi, dev!");
        } else
            Main.log("Debug mode is disabled!");

        if (Main.config.getBoolean(Config.BooleanPaths.OPTIONS_AUTO_UPDATE)) {
            Main.log("Auto-update is enabled!");
            plugin.checkForUpdates(false);
        } else
            Main.log("Auto-update is disabled!");

        if (Main.config.getBoolean(Config.BooleanPaths.OPTIONS_FORCEGM)) {
            try {
                CGM.ControlledGameMode cgm = CGM.getControlledGamemodeByIdOrName(Main.config.getString(Config.StringPaths.OPTIONS_FORCEGM_MODE));
                plugin.getServer().setDefaultGameMode(cgm.getGamemode());

                Main.log("Forcing gamemode " + CGM.getMessageColor(cgm) + cgm.getName() + MessageFormat.RESET +
                        " on player join");
            } catch (InvalidParameterException | GameModeNotFoundException e) {
                Main.log(
                        "You specified a wrong parameter for '" + Config.StringPaths.OPTIONS_FORCEGM_MODE.getPath() + "'!",
                        MessageColor.ERROR
                );

                Main.log(
                        "Using the default gamemode " + CGM.ControlledGameMode.SURVIVAL.getMessageFormatted(),
                        MessageColor.ERROR
                );

                Main.config.setString(
                        Config.StringPaths.OPTIONS_FORCEGM_MODE,
                        CGM.ControlledGameMode.SURVIVAL.toString().toLowerCase()
                );

                plugin.saveConfig();
                plugin.reloadConfig();
            }
        } else
            Main.log("Forcing gamemode is disabled!");

        if (sender instanceof Player)
            sender.sendMessage(Main.MESSAGE_PREFIX + "Config reloaded");

        Main.log("Config reloaded");
    }

    public static void Info(CommandSender sender) {
        if (!sender.hasPermission(Permissions.get(Permissions.Perm.GMI))) {
            Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            return;
        }

        sender.sendMessage(MessageColor.WHITE + "----- " + MessageColor.GRAY + " [" + MessageColor.DARK_GREEN +
                " Game" + MessageColor.GREEN + " Mode" + MessageColor.WHITE + " Control" + MessageColor.GRAY + " ] " +
                MessageColor.WHITE + " -----");
        sender.sendMessage(MessageColor.GREEN + " Version" + MessageColor.GRAY + " : " + MessageColor.WHITE + " " +
                plugin.getDescription().getVersion());
        sender.sendMessage(MessageColor.GREEN + " Author" + MessageColor.GRAY + " : " + MessageColor.WHITE + " " +
                String.join(",", plugin.getDescription().getAuthors()));
        sender.sendMessage(MessageColor.GREEN + " Website" + MessageColor.GRAY + " : " + MessageColor.DARK_PURPLE +
                " " + MessageFormat.UNDERLINE + " " + plugin.getDescription().getWebsite());
        sender.sendMessage(MessageColor.WHITE + "-----------------------------");
    }

    public static boolean Help(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.get(Permissions.Perm.GMH))) {
            Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            return true;
        }

        if (args.length == 0)
            Help.Show(sender, 1);
        else if (args.length == 1)
            try {
                Help.Show(sender, Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                Help.Command(sender, args[0]);
            }
        else
            return false;
        return true;
    }

    public static boolean OneTimeGamemode(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.get(Permissions.Perm.GMONCE))) {
            Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            return true;
        }

        if (args.length > 0) {
            try {
                boolean survival = false,
                        creative = false,
                        adventure = false,
                        spectator = false; // default for all game modes is false

                // go through all parameters and check, which game modes have been enabled by the operator
                try {
                    for (var i = 1; i < args.length; i++) {
                        var cgm = CGM.getGamemodeByIdOrName(args[i]);
                        switch (cgm) {
                            case CREATIVE:
                                creative = true;
                                break;
                            case SURVIVAL:
                                survival = true;
                                break;
                            case ADVENTURE:
                                adventure = true;
                                break;
                            case SPECTATOR:
                                spectator = true;
                                break;
                        }
                    }
                } catch (GameModeNotFoundException gme) {
                    Main.send(
                            sender,
                            Main.config.getString(Config.StringPaths.OTHER_OTGM_INVALID),
                            ImmutableMap.<String, String>builder()
                                    .put("\u0024gm", gme.getGamemode())
                                    .put("\u0024message", gme.getMessage())
                                    .build()
                    );

                    return true;
                }

                if (!survival && !creative && !adventure && !spectator) {
                    Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_OTGM_ERROR));

                    return true;
                }

                var p = Main.getPlayerByName(args[0]);
                otgm.put(p, new Boolean[]{survival, creative, adventure, spectator});

                Main.send(
                        sender,
                        Main.config.getString(Config.StringPaths.OTHER_OTGM_MESSAGE),
                        ImmutableMap.<String, String>builder().put("\u0024player", args[0]).build()
                );

                StringBuilder message = new StringBuilder();
                for (CGM.ControlledGameMode cgm : CGM.ControlledGameMode.values())
                    if (otgm.get(Main.getPlayerByName(args[0]))[cgm.getId()])
                        message.append(cgm.getMessageFormatted())
                                .append(MessageFormat.RESET)
                                .append(", ");

                if (message.length() - 4 > 0)
                    message = new StringBuilder(message.subSequence(0, message.length() - 4));

                sender.sendMessage(message.toString());

                Main.send(p, Main.config.getString(Config.StringPaths.OTHER_OTGM_ALLOWED));
                p.sendMessage(message.toString());
            } catch (PlayerNotFoundException e) {
                Main.send(
                        sender,
                        Main.config.getString(Config.StringPaths.OTHER_PLAYER_NOT_FOUND),
                        ImmutableMap.<String, String>builder().put("\u0024player", args[0]).build()
                );
            }

            return true;
        } else
            return false;
    }

    public static boolean TemporaryGamemode(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.get(Permissions.Perm.GMTEMP))) {
            Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_NO_PERMISSION));
            return true;
        }

        if (args.length > 2) {
            try {
                var p = Main.getPlayerByName(args[0]);
                var oldGm = p.getGameMode();
                var cgm = CGM.getControlledGamemodeByIdOrName(args[1]);

                CGM.set(p, Bukkit.getConsoleSender(), cgm);

                var rgm = new ResetGameModeTask(p, oldGm);
                Commands.resetGmTasks.add(rgm);

                // convert seconds to ticks (20 ticks = 1 second, if the server is running at normal speed (no lags))
                Bukkit.getScheduler().runTaskLater(Commands.plugin, rgm, Long.parseLong(args[2]) * 20);

                var messageArgs = ImmutableMap.<String, String>builder()
                        .put("\u0024player", p.getName())
                        .put("\u0024gm", cgm.getMessageFormatted())
                        .put("\u0024time", args[2])
                        .build();

                Main.send(sender, Main.config.getString(Config.StringPaths.OTHER_GMTEMP_TO), messageArgs);
                Main.send(p, Main.config.getString(Config.StringPaths.OTHER_GMTEMP_FROM), messageArgs);
            } catch (PlayerNotFoundException e) {
                Main.send(
                        sender,
                        Main.config.getString(Config.StringPaths.OTHER_PLAYER_NOT_FOUND),
                        ImmutableMap.<String, String>builder().put("\u0024player", args[0]).build()
                );
            } catch (GameModeNotFoundException e) {
                Main.send(
                        sender,
                        Main.config.getString(Config.StringPaths.OTHER_GAMEMODE_NOT_FOUND),
                        ImmutableMap.<String, String>builder()
                                .put("\u0024gm", e.getGamemode())
                                .put("\u0024message", e.getMessage())
                                .build()
                );
            }

            return true;
        } else
            return false;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        try {
            if (Main.config.getBoolean(Config.BooleanPaths.OPTIONS_FORCEGM)) {
                if (e.getPlayer().hasPermission(Permissions.get(Permissions.Perm.FORCEGM))) {
                    e.getPlayer().setGameMode(CGM.getGamemodeByIdOrName(Main.config.getString(Config.StringPaths.OPTIONS_FORCEGM_MODE)));
                } else if (Main.debug)
                    Main.log("Didn't change " + e.getPlayer().getName() + "'s gamemode");
            }
        } catch (GameModeNotFoundException gme) {
            Main.log("Error: please check your config at '" + Config.StringPaths.OPTIONS_FORCEGM_MODE.getPath() + "'", MessageColor.ERROR);

            if (Main.debug)
                gme.printStackTrace();
        }

        // add the player to the change-gm-once-time-map
        otgm.put(e.getPlayer(), new Boolean[]{false, false, false, false});

        if (Main.debug)
            Main.log("Player " + e.getPlayer().getName() + " has gamemode " + e.getPlayer().getGameMode().toString().toUpperCase());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        otgm.remove(e.getPlayer());

        if (Main.debug)
            Main.log(e.getPlayer().getName() + " left with gamemode " + e.getPlayer().getGameMode().toString().toUpperCase());
    }
}
