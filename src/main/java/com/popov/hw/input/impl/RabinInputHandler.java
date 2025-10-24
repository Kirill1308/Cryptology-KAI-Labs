package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import com.popov.hw.ui.UserInterface;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.RABIN;
import static com.popov.hw.model.CryptoParameters.RabinParameters;

@Component
public class RabinInputHandler implements InputHandler<RabinParameters> {

    @Override
    public RabinParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.getInput("Enter prime number p (must be ≡ 3 mod 4): "));
        BigInteger q = new BigInteger(ui.getInput("Enter prime number q (must be ≡ 3 mod 4): "));

        BigInteger n = p.multiply(q);
        ui.displayInfo("Modulus n: " + n);

        return new RabinParameters(p, q, n);
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RABIN;
    }
}
