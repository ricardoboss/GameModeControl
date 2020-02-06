package de.ricardoboss.gmc.excpetions;

public class PlayerNotFoundException extends Exception {
    private static final long serialVersionUID = -2703559143022169978L;

    private final String player;

    public PlayerNotFoundException(String player, String message) {
        super(message);

        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
