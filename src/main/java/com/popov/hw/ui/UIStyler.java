package com.popov.hw.ui;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UIStyler {

    // ANSI Color codes
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright colors
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // Background colors
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_MAGENTA = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // Text styles
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";
    public static final String REVERSE = "\u001B[7m";

    // Box drawing characters
    public static final String BOX_TOP_LEFT = "╔";
    public static final String BOX_TOP_RIGHT = "╗";
    public static final String BOX_BOTTOM_LEFT = "╚";
    public static final String BOX_BOTTOM_RIGHT = "╝";
    public static final String BOX_HORIZONTAL = "═";
    public static final String BOX_VERTICAL = "║";
    public static final String BOX_VERTICAL_RIGHT = "╠";
    public static final String BOX_VERTICAL_LEFT = "╣";
    public static final String BOX_HORIZONTAL_DOWN = "╦";
    public static final String BOX_HORIZONTAL_UP = "╩";

    // Icons
    public static final String ICON_SUCCESS = "✅";
    public static final String ICON_ERROR = "❌";
    public static final String ICON_INFO = "ℹ️";
    public static final String ICON_WARNING = "⚠️";
    public static final String ICON_LOCK = "🔒";
    public static final String ICON_KEY = "🔑";
    public static final String ICON_ARROW = "➜";

    public static String colorize(String text, String color) {
        return color + text + RESET;
    }

    public static String bold(String text) {
        return BOLD + text + RESET;
    }

    public static String success(String text) {
        return BRIGHT_GREEN + ICON_SUCCESS + " " + text + RESET;
    }

    public static String error(String text) {
        return BRIGHT_RED + ICON_ERROR + " " + text + RESET;
    }

    public static String info(String text) {
        return BRIGHT_CYAN + ICON_INFO + " " + text + RESET;
    }

    public static String warning(String text) {
        return BRIGHT_YELLOW + ICON_WARNING + " " + text + RESET;
    }

    public static String highlight(String text) {
        return BOLD + BRIGHT_CYAN + text + RESET;
    }

    public static String prompt(String text) {
        return BRIGHT_BLUE + ICON_ARROW + " " + text + RESET;
    }

    public static String title(String text) {
        return BOLD + BRIGHT_MAGENTA + text + RESET;
    }

    public static String subtitle(String text) {
        return BOLD + BRIGHT_CYAN + text + RESET;
    }

    public static String createBox(String title, int width) {
        StringBuilder box = new StringBuilder();
        int titleLength = title.length();
        int padding = (width - titleLength - 2) / 2;

        // Top border
        box.append(BRIGHT_CYAN).append(BOX_TOP_LEFT);
        box.append(BOX_HORIZONTAL.repeat(Math.max(0, width - 2)));
        box.append(BOX_TOP_RIGHT).append(RESET).append("\n");

        // Title line
        box.append(BRIGHT_CYAN).append(BOX_VERTICAL).append(RESET);
        box.append(" ".repeat(padding));
        box.append(BOLD).append(BRIGHT_MAGENTA).append(title).append(RESET);
        box.append(" ".repeat(width - titleLength - padding - 2));
        box.append(BRIGHT_CYAN).append(BOX_VERTICAL).append(RESET).append("\n");

        // Bottom border
        box.append(BRIGHT_CYAN).append(BOX_BOTTOM_LEFT);
        box.append(BOX_HORIZONTAL.repeat(Math.max(0, width - 2)));
        box.append(BOX_BOTTOM_RIGHT).append(RESET);

        return box.toString();
    }

    public static String createSeparator(int width) {
        return BRIGHT_BLACK + "─".repeat(width) + RESET;
    }

    public static String createMenuItem(int number, String text, String emoji) {
        return String.format("  %s %s. %s%s%s",
                emoji,
                colorize(String.valueOf(number), BRIGHT_YELLOW),
                BOLD + WHITE,
                text,
                RESET);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }

    public static String padLeft(String text, int length) {
        return String.format("%" + length + "s", text);
    }
}
