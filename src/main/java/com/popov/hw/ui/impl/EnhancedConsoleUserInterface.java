package com.popov.hw.ui.impl;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.exception.InvalidInputException;
import com.popov.hw.i18n.MessageService;
import com.popov.hw.ui.ConsoleColors;
import com.popov.hw.ui.ConsoleFormatter;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Primary
@Component
@RequiredArgsConstructor
public class EnhancedConsoleUserInterface implements UserInterface {

    private final MessageService messageService;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public CryptoAlgorithm selectAlgorithm() {
        ConsoleFormatter.clearScreen();
        displayWelcomeBanner();
        displayAlgorithmMenu();

        System.out.print(ConsoleColors.prompt("\n" + messageService.getEnterLabNumberPrompt()));
        String input = scanner.nextLine().trim();

        try {
            int number = Integer.parseInt(input);
            CryptoAlgorithm algorithm = CryptoAlgorithm.fromNumber(number);
            displaySelection("Algorithm", messageService.getMessage("algorithm." + algorithm.getMessageKey()));
            return algorithm;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(messageService.getMessage("error.invalid.lab.number", input));
        }
    }

    @Override
    public CipherOperation selectOperation() {
        System.out.println("\n" + ConsoleFormatter.createSeparator(70));
        System.out.println(ConsoleColors.title("  OPERATION SELECTION"));
        System.out.println(ConsoleFormatter.createSeparator(70));

        System.out.println("\n" + ConsoleColors.CYAN + "  Available operations:" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN + "    • encrypt" + ConsoleColors.RESET + " - Protect your data with encryption");
        System.out.println(ConsoleColors.YELLOW + "    • decrypt" + ConsoleColors.RESET + " - Retrieve original data from encrypted file");
        System.out.println();

        System.out.print(ConsoleColors.prompt("  " + messageService.getEnterOperationPrompt()));
        String input = scanner.nextLine();

        try {
            CipherOperation operation = CipherOperation.fromString(input);
            displaySelection("Operation", operation.getOperation().toUpperCase());
            return operation;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(messageService.getInvalidOperationError(input));
        }
    }

    @Override
    public String getInputFilePath() {
        System.out.println("\n" + ConsoleFormatter.createSeparator(70));
        System.out.println(ConsoleColors.title("  FILE PATHS CONFIGURATION"));
        System.out.println(ConsoleFormatter.createSeparator(70));
        System.out.println();

        System.out.print(ConsoleColors.prompt("  " + messageService.getEnterInputFilePrompt()));
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.input.file"));
        }

