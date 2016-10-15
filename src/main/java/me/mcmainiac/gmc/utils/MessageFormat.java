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
            case RESET: return "\u00A7r";
            case BOLD: return "\u00A7l";
            case ITALIC: return "\u00A7o";
            case UNDERLINE: return "\u00A7n";
            case STRIKE_THROUGH: return "\u00A7m";
            case OBFUSCATED: return "\u00A7k";
        }
        return "";
    }
}
