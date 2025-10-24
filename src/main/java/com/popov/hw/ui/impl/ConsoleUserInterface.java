package com.popov.hw.ui.impl;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.ui.UserInterface;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.function.Supplier;

@Component
public class ConsoleUserInterface implements UserInterface {

    private final Scanner scanner;
    private final Supplier<String> inputSupplier;

    public ConsoleUserInterface() {
        this.scanner = new Scanner(System.in);
        this.inputSupplier = scanner::nextLine;
    }

    @Override
    public void displayAlgorithmMenu() {
        System.out.println("=== Cryptology Labs ===");
        System.out.println("Select lab:");
        for (CryptoAlgorithm algorithm : CryptoAlgorithm.values()) {
            System.out.println(algorithm.getNumber() + ". " + algorithm.getDisplayName());
        }
    }

    @Override
    public CryptoAlgorithm getAlgorithmSelection() {
        System.out.print("Enter lab number: ");
        String input = inputSupplier.get();
        return CryptoAlgorithm.fromString(input);
    }

    @Override
    public CipherOperation getCipherOperation() {
        System.out.print("Enter cipher type (encrypt/decrypt): ");
        String input = inputSupplier.get();
        return CipherOperation.fromString(input);
    }

    @Override
    public String[] getFilePaths() {
        System.out.print("Enter input file path: ");
        String inputFile = inputSupplier.get();

        if (inputFile == null || inputFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Input file path cannot be empty");
        }

        System.out.print("Enter output file path: ");
        String outputFile = inputSupplier.get();

        if (outputFile == null || outputFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Output file path cannot be empty");
        }

        return new String[]{inputFile, outputFile};
    }

    @Override
    public void displayError(String message) {
        System.err.println("Error: " + message);
    }

    @Override
    public void displaySuccess(String message) {
        System.out.println("Success: " + message);
    }

    @Override
    public void displayInfo(String message) {
        System.out.println(message);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return inputSupplier.get();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
