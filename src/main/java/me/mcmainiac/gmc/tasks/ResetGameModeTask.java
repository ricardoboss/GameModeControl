package me.mcmainiac.gmc.tasks;

import me.mcmainiac.gmc.Main;
import me.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import me.mcmainiac.gmc.utils.CGM;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ResetGameModeTask implements Runnable {
	private Player p;
	private GameMode oldgm;

	public ResetGameModeTask(Player p, GameMode oldgm) {
		this.p = p;
		this.oldgm = oldgm;
	}

	@Override
	public void run() {
		try {
			CGM.set(p, CGM.getCGMByGamemode(oldgm)); // reset the old gamemode of the player
		} catch (GameModeNotFoundException e) {
			if (Main.config.getBoolean("options.debug")) {
				Main.log("An exception occurred while resetting \"" + p.getName() + "\"'s game mode:");
				e.printStackTrace();
			}
		}
	}
}
