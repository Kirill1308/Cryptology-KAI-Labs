package com.popov.hw.ui;

public class ConsoleFormatter {

    private static final int DEFAULT_WIDTH = 80;
    private static final String HORIZONTAL_LINE = "─";
    private static final String VERTICAL_LINE = "│";
    private static final String TOP_LEFT = "╭";
    private static final String TOP_RIGHT = "╮";
    private static final String BOTTOM_LEFT = "╰";
    private static final String BOTTOM_RIGHT = "╯";
    private static final String CROSS = "┼";
    private static final String T_DOWN = "┬";
    private static final String T_UP = "┴";

    public static String createBox(String title, String content) {
        return createBox(title, content, DEFAULT_WIDTH);
    }

    public static String createBox(String title, String content, int width) {
        StringBuilder box = new StringBuilder();

        box.append(TOP_LEFT).append(HORIZONTAL_LINE.repeat(width - 2)).append(TOP_RIGHT).append("\n");

        if (title != null && !title.isEmpty()) {
            String paddedTitle = centerText(title, width - 4);
            box.append(VERTICAL_LINE).append(" ").append(paddedTitle).append(" ").append(VERTICAL_LINE).append("\n");
            box.append(VERTICAL_LINE).append(HORIZONTAL_LINE.repeat(width - 2)).append(VERTICAL_LINE).append("\n");
        }

        if (content != null && !content.isEmpty()) {
            String[] lines = content.split("\n");
            for (String line : lines) {
                String paddedLine = padRight(line, width - 4);
                box.append(VERTICAL_LINE).append(" ").append(paddedLine).append(" ").append(VERTICAL_LINE).append("\n");
            }
        }

        box.append(BOTTOM_LEFT).append(HORIZONTAL_LINE.repeat(width - 2)).append(BOTTOM_RIGHT);

        return box.toString();
    }

    public static String createSeparator(int width) {
        return HORIZONTAL_LINE.repeat(width);
    }

    public static String createSeparator() {
        return createSeparator(DEFAULT_WIDTH);
    }

    public static String centerText(String text, int width) {
        int padding = (width - getVisibleLength(text)) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    public static String padRight(String text, int width) {
        int visibleLength = getVisibleLength(text);
        int padding = width - visibleLength;
        return text + " ".repeat(Math.max(0, padding));
    }

    public static String createHeader(String text) {
        int width = DEFAULT_WIDTH;
        StringBuilder header = new StringBuilder();

        header.append("\n");
        header.append(ConsoleColors.CYAN_BOLD);
        header.append(TOP_LEFT).append(HORIZONTAL_LINE.repeat(width - 2)).append(TOP_RIGHT).append("\n");
        header.append(VERTICAL_LINE).append(centerText(text, width - 2)).append(VERTICAL_LINE).append("\n");
        header.append(BOTTOM_LEFT).append(HORIZONTAL_LINE.repeat(width - 2)).append(BOTTOM_RIGHT);
        header.append(ConsoleColors.RESET);
        header.append("\n");

        return header.toString();
    }

    public static String createList(String... items) {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            list.append(ConsoleColors.CYAN).append("  ").append(i + 1).append(". ")
                .append(ConsoleColors.RESET).append(items[i]).append("\n");
        }
        return list.toString();
    }

    public static String createTable(String[] headers, String[][] data) {
        int columns = headers.length;
        int[] columnWidths = new int[columns];

        for (int i = 0; i < columns; i++) {
            columnWidths[i] = headers[i].length();
            for (String[] row : data) {
                if (i < row.length) {
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
            }
            columnWidths[i] += 2;
        }

        StringBuilder table = new StringBuilder();

        table.append(TOP_LEFT);
        for (int i = 0; i < columns; i++) {
            table.append(HORIZONTAL_LINE.repeat(columnWidths[i]));
            if (i < columns - 1) table.append(T_DOWN);
        }
        table.append(TOP_RIGHT).append("\n");

        table.append(VERTICAL_LINE);
        for (int i = 0; i < columns; i++) {
            table.append(centerText(headers[i], columnWidths[i]));
            table.append(VERTICAL_LINE);
        }
        table.append("\n");

        table.append(VERTICAL_LINE);
        for (int i = 0; i < columns; i++) {
            table.append(HORIZONTAL_LINE.repeat(columnWidths[i]));
            if (i < columns - 1) table.append(CROSS);
            else table.append(VERTICAL_LINE);
        }
        table.append("\n");

        for (String[] row : data) {
            table.append(VERTICAL_LINE);
            for (int i = 0; i < columns; i++) {
                String cell = i < row.length ? row[i] : "";
                table.append(" ").append(padRight(cell, columnWidths[i] - 1));
                table.append(VERTICAL_LINE);
            }
            table.append("\n");
        }

        table.append(BOTTOM_LEFT);
        for (int i = 0; i < columns; i++) {
            table.append(HORIZONTAL_LINE.repeat(columnWidths[i]));
            if (i < columns - 1) table.append(T_UP);
        }
        table.append(BOTTOM_RIGHT);

        return table.toString();
    }

    private static int getVisibleLength(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "").length();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String createProgressBar(int current, int total, int width) {
        int filled = (int) ((double) current / total * width);
        int empty = width - filled;

        return ConsoleColors.GREEN + "█".repeat(filled) +
               ConsoleColors.WHITE + "░".repeat(empty) +
               ConsoleColors.RESET + " " + current + "/" + total;
    }
}
