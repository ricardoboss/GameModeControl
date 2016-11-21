package de.mcmainiac.gmc.tasks;

import de.mcmainiac.gmc.Main;
import de.mcmainiac.gmc.excpetions.GameModeNotFoundException;
import de.mcmainiac.gmc.utils.CGM;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ResetGameModeTask implements Runnable {
	private final Player p;
	private final GameMode oldgm;

	public ResetGameModeTask(Player p, GameMode oldgm) {
		this.p = p;
		this.oldgm = oldgm;
	}

	@Override
	public void run() {
		try {
			CGM.set(p, CGM.getCGMByGamemode(oldgm)); // reset the old gamemode of the player
		} catch (GameModeNotFoundException e) {
			Main.log("An exception occurred while resetting \"" + p.getName() + "\"'s game mode!");

			if (Main.debug)
				e.printStackTrace();
		}
	}
}
