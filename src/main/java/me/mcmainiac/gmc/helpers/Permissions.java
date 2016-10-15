package me.mcmainiac.gmc.helpers;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;

class Permissions {
	private static HashMap<String, Permission> permissions = new HashMap<>();

	private Permissions() {
		permissions.put("gmi", new Permission("gmc.gmi", PermissionDefault.TRUE));
		permissions.put("gmh", new Permission("gmc.gmh", PermissionDefault.TRUE));
		permissions.put("forcegm", new Permission("gmc.forcegm", PermissionDefault.TRUE));
		permissions.put("gmr", new Permission("gmc.gmr", PermissionDefault.OP));
		permissions.put("gmonce", new Permission("gmc.gmonce", PermissionDefault.OP));
		permissions.put("gmtemp", new Permission("gmc.gmtemp", PermissionDefault.OP));

		permissions.put("gm", new Permission("gmc.gm", PermissionDefault.OP));
		permissions.put("gm.self", new Permission("gmc.gm.self", PermissionDefault.OP));
		permissions.put("gm.others", new Permission("gmc.gm.others", PermissionDefault.OP));
		permissions.put("gamemode", new Permission("gmc.gamemode", PermissionDefault.OP));
		permissions.put("gamemode.self", new Permission("gmc.gamemode.self", PermissionDefault.OP));
		permissions.put("gamemode.others", new Permission("gmc.gamemode.others", PermissionDefault.OP));
		permissions.put("survival", new Permission("gmc.survival", PermissionDefault.OP));
		permissions.put("survival.self", new Permission("gmc.survival.self", PermissionDefault.OP));
		permissions.put("survival.others", new Permission("gmc.survival.others", PermissionDefault.OP));
		permissions.put("creative", new Permission("gmc.creative", PermissionDefault.OP));
		permissions.put("creative.self", new Permission("gmc.creative.self", PermissionDefault.OP));
		permissions.put("creative.others", new Permission("gmc.creative.others", PermissionDefault.OP));
		permissions.put("adventure", new Permission("gmc.adventure", PermissionDefault.OP));
		permissions.put("adventure.self", new Permission("gmc.adventure.self", PermissionDefault.OP));
		permissions.put("adventure.others", new Permission("gmc.adventure.others", PermissionDefault.OP));
		permissions.put("spectator", new Permission("gmc.spectator", PermissionDefault.OP));
		permissions.put("spectator.self", new Permission("gmc.spectator.self", PermissionDefault.OP));
		permissions.put("spectator.others", new Permission("gmc.spectator.others", PermissionDefault.OP));
	}

	static Permission get(String key) {
		if (permissions.containsKey(key))
			return permissions.get(key);
		else
			return null;
	}
}
