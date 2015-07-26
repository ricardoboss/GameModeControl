package com.mcmainiac.gmc.excpetions;

public class PlayerNotFoundException extends Exception {
	private static final long serialVersionUID = -2703559143022169978L;
	public PlayerNotFoundException() { super(); }
	public PlayerNotFoundException(String message) { super(message); }
	public PlayerNotFoundException(String message, Throwable cause) { super(message, cause); }
	public PlayerNotFoundException(Throwable cause) { super(cause); }
}
