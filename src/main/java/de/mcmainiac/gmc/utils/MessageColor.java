package de.mcmainiac.gmc.utils;

/**
 * It's actually an alias for org.bukkit.ChatColor.
 * Just for static SUCCESS, INFO, WARNING,
 * ERROR if I want to change one of the colors
 * later.
 *
 * @version V1.0
 * @author MCMainiac
 */
public enum MessageColor {
	SUCCESS,
	INFO,
	WARNING,
	ERROR,

	GM_SURVIVAL,
	GM_CREATIVE,
	GM_ADVENTURE,
	GM_SPECTATOR,

	BLACK,
	DARK_BLUE,
	DARK_GREEN,
	DARK_AQUA,
	DARK_RED,
	DARK_PURPLE,
	GOLD,
	GRAY,
	DARK_GRAY,
	BLUE,
	GREEN,
	AQUA,
	RED,
	LIGHT_PURPLE,
	YELLOW,
	WHITE;

	@Override
	public String toString() {
		switch(this) {
			case BLACK:
				return "\u00A70";
			case DARK_BLUE:
				return "\u00A71";
			case DARK_GREEN:
				return "\u00A72";
			case DARK_AQUA:
				return "\u00A73";
			case DARK_RED:
				return "\u00A74";
			case DARK_PURPLE:
				return "\u00A75";
			case GOLD:
			case GM_SURVIVAL:
				return "\u00A76";
			case GRAY:
			case INFO:
			default:
				return "\u00A77";
			case DARK_GRAY:
				return "\u00A78";
			case BLUE:
			case GM_ADVENTURE:
				return "\u00A79";
			case GREEN:
			case SUCCESS:
			case GM_SPECTATOR:
				return "\u00A7a";
			case AQUA:
			case GM_CREATIVE:
				return "\u00A7b";
			case RED:
			case ERROR:
				return "\u00A7c";
			case LIGHT_PURPLE:
				return "\u00A7d";
			case YELLOW:
			case WARNING:
				return "\u00A7e";
			case WHITE:
				return "\u00A7f";
		}
	}
}
