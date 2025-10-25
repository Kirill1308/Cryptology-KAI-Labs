package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.ParameterCollector;
import com.popov.hw.model.ShamirParameters;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.SHAMIR;

@Component
@RequiredArgsConstructor
public class ShamirParameterCollector implements ParameterCollector<ShamirParameters> {

    @Override
    public ShamirParameters collectParameters(UserInterface ui) {
        BigInteger p = new BigInteger(ui.promptInput("shamir.enter.prime"));
        BigInteger ca = new BigInteger(ui.promptInput("shamir.enter.key.a"));
        BigInteger cb = new BigInteger(ui.promptInput("shamir.enter.key.b"));

        return ShamirParameters.builder()
                .p(p)
                .keyPair(new BigInteger[]{ca, cb})
                .build();
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return SHAMIR;
    }
}
