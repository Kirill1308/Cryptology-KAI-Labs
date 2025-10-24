package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import com.popov.hw.model.CryptoParameters.ElGamalParameters;
import com.popov.hw.service.ElGamalService;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.EL_GAMAL;

@Component
@RequiredArgsConstructor
public class ElGamalInputHandler implements InputHandler<ElGamalParameters> {

    private final ElGamalService elGamalService;

    @Override
    public ElGamalParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.getInput("Enter large prime number p: "));
        BigInteger g = new BigInteger(ui.getInput("Enter primitive root g: "));
        BigInteger x = new BigInteger(ui.getInput("Enter private key x: "));

        BigInteger publicKey = elGamalService.calculatePublicKey(g, x, p);
        ui.displayInfo("Calculated public key: " + publicKey);

        return new ElGamalParameters(p, g, x, publicKey);
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return EL_GAMAL;
    }
}
