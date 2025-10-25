package com.popov.hw.ui.impl;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.exception.InvalidInputException;
import com.popov.hw.i18n.MessageService;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ConsoleUserInterface implements UserInterface {

    private final MessageService messageService;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public CryptoAlgorithm selectAlgorithm() {
        displayAlgorithmMenu();
        System.out.print(messageService.getEnterLabNumberPrompt());
        String input = scanner.nextLine().trim();

        try {
            int number = Integer.parseInt(input);
            return CryptoAlgorithm.fromNumber(number);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(messageService.getMessage("error.invalid.lab.number", input));
        }
    }

    @Override
    public CipherOperation selectOperation() {
        System.out.print(messageService.getEnterOperationPrompt());
        String input = scanner.nextLine();

        try {
            return CipherOperation.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(messageService.getInvalidOperationError(input));
        }
    }

    @Override
    public String getInputFilePath() {
        System.out.print(messageService.getEnterInputFilePrompt());
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.input.file"));
        }

        return path;
    }

    @Override
    public String getOutputFilePath() {
        System.out.print(messageService.getEnterOutputFilePrompt());
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            throw new InvalidInputException(messageService.getMessage("error.empty.output.file"));
        }

        return path;
    }

    @Override
    public String promptInput(String messageKey, Object... args) {
        System.out.print(messageService.getMessage(messageKey, args));
        return scanner.nextLine().trim();
    }

    @Override
    public void displayInfo(String messageKey, Object... args) {
        System.out.println(messageService.getMessage(messageKey, args));
    }

    @Override
    public void displayError(String message) {
        System.err.println(messageService.getErrorMessage(message));
    }

    @Override
    public void displaySuccess() {
        System.out.println(messageService.getSuccessMessage());
    }

    @Override
    public void close() {
        scanner.close();
    }

    private void displayAlgorithmMenu() {
        System.out.println("=== " + messageService.getAppTitle() + " ===");
        System.out.println(messageService.getSelectLabMessage());

        for (CryptoAlgorithm algorithm : CryptoAlgorithm.values()) {
            String name = messageService.getMessage("algorithm." + algorithm.getMessageKey());
            System.out.println(algorithm.getNumber() + ". " + name);
        }
    }
}
