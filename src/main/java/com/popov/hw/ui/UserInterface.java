package com.popov.hw.ui;

import com.popov.hw.enums.CipherOperation;
import com.popov.hw.enums.CryptoAlgorithm;

public interface UserInterface {

    CryptoAlgorithm selectAlgorithm();

    CipherOperation selectOperation();

    String getInputFilePath();

    String getOutputFilePath();

    String promptInput(String messageKey, Object... args);

    void displayInfo(String messageKey, Object... args);

    void displayError(String message);

    void displaySuccess();

    void close();
}
