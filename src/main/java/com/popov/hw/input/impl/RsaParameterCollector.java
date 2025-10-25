package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.ParameterCollector;
import com.popov.hw.model.RsaParameters;
import com.popov.hw.service.RsaKeyCalculator;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.RSA;

@Component
@RequiredArgsConstructor
public class RsaParameterCollector implements ParameterCollector<RsaParameters> {

    private final RsaKeyCalculator keyCalculator;

    @Override
    public RsaParameters collectParameters(UserInterface ui) {
        BigInteger e = new BigInteger(ui.promptInput("rsa.enter.public.key"));
        BigInteger p = new BigInteger(ui.promptInput("rsa.enter.prime.p"));
        BigInteger q = new BigInteger(ui.promptInput("rsa.enter.prime.q"));

        BigInteger n = keyCalculator.calculateModulus(p, q);
        BigInteger d = keyCalculator.calculatePrivateKey(e, p, q);

        ui.displayInfo("rsa.calculated.private.key", d);
        ui.displayInfo("rsa.modulus", n);

        return RsaParameters.builder()
                .e(e)
                .p(p)
                .q(q)
                .n(n)
                .d(d)
                .build();
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return RSA;
    }
}
