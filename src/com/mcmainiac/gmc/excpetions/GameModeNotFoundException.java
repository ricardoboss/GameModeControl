package com.mcmainiac.gmc.excpetions;

public class GameModeNotFoundException extends Exception {
	private static final long serialVersionUID = 4135630639716104833L;
	public GameModeNotFoundException() { super(); }
	public GameModeNotFoundException(String message) { super(message); }
	public GameModeNotFoundException(String message, Throwable cause) { super(message, cause); }
	public GameModeNotFoundException(Throwable cause) { super(cause); }
}
