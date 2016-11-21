package me.mcmainiac.gmc.utils;

public enum MessageFormat {
    RESET,
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKE_THROUGH,
    OBFUSCATED;

    @Override
    public String toString() {
        switch (this) {
            case OBFUSCATED:
                return "\u00A7k";
            case BOLD:
                return "\u00A7l";
            case STRIKE_THROUGH:
                return "\u00A7m";
            case UNDERLINE:
                return "\u00A7n";
            case ITALIC:
                return "\u00A7o";
            case RESET:
            default:
                return "\u00A7r";
        }
    }
}
