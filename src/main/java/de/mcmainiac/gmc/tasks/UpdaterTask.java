package de.mcmainiac.gmc.tasks;

import de.mcmainiac.gmc.Main;
import de.mcmainiac.gmc.utils.Updater;
import de.mcmainiac.gmc.utils.MessageColor;

import java.io.File;

public class UpdaterTask implements Runnable {
	private boolean update = false;
	private final Main plugin;
	private final File pluginFile;

	public UpdaterTask(Main p, File f, boolean u) {
		this.plugin = p;
		this.pluginFile = f;
		this.update = u;
	}

	@Override
	public void run() {
		if (Main.debug)
			Main.log("Running updater");

		Updater.UpdateType updateType;
		if (update)
			updateType = Updater.UpdateType.DEFAULT;
		else
			updateType = Updater.UpdateType.NO_DOWNLOAD;

		Updater updater = new Updater(plugin, 71110, pluginFile, updateType, true);

		switch(updater.getResult()) {
			case NO_UPDATE:
				Main.log("[Updater] No update was found (last version: " + updater.getLatestName() +
						"; this version: " + plugin.getDescription().getVersion() + ").");
				break;
			case SUCCESS:
				Main.log("[Updater] The newest version " + updater.getLatestName() + " has been downloaded and will be");
				Main.log("[Updater] loaded the next time the server restarts/reloads.");
				break;
			case UPDATE_AVAILABLE:
				Main.log("[Updater] There is a newer version available: " + updater.getLatestName() + ", but since");
				Main.log("[Updater] auto-update is disabled, nothing was downloaded.");
				break;
			case DISABLED:
				break;
			default:
				Main.log("[Updater] Something went wrong while updating!", MessageColor.ERROR);
				break;
		}
	}
}
