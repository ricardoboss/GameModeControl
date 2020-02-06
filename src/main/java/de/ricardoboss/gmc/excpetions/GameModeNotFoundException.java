package de.ricardoboss.gmc.excpetions;

public class GameModeNotFoundException extends Exception {
    private static final long serialVersionUID = 4135630639716104833L;

    private final String gamemode;

    public GameModeNotFoundException(String gamemode, String message) {
        super(message);

        this.gamemode = gamemode;
    }

    public String getGamemode() {
        return gamemode;
    }
}
