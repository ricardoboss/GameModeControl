package de.ricardoboss.gmc.utils;

import de.ricardoboss.gmc.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MetricsCollector {
    public static final MostUsedGamemode MOST_USED_GAMEMODE = new MostUsedGamemode();

    public static class MostUsedGamemode implements Callable<Map<String, Integer>> {
        @Override
        public Map<String, Integer> call() throws Exception {
            if (Main.debug)
                Main.log("MOST_USED_GAMEMODE metric is called");

            Map<String, Integer> counts = new HashMap<>();

            CGM.ControlledGameMode cgm;
            Integer count;
            for (Player p : Bukkit.getOnlinePlayers()) {
                cgm = CGM.getControlledGamemodeByGamemode(p.getGameMode());
                count = counts.getOrDefault(cgm.getConsoleFormatted(), 0);

                counts.put(cgm.getConsoleFormatted(), count + 1);
            }

            return counts;
        }
    }
}
