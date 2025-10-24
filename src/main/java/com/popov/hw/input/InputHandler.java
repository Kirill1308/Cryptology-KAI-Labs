package com.popov.hw.input;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.ui.UserInterface;

public interface InputHandler<T> {

    T collectParameters(UserInterface ui);

    CryptoAlgorithm getSupportedAlgorithm();
}
