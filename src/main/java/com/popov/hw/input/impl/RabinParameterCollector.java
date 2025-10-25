package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.ParameterCollector;
import com.popov.hw.model.RabinParameters;
import com.popov.hw.service.RabinKeyCalculator;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.RABIN;

@Component
@RequiredArgsConstructor
public class RabinParameterCollector implements ParameterCollector<RabinParameters> {

    private final RabinKeyCalculator keyCalculator;

    @Override
    public RabinParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.promptInput("rabin.enter.prime.p"));
        BigInteger q = new BigInteger(ui.promptInput("rabin.enter.prime.q"));

        BigInteger n = keyCalculator.calculateModulus(p, q);

        ui.displayInfo("rabin.modulus", n);

        return RabinParameters.builder()
                .p(p)
                .q(q)
                .n(n)
                .build();
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RABIN;
    }
}
