package de.mcmainiac.gmc.helpers;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;

class Permissions {
	private static final HashMap<Perm, Permission> permissions = new HashMap<>();

	static {
		permissions.put(Perm.GMI, new Permission("gmc.gmi", PermissionDefault.TRUE));
		permissions.put(Perm.GMH, new Permission("gmc.gmh", PermissionDefault.TRUE));
		permissions.put(Perm.GMR, new Permission("gmc.gmr", PermissionDefault.OP));
		permissions.put(Perm.FORCEGM, new Permission("gmc.forcegm", PermissionDefault.TRUE));
		permissions.put(Perm.GMONCE, new Permission("gmc.gmonce", PermissionDefault.OP));
		permissions.put(Perm.GMTEMP, new Permission("gmc.gmtemp", PermissionDefault.OP));

		permissions.put(Perm.GM, new Permission("gmc.gm", PermissionDefault.OP));
		permissions.put(Perm.GM_SELF, new Permission("gmc.gm.self", PermissionDefault.OP));
		permissions.put(Perm.GM_OTHERS, new Permission("gmc.gm.others", PermissionDefault.OP));
		permissions.put(Perm.GAMEMODE, new Permission("gmc.gamemode", PermissionDefault.OP));
		permissions.put(Perm.GAMEMODE_SELF, new Permission("gmc.gamemode.self", PermissionDefault.OP));
		permissions.put(Perm.GAMEMODE_OTHERS, new Permission("gmc.gamemode.others", PermissionDefault.OP));
		permissions.put(Perm.SURVIVAL, new Permission("gmc.survival", PermissionDefault.OP));
		permissions.put(Perm.SURVIVAL_SELF, new Permission("gmc.survival.self", PermissionDefault.OP));
		permissions.put(Perm.SURVIVAL_OTHERS, new Permission("gmc.survival.others", PermissionDefault.OP));
		permissions.put(Perm.CREATIVE, new Permission("gmc.creative", PermissionDefault.OP));
		permissions.put(Perm.CREATIVE_SELF, new Permission("gmc.creative.self", PermissionDefault.OP));
		permissions.put(Perm.CREATIVE_OTHERS, new Permission("gmc.creative.others", PermissionDefault.OP));
		permissions.put(Perm.ADVENTURE, new Permission("gmc.adventure", PermissionDefault.OP));
		permissions.put(Perm.ADVENTURE_SELF, new Permission("gmc.adventure.self", PermissionDefault.OP));
		permissions.put(Perm.ADVENTURE_OTHERS, new Permission("gmc.adventure.others", PermissionDefault.OP));
		permissions.put(Perm.SPECTATOR, new Permission("gmc.spectator", PermissionDefault.OP));
		permissions.put(Perm.SPECTATOR_SELF, new Permission("gmc.spectator.self", PermissionDefault.OP));
		permissions.put(Perm.SPECTATOR_OTHERS, new Permission("gmc.spectator.others", PermissionDefault.OP));

		permissions.put(Perm.DEFAULT_OP, new Permission("gmc.default-op", PermissionDefault.OP));
	}

	static Permission get(Perm key) {
		return permissions.get(key);
	}

	enum Perm {
		GMI,
		GMH,
		GMR,
		FORCEGM,
		GMONCE,
		GMTEMP,
		GM,
		GM_SELF,
		GM_OTHERS,
		GAMEMODE,
		GAMEMODE_SELF,
		GAMEMODE_OTHERS,
		SURVIVAL,
		SURVIVAL_SELF,
		SURVIVAL_OTHERS,
		CREATIVE,
		CREATIVE_SELF,
		CREATIVE_OTHERS,
		ADVENTURE,
		ADVENTURE_SELF,
		ADVENTURE_OTHERS,
		SPECTATOR,
		SPECTATOR_SELF,
		SPECTATOR_OTHERS,

		DEFAULT_OP;

		public static Perm fromString(String perm) {
			perm = perm.toLowerCase();
			switch (perm) {
				case "gmi": return GMI;
				case "gmh": return GMH;
				case "gmr": return GMR;
				case "forcegm": return FORCEGM;
				case "gmonce": return GMONCE;
				case "gmtemp": return GMTEMP;
				case "gm": return GM;
				case "gm.self": return GM_SELF;
				case "gm.others": return GM_OTHERS;
				case "gamemode": return GAMEMODE;
				case "gamemode.self": return GAMEMODE_SELF;
				case "gamemode.others": return GAMEMODE_OTHERS;
				case "survival": return SURVIVAL;
				case "survival.self": return SURVIVAL_SELF;
				case "survival.others": return SURVIVAL_OTHERS;
				case "creative": return CREATIVE;
				case "creative.self": return CREATIVE_SELF;
				case "creative.others": return CREATIVE_OTHERS;
				case "adventure": return ADVENTURE;
				case "adventure.self": return ADVENTURE_SELF;
				case "adventure.others": return ADVENTURE_OTHERS;
				case "spectator": return SPECTATOR;
				case "spectator.self": return SPECTATOR_SELF;
				case "spectator.others": return SPECTATOR_OTHERS;
				default: return DEFAULT_OP;
			}
		}
	}
}
