package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.ParameterCollector;
import com.popov.hw.model.ElGamalParameters;
import com.popov.hw.service.ElGamalKeyCalculator;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.EL_GAMAL;

@Component
@RequiredArgsConstructor
public class ElGamalParameterCollector implements ParameterCollector<ElGamalParameters> {

    private final ElGamalKeyCalculator keyCalculator;

    @Override
    public ElGamalParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.promptInput("elgamal.enter.prime"));
        BigInteger g = new BigInteger(ui.promptInput("elgamal.enter.generator"));
        BigInteger x = new BigInteger(ui.promptInput("elgamal.enter.private.key"));

        BigInteger publicKey = keyCalculator.calculatePublicKey(g, x, p);

        ui.displayInfo("elgamal.calculated.public.key", publicKey);

        return ElGamalParameters.builder()
                .p(p)
                .g(g)
                .x(x)
                .publicKey(publicKey)
                .build();
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return EL_GAMAL;
    }
}