        System.out.println(ConsoleColors.info("  Input: " + ConsoleColors.highlight(path)));
        return path;
    }

    @Override
    public String getOutputFilePath() {
        System.out.print(ConsoleColors.prompt("  " + messageService.getEnterOutputFilePrompt()));
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.output.file"));
        }

        System.out.println(ConsoleColors.info("  Output: " + ConsoleColors.highlight(path)));
        return path;
    }

    @Override
    public String promptInput(String messageKey, Object... args) {
        String message = messageService.getMessage(messageKey, args);
        System.out.print(ConsoleColors.PURPLE + "  ➜ " + ConsoleColors.RESET + message);
        return scanner.nextLine().trim();
    }

    @Override
    public void displayInfo(String messageKey, Object... args) {
        String message = messageService.getMessage(messageKey, args);
        System.out.println(ConsoleColors.CYAN + "    ℹ " + ConsoleColors.RESET + message);
    }

    @Override
    public void displayError(String message) {
        System.out.println("\n" + ConsoleFormatter.createSeparator(70));
        System.out.println(ConsoleColors.RED_BOLD + "  ╔════════════════════════════════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED_BOLD + "  ║  " + ConsoleColors.RESET + ConsoleColors.error("ERROR") + ConsoleColors.RED_BOLD + "                                                        ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED_BOLD + "  ╠════════════════════════════════════════════════════════════════╣" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "  ║  " + message + ConsoleColors.RED_BOLD + "  ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED_BOLD + "  ╚════════════════════════════════════════════════════════════════╝" + ConsoleColors.RESET);
        System.out.println(ConsoleFormatter.createSeparator(70) + "\n");
    }

    @Override
    public void displaySuccess() {
        System.out.println("\n" + ConsoleFormatter.createSeparator(70));
        System.out.println(ConsoleColors.GREEN_BOLD + "  ╔════════════════════════════════════════════════════════════════╗" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "  ║  " + ConsoleColors.success("SUCCESS") + ConsoleColors.GREEN_BOLD + "                                                    ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "  ╠════════════════════════════════════════════════════════════════╣" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN + "  ║  " + messageService.getSuccessMessage() + ConsoleColors.GREEN_BOLD + "               ║" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN_BOLD + "  ╚════════════════════════════════════════════════════════════════╝" + ConsoleColors.RESET);
        displaySuccessAnimation();
        System.out.println(ConsoleFormatter.createSeparator(70) + "\n");
    }

    @Override
    public void close() {
        System.out.println("\n" + ConsoleColors.CYAN_BOLD + "  ═══════════════════════════════════════════════════════════════" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.CYAN_BOLD + "    Thank you for using " + ConsoleColors.BLUE_BOLD + "Cryptology Labs" + ConsoleColors.CYAN_BOLD + "! 🔐" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.CYAN_BOLD + "  ═══════════════════════════════════════════════════════════════" + ConsoleColors.RESET + "\n");
        scanner.close();
    }

    private void displayWelcomeBanner() {
        String banner =
            ConsoleColors.CYAN_BOLD + "\n" +
            "  ╔═══════════════════════════════════════════════════════════════════════╗\n" +
            "  ║                                                                       ║\n" +
            "  ║              " + ConsoleColors.BLUE_BOLD + "🔐  CRYPTOLOGY LABS  🔐" + ConsoleColors.CYAN_BOLD + "                          ║\n" +
            "  ║                                                                       ║\n" +
            "  ║           " + ConsoleColors.WHITE + "Advanced Cryptographic Algorithms Suite" + ConsoleColors.CYAN_BOLD + "                  ║\n" +
            "  ║                                                                       ║\n" +
            "  ╚═══════════════════════════════════════════════════════════════════════╝\n" +
            ConsoleColors.RESET;

        System.out.println(banner);
    }

    private void displayAlgorithmMenu() {
        System.out.println(ConsoleFormatter.createSeparator(70));
        System.out.println(ConsoleColors.title("  AVAILABLE ALGORITHMS"));
        System.out.println(ConsoleFormatter.createSeparator(70));
        System.out.println();

        String[][] algorithmData = new String[CryptoAlgorithm.values().length][3];
        int index = 0;

        for (CryptoAlgorithm algorithm : CryptoAlgorithm.values()) {
            String name = messageService.getMessage("algorithm." + algorithm.getMessageKey());
            String icon = getAlgorithmIcon(algorithm);
            String description = getAlgorithmDescription(algorithm);

            algorithmData[index][0] = ConsoleColors.CYAN_BOLD + String.valueOf(algorithm.getNumber()) + ConsoleColors.RESET;
            algorithmData[index][1] = icon + " " + ConsoleColors.WHITE_BOLD + name + ConsoleColors.RESET;
            algorithmData[index][2] = ConsoleColors.WHITE + description + ConsoleColors.RESET;
            index++;
        }

        String[] headers = {
            ConsoleColors.YELLOW_BOLD + "#" + ConsoleColors.RESET,
            ConsoleColors.YELLOW_BOLD + "Algorithm" + ConsoleColors.RESET,
            ConsoleColors.YELLOW_BOLD + "Description" + ConsoleColors.RESET
        };

        System.out.println(ConsoleFormatter.createTable(headers, algorithmData));
    }

    private String getAlgorithmIcon(CryptoAlgorithm algorithm) {
        return switch (algorithm) {
            case RSA -> ConsoleColors.GREEN + "🔑" + ConsoleColors.RESET;
            case EL_GAMAL -> ConsoleColors.BLUE + "🛡️" + ConsoleColors.RESET;
            case SHAMIR -> ConsoleColors.PURPLE + "🤝" + ConsoleColors.RESET;
            case RABIN -> ConsoleColors.YELLOW + "⚡" + ConsoleColors.RESET;
            case ELLIPTIC_CURVE -> ConsoleColors.CYAN + "📈" + ConsoleColors.RESET;
        };
    }

    private String getAlgorithmDescription(CryptoAlgorithm algorithm) {
        return switch (algorithm) {
            case RSA -> "Public-key cryptosystem";
            case EL_GAMAL -> "Asymmetric encryption";
            case SHAMIR -> "Secret sharing protocol";
            case RABIN -> "Quadratic residue system";
            case ELLIPTIC_CURVE -> "ECC-based encryption";
        };
    }

    private void displaySelection(String label, String value) {
        System.out.println(ConsoleColors.GREEN + "  ✓ " + ConsoleColors.RESET +
                          ConsoleColors.WHITE_BOLD + label + ": " + ConsoleColors.RESET +
                          ConsoleColors.highlight(value));
    }

    private void displaySuccessAnimation() {
        String[] frames = {
            "  " + ConsoleColors.GREEN + "▓" + ConsoleColors.WHITE + "░░░░░░░░░░░░░░░░░░░" + ConsoleColors.RESET + " 5%",
            "  " + ConsoleColors.GREEN + "▓▓▓▓" + ConsoleColors.WHITE + "░░░░░░░░░░░░░░░░" + ConsoleColors.RESET + " 20%",
            "  " + ConsoleColors.GREEN + "▓▓▓▓▓▓▓▓" + ConsoleColors.WHITE + "░░░░░░░░░░░░" + ConsoleColors.RESET + " 40%",
            "  " + ConsoleColors.GREEN + "▓▓▓▓▓▓▓▓▓▓▓▓" + ConsoleColors.WHITE + "░░░░░░░░" + ConsoleColors.RESET + " 60%",
            "  " + ConsoleColors.GREEN + "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓" + ConsoleColors.WHITE + "░░░░" + ConsoleColors.RESET + " 80%",
            "  " + ConsoleColors.GREEN_BOLD + "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓" + ConsoleColors.RESET + " " + ConsoleColors.GREEN_BOLD + "100% ✓ Complete!" + ConsoleColors.RESET
        };

        try {
            System.out.println();
            for (String frame : frames) {
                System.out.print("\r" + frame);
                System.out.flush();
                Thread.sleep(120);
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

