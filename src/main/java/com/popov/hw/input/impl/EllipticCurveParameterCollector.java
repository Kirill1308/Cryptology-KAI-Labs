package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.ParameterCollector;
import com.popov.hw.model.ECCurve;
import com.popov.hw.model.ECPoint;
import com.popov.hw.model.EllipticCurveParameters;
import com.popov.hw.service.EllipticCurveKeyCalculator;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.ELLIPTIC_CURVE;

@Component
@RequiredArgsConstructor
public class EllipticCurveParameterCollector implements ParameterCollector<EllipticCurveParameters> {

    private final EllipticCurveKeyCalculator keyCalculator;

    @Override
    public EllipticCurveParameters collectParameters(UserInterface ui) {
        BigInteger a = new BigInteger(ui.promptInput("ec.enter.curve.a"));
        BigInteger b = new BigInteger(ui.promptInput("ec.enter.curve.b"));
        BigInteger p = new BigInteger(ui.promptInput("ec.enter.prime"));
        BigInteger baseX = new BigInteger(ui.promptInput("ec.enter.base.point.x"));
        BigInteger baseY = new BigInteger(ui.promptInput("ec.enter.base.point.y"));
        BigInteger privateKey = new BigInteger(ui.promptInput("ec.enter.private.key"));

        ECPoint basePoint = new ECPoint(baseX, baseY);
        ECCurve curve = new ECCurve(a, b, p, basePoint);
        ECPoint publicKey = keyCalculator.calculatePublicKey(privateKey, basePoint, curve);

        ui.displayInfo("ec.calculated.public.key", publicKey.x(), publicKey.y());

        return EllipticCurveParameters.builder()
                .privateKey(privateKey)
                .publicKey(publicKey)
                .curve(curve)
                .build();
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return ELLIPTIC_CURVE;
    }
}
