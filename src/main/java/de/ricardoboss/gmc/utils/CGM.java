package de.ricardoboss.gmc.utils;

import com.google.common.collect.ImmutableMap;
import de.ricardoboss.gmc.Main;
import de.ricardoboss.gmc.excpetions.GameModeNotFoundException;
import de.ricardoboss.gmc.helpers.Config;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.InvalidParameterException;

/**
 * Controlled Game Mode
 *
 * @author Mizzle0
 */
public class CGM {
    public static void set(Player p, ControlledGameMode cgm) {
        setActual(p, cgm);

        Main.send(
                p,
                Main.config.getString((Config.StringPaths) Config.Paths.fromString(cgm.getName() + ".self"))
        );

        Main.log("Set " + p.getName() + "'s game mode to " + cgm.getName(), MessageColor.INFO);
    }

    public static void set(Player p, CommandSender console, ControlledGameMode cgm) {
        setActual(p, cgm);

        Main.send(
                p,
                Main.config.getString((Config.StringPaths) Config.Paths.fromString(cgm.getName() + ".from")),
                ImmutableMap.<String, String>builder().put("\u0024sender", console.getName()).build()
        );

        Main.log("Console set " + p.getName() + "'s game mode to " + cgm.getName());
    }

    public static void set(Player p, Player sender, ControlledGameMode cgm) {
        setActual(p, cgm);

        Main.send(
                p,
                Main.config.getString((Config.StringPaths) Config.Paths.fromString(cgm.getName() + ".from")),
                ImmutableMap.<String, String>builder().put("\u0024sender", sender.getName()).build()
        );

        Main.send(
                sender,
                Main.config.getString((Config.StringPaths) Config.Paths.fromString(cgm.getName() + ".to")),
                ImmutableMap.<String, String>builder().put("\u0024player", p.getName()).build()
        );

        Main.log(sender.getName() + " set " + p.getName() + "'s game mode to " + cgm.getName());
    }

    private static void setActual(Player p, ControlledGameMode cgm) {
        p.setGameMode(cgm.getGamemode());
    }

    private static String capitalize(String string) {
        if (string.length() == 0)
            return string;

        string = string.toLowerCase();

        var first = Character.toUpperCase(string.charAt(0));
        return first + string.substring(1);
    }

    private static ControlledGameMode getControlledGamemodeById(int id) throws GameModeNotFoundException {
        switch (id) {
            case 0:
                return ControlledGameMode.SURVIVAL;
            case 1:
                return ControlledGameMode.CREATIVE;
            case 2:
                return ControlledGameMode.ADVENTURE;
            case 3:
                return ControlledGameMode.SPECTATOR;
            default:
                throw new GameModeNotFoundException(String.valueOf(id), "The game mode id '" + id + "' could not be found!");
        }
    }

	public static ControlledGameMode getControlledGamemodeByName(String name) throws GameModeNotFoundException {
        if (name.length() == 0)
            throw new GameModeNotFoundException(name, "Please specify a name with at least one character!");

        // minimize name to the absolutely needed information
        // e.g. su = survival, c = creative, a = adventure, sp = spectator
        name = name.toLowerCase();
        if (name.charAt(0) == 's') {
            if (name.length() == 1)
                throw new GameModeNotFoundException(name, "Game mode name '" + name + "' is ambiguous!");
            
            name = name.substring(0, 2);
        } else
            name = name.substring(0, 1);
        
		switch (name) {
            case "su":
            case "0":
                return ControlledGameMode.SURVIVAL;
            case "c":
            case "1":
                return ControlledGameMode.CREATIVE;
            case "a":
            case "2":
                return ControlledGameMode.ADVENTURE;
            case "sp":
            case "3":
                return ControlledGameMode.SPECTATOR;
            default:
                throw new GameModeNotFoundException(name, "The game mode name '" + name + "' could not be found!");
        }
	}

    public static ControlledGameMode getControlledGamemodeByIdOrName(Object o) throws InvalidParameterException, GameModeNotFoundException {
        if (o instanceof String) {
            return getControlledGamemodeByName((String) o);
        } else if (o instanceof Integer) {
            return getControlledGamemodeById((Integer) o);
        } else
            throw new InvalidParameterException("The specified object is neither a string nor an integer!");
    }

    public static ControlledGameMode getControlledGamemodeByGamemode(GameMode gm) throws GameModeNotFoundException {
        switch (gm) {
            case SURVIVAL:
                return ControlledGameMode.SURVIVAL;
            case CREATIVE:
                return ControlledGameMode.CREATIVE;
            case ADVENTURE:
                return ControlledGameMode.ADVENTURE;
            case SPECTATOR:
                return ControlledGameMode.SPECTATOR;
            default:
                throw new GameModeNotFoundException(gm.name(), "Unknown built-in game mode: " + gm.name());
        }
    }

    public static GameMode getGamemodeByIdOrName(Object o) throws InvalidParameterException, GameModeNotFoundException {
        if (o instanceof String) {
            return getControlledGamemodeByName((String) o).getGamemode();
        } else if (o instanceof Integer) {
            return getControlledGamemodeById((Integer) o).getGamemode();
        } else
            throw new InvalidParameterException("The specified object is neither a string nor an integer!");
    }

    public static MessageColor getMessageColor(ControlledGameMode cgm) {
        return cgm.getMessageColor();
    }

    public enum ControlledGameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SPECTATOR;

        public String getName() {
            return this.toString();
        }

        public int getId() {
            switch (this) {
                case CREATIVE:
                    return 1;
                case ADVENTURE:
                    return 2;
                case SPECTATOR:
                    return 3;
                default:
                    return 0;
            }
        }

        public GameMode getGamemode() {
            switch (this) {
                case CREATIVE:
                    return GameMode.CREATIVE;
                case ADVENTURE:
                    return GameMode.ADVENTURE;
                case SPECTATOR:
                    return GameMode.SPECTATOR;
                default:
                    return GameMode.SURVIVAL;
            }
        }

        public MessageColor getMessageColor() {
            switch (this) {
                case SURVIVAL:
                    return MessageColor.GM_SURVIVAL;
                case CREATIVE:
                    return MessageColor.GM_CREATIVE;
                case ADVENTURE:
                    return MessageColor.GM_ADVENTURE;
                case SPECTATOR:
                    return MessageColor.GM_SPECTATOR;
                default:
                    return MessageColor.GRAY;
            }
        }

        public String getConsoleFormatted() {
            return CGM.capitalize(this.toString());
        }

        public String getMessageFormatted() {
            return this.getMessageColor() + CGM.capitalize(this.toString());
        }
    }
}
