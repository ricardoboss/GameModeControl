package com.mcmainiac.gmc.helpers;

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
		case BLACK:	return "§0";
		case DARK_BLUE: return "§1";
		case DARK_GREEN: return"§2";
		case DARK_AQUA: return"§3";
		case DARK_RED: return"§4";
		case DARK_PURPLE: return"§5";
		case GOLD:
			case GM_SURVIVAL:
			return"§6";
		case GRAY:
			default:
			return"§7";
		case DARK_GRAY: return"§8";
		case BLUE:
			case GM_ADVENTURE:
			return"§9";
		case GREEN:
			case SUCCESS:
			case GM_SPECTATOR:
			return"§a";
		case AQUA:
			case GM_CREATIVE: 
			return"§b";
		case RED:
			case ERROR:
			return"§c";
		case LIGHT_PURPLE: return"§d";
		case YELLOW:
			case WARNING:
			return"§e";
		case WHITE: return"§f";
		}
	}
}