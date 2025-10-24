package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import com.popov.hw.service.RsaService;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.RSA;
import static com.popov.hw.model.CryptoParameters.RsaParameters;

@Component
@RequiredArgsConstructor
public class RsaInputHandler implements InputHandler<RsaParameters> {

    private final RsaService rsaService;

    @Override
    public RsaParameters collectParameters(UserInterface ui) {
        BigInteger e = new BigInteger(ui.getInput("Enter public key e: "));
        BigInteger p = new BigInteger(ui.getInput("Enter prime number p: "));
        BigInteger q = new BigInteger(ui.getInput("Enter prime number q: "));

        BigInteger n = p.multiply(q);
        BigInteger d = rsaService.calculatePrivateKey(e, p, q);

        ui.displayInfo("Calculated private key d: " + d);
        ui.displayInfo("Modulus n: " + n);

        return new RsaParameters(e, p, q, n, d);
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RSA;
    }
}
