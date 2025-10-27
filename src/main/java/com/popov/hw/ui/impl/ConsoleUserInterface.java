package com.popov.hw.ui.impl;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.exception.InvalidInputException;
import com.popov.hw.i18n.MessageService;
import com.popov.hw.ui.UIStyler;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Scanner;

import static com.popov.hw.ui.UIStyler.ICON_LOCK;

@Component
@RequiredArgsConstructor
public class ConsoleUserInterface implements UserInterface {

    private final MessageService messageService;
    private final Scanner scanner = new Scanner(System.in);

    private static final int BOX_WIDTH = 60;
    private boolean localeSelected = false;

    @Override
    public CryptoAlgorithm selectAlgorithm() {
        if (!localeSelected) {
            selectLanguage();
            localeSelected = true;
        }

        displayAlgorithmMenu();
        System.out.print(UIStyler.prompt(messageService.getEnterLabNumberPrompt()));
        String input = scanner.nextLine().trim();

        try {
            int number = Integer.parseInt(input);
            return CryptoAlgorithm.fromNumber(number);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(messageService.getMessage("error.invalid.lab.number", input));
        }
    }

    @Override
    public CipherOperation selectOperation() {
        System.out.println("\n" + UIStyler.createSeparator(BOX_WIDTH));
        System.out.println(UIStyler.subtitle("  " + messageService.getMessage("operation.description.select")));
        System.out.println(UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();

        System.out.println(UIStyler.createMenuItem(
                1,
                messageService.getMessage("operation.encrypt") + " - " + messageService.getMessage("operation.encrypt.description"),
                "🔐"
        ));
        System.out.println(UIStyler.createMenuItem(
                2,
                messageService.getMessage("operation.decrypt") + " - " + messageService.getMessage("operation.decrypt.description"),
                "🔓"
        ));

        System.out.println("\n" + UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();

        System.out.print(UIStyler.prompt(messageService.getEnterOperationPrompt()));
        String input = scanner.nextLine().trim();

        try {
            return CipherOperation.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(messageService.getInvalidOperationError(input));
        }
    }

    @Override
    public String getInputFilePath() {
        System.out.print(UIStyler.prompt(messageService.getEnterInputFilePrompt()));
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.input.file"));
        }

        return path;
    }

    @Override
    public String getOutputFilePath() {
        System.out.print(UIStyler.prompt(messageService.getEnterOutputFilePrompt()));
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.output.file"));
        }

        return path;
    }

    @Override
    public String promptInput(String messageKey, Object... args) {
        System.out.print(UIStyler.prompt(messageService.getMessage(messageKey, args)));
        return scanner.nextLine().trim();
    }

    @Override
    public void displayInfo(String messageKey, Object... args) {
        String message = messageService.getMessage(messageKey, args);
        System.out.println(UIStyler.info(message));
    }

    @Override
    public void displayError(String message) {
        System.err.println("\n" + UIStyler.error(messageService.getErrorMessage(message)));
    }

    @Override
    public void displaySuccess() {
        System.out.println("\n" + UIStyler.createSeparator(BOX_WIDTH));
        System.out.println(UIStyler.success(messageService.getSuccessMessage()));
        System.out.println(UIStyler.createSeparator(BOX_WIDTH) + "\n");
    }

    @Override
    public void close() {
        System.out.println("\n" + UIStyler.colorize("👋 " + messageService.getMessage("app.exit"), UIStyler.BRIGHT_MAGENTA));
        scanner.close();
    }

    private void displayAlgorithmMenu() {
        // Clear screen for better presentation
        System.out.println("\n");

        // Display main title box
        System.out.println(UIStyler.createBox(ICON_LOCK + " " + messageService.getAppTitle() + " " + ICON_LOCK, BOX_WIDTH));
        System.out.println();

        // Display subtitle
        System.out.println(UIStyler.subtitle("  " + messageService.getSelectLabMessage()));
        System.out.println(UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();

        // Display algorithm options with emojis
        for (CryptoAlgorithm algorithm : CryptoAlgorithm.values()) {
            String name = messageService.getMessage("algorithm." + algorithm.getMessageKey());
            System.out.println(UIStyler.createMenuItem(algorithm.getNumber(), name, ICON_LOCK));
        }

        System.out.println("\n" + UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();
    }

    private void selectLanguage() {
        System.out.println("\n");
        System.out.println(UIStyler.createBox(ICON_LOCK + " Cryptology Labs - KAI " + ICON_LOCK, BOX_WIDTH));
        System.out.println();

        System.out.println(UIStyler.subtitle("  SELECT LANGUAGE / ОБЕРІТЬ МОВУ"));
        System.out.println(UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();

        System.out.println(UIStyler.createMenuItem(1, "English", "🇬🇧"));
        System.out.println(UIStyler.createMenuItem(2, "Українська", "🇺🇦"));

        System.out.println("\n" + UIStyler.createSeparator(BOX_WIDTH));
        System.out.println();

        System.out.print(UIStyler.prompt("Enter your choice / Введіть ваш вибір: "));
        String input = scanner.nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> messageService.setLocale(Locale.ENGLISH);
                case 2 -> messageService.setLocale(new Locale("uk"));
                default -> {
                    System.out.println(UIStyler.warning("Invalid choice. Using English by default."));
                    messageService.setLocale(Locale.ENGLISH);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(UIStyler.warning("Invalid input. Using English by default."));
            messageService.setLocale(Locale.ENGLISH);
        }
    }
}
