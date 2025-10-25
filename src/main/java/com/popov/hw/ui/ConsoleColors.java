package com.popov.hw.ui;

public class ConsoleColors {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BOLD = "\u001B[1;30m";
    public static final String RED_BOLD = "\u001B[1;31m";
    public static final String GREEN_BOLD = "\u001B[1;32m";
    public static final String YELLOW_BOLD = "\u001B[1;33m";
    public static final String BLUE_BOLD = "\u001B[1;34m";
    public static final String PURPLE_BOLD = "\u001B[1;35m";
    public static final String CYAN_BOLD = "\u001B[1;36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static final String BLACK_BG = "\u001B[40m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String PURPLE_BG = "\u001B[45m";
    public static final String CYAN_BG = "\u001B[46m";
    public static final String WHITE_BG = "\u001B[47m";

    public static String colorize(String text, String color) {
        return color + text + RESET;
    }

    public static String success(String text) {
        return GREEN_BOLD + "✓ " + text + RESET;
    }

    public static String error(String text) {
        return RED_BOLD + "✗ " + text + RESET;
    }

    public static String info(String text) {
        return CYAN + "ℹ " + text + RESET;
    }

    public static String warning(String text) {
        return YELLOW_BOLD + "⚠ " + text + RESET;
    }

    public static String highlight(String text) {
        return CYAN_BOLD + text + RESET;
    }

    public static String title(String text) {
        return BLUE_BOLD + text + RESET;
    }

    public static String prompt(String text) {
        return PURPLE + text + RESET;
    }
}
