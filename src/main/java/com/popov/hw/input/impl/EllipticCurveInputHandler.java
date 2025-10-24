package com.popov.hw.input.impl;

import com.popov.hw.enums.CryptoAlgorithm;
import com.popov.hw.input.InputHandler;
import com.popov.hw.service.EllipticCurveService;
import com.popov.hw.ui.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.popov.hw.enums.CryptoAlgorithm.ELLIPTIC_CURVE;
import static com.popov.hw.model.CryptoParameters.EllipticCurveParameters;

@Component
@RequiredArgsConstructor
public class EllipticCurveInputHandler implements InputHandler<EllipticCurveParameters> {

    private final EllipticCurveService ellipticCurveService;

    @Override
    public EllipticCurveParameters collectParameters(UserInterface ui) {
        ui.displayInfo("Using standard curve: p=751, a=-1, b=1, G=(0,1)");

        BigInteger p = BigInteger.valueOf(751);
        BigInteger a = BigInteger.valueOf(-1);
        BigInteger b = BigInteger.ONE;
        EllipticCurveService.ECPoint g = new EllipticCurveService.ECPoint(BigInteger.ZERO, BigInteger.ONE);
        EllipticCurveService.ECParameters curve = new EllipticCurveService.ECParameters(a, b, p, g);

        BigInteger privateKey = new BigInteger(ui.getInput("Enter private key: "));

        EllipticCurveService.ECPoint publicKey = ellipticCurveService.calculatePublicKey(privateKey, curve);
        ui.displayInfo("Calculated public key: (" + publicKey.getX() + ", " + publicKey.getY() + ")");

        return new EllipticCurveParameters(privateKey, publicKey, curve);
    }

    @Override
    public CryptoAlgorithm getSupportedAlgorithm() {
        return ELLIPTIC_CURVE;
    }
}
