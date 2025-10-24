package com.popov.hw.ui;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;

public interface UserInterface {

    void displayAlgorithmMenu();

    CryptoAlgorithm getAlgorithmSelection();

    CipherOperation getCipherOperation();

    String[] getFilePaths();

    void displayError(String message);

    void displaySuccess(String message);

    void displayInfo(String message);

    String getInput(String prompt);

    void close();
}
