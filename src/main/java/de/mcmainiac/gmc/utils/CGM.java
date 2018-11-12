package de.mcmainiac.gmc.utils;

import com.google.common.collect.ImmutableMap;
import de.mcmainiac.gmc.Main;
import de.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import de.mcmainiac.gmc.helpers.Config;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.InvalidParameterException;

/**
 * Controlled Game Mode
 * @author MCMainiac
 */
public class CGM {
	//-----------
	// static methods
	//-----------
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
			case 0: return ControlledGameMode.SURVIVAL;
			case 1: return ControlledGameMode.CREATIVE;
			case 2: return ControlledGameMode.ADVENTURE;
			case 3: return ControlledGameMode.SPECTATOR;
			default: throw new GameModeNotFoundException("The game mode id '" + id + "' could not be found!");
		}
	}

	/* Maybe we will need this later
	public static ControlledGameMode getCGMByName(String name) throws GameModeNotFoundException {
		switch (name.toLowerCase()) {
		case "survival": return ControlledGameMode.SURVIVAL;
		case "creative": return ControlledGameMode.CREATIVE;
		case "adventure": return ControlledGameMode.ADVENTURE;
		case "spectator": return ControlledGameMode.SPECTATOR;
		default: throw new GameModeNotFoundException("The game mode name '" + name + "' could not be found!");
		}
	}*/

	public static ControlledGameMode getCGMByIdOrName(Object o) throws InvalidParameterException, GameModeNotFoundException {
		if (o instanceof String) {
			switch (((String)o).toLowerCase()) {
				case "survival":
				case "0":
					return ControlledGameMode.SURVIVAL;
				case "creative":
				case "1":
					return ControlledGameMode.CREATIVE;
				case "adventure":
				case "2":
					return ControlledGameMode.ADVENTURE;
				case "spectator":
				case "3":
					return ControlledGameMode.SPECTATOR;
				default:
					throw new GameModeNotFoundException("The game mode name '" + o + "' could not be found!");
			}
		} else if (o instanceof Integer) {
			return getControlledGamemodeById((Integer) o);
		} else
			throw new InvalidParameterException("The specified object is neither a string nor an integer!");
	}

	public static ControlledGameMode getCGMByGamemode(GameMode gm) throws GameModeNotFoundException {
		switch (gm) {
			case SURVIVAL: return ControlledGameMode.SURVIVAL;
			case CREATIVE: return ControlledGameMode.CREATIVE;
			case ADVENTURE: return ControlledGameMode.ADVENTURE;
			case SPECTATOR: return ControlledGameMode.SPECTATOR;
			default: throw new GameModeNotFoundException();
		}
	}

	/*private static GameMode getGamemodeByCGM(ControlledGameMode cgm) throws GameModeNotFoundException {
		switch (cgm) {
			case SURVIVAL: return GameMode.SURVIVAL;
			case CREATIVE: return GameMode.CREATIVE;
			case ADVENTURE: return GameMode.ADVENTURE;
			case SPECTATOR: return GameMode.SPECTATOR;
			default: throw new GameModeNotFoundException();
		}
	}*/

	private static GameMode getGamemodeById(int id) throws GameModeNotFoundException {
		switch (id) {
			case 0: return GameMode.SURVIVAL;
			case 1: return GameMode.CREATIVE;
			case 2: return GameMode.ADVENTURE;
			case 3: return GameMode.SPECTATOR;
			default: throw new GameModeNotFoundException();
		}
	}

	/*public static GameMode getGamemodeByName(String name) throws GameModeNotFoundException {
		switch(name.toLowerCase()) {
			case "survival": return GameMode.SURVIVAL;
			case "creative": return GameMode.CREATIVE;
			case "adventure": return GameMode.ADVENTURE;
			case "spectator": return GameMode.SPECTATOR;
			default: throw new GameModeNotFoundException();
		}
	}*/

	public static GameMode getGamemodeByIdOrName(Object o) throws InvalidParameterException, GameModeNotFoundException {
		if (o instanceof String) {
			switch (((String)o).toLowerCase()) {
				case "survival":
				case "0":
					return GameMode.SURVIVAL;
				case "creative":
				case "1":
					return GameMode.CREATIVE;
				case "adventure":
				case "2":
					return GameMode.ADVENTURE;
				case "spectator":
				case "3":
					return GameMode.SPECTATOR;
				default:
					throw new GameModeNotFoundException("The game mode name '" + o + "' could not be found!");
			}
		} else if (o instanceof Integer) {
			return getGamemodeById((Integer) o);
		} else
			throw new InvalidParameterException("The specified object is neither a string nor an integer!");
	}

	public static MessageColor getMessageColor(ControlledGameMode cgm) {
		return cgm.getMessageColor();
	}

	//----------
	// Game modes
	//----------
	public enum ControlledGameMode {
		SURVIVAL,
		CREATIVE,
		ADVENTURE,
		SPECTATOR;

		public String getName() {
			return this.toString();
		}

		public int getId() {
			switch(this) {
				case CREATIVE: return 1;
				case ADVENTURE: return 2;
				case SPECTATOR: return 3;
				default: return 0;
			}
		}

		public GameMode getGamemode() {
			switch(this) {
				case CREATIVE: return GameMode.CREATIVE;
				case ADVENTURE: return GameMode.ADVENTURE;
				case SPECTATOR: return GameMode.SPECTATOR;
				default: return GameMode.SURVIVAL;
			}
		}

		public MessageColor getMessageColor() {
			switch (this) {
				case SURVIVAL: return MessageColor.GM_SURVIVAL;
				case CREATIVE: return MessageColor.GM_CREATIVE;
				case ADVENTURE: return MessageColor.GM_ADVENTURE;
				case SPECTATOR: return MessageColor.GM_SPECTATOR;
				default: return MessageColor.GRAY;
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
