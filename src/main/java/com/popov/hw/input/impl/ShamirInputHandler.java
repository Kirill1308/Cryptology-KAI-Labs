package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import com.popov.hw.model.CryptoParameters.ShamirParameters;
import com.popov.hw.service.ShamirService;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.SHAMIR;

@Component
@RequiredArgsConstructor
public class ShamirInputHandler implements InputHandler<ShamirParameters> {

    private final ShamirService shamirService;

    @Override
    public ShamirParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.getInput("Enter large prime number p: "));

        BigInteger[] keyPair = shamirService.generateKeyPair(p);
        ui.displayInfo("Generated encryption key cA: " + keyPair[0]);
        ui.displayInfo("Generated decryption key dA: " + keyPair[1]);

        return new ShamirParameters(p, keyPair);
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return SHAMIR;
    }
}
