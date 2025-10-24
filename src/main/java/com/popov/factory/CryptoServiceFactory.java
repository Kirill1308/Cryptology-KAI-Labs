package com.popov.factory;

import com.popov.service.*;
import com.popov.service.crypto.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory pattern implementation for creating crypto services
 */
@Component
@RequiredArgsConstructor
public class CryptoServiceFactory {

    private final RsaService rsaService;
    private final ElGamalService elGamalService;
    private final ShamirService shamirService;
    private final RabinService rabinService;
    private final EllipticCurveService ellipticCurveService;

    /**
     * Creates appropriate crypto service based on algorithm name
     * @param algorithm algorithm name (RSA, ElGamal, Shamir, Rabin, EllipticCurve)
     * @return crypto service instance
     * @throws IllegalArgumentException if algorithm is not supported
     */
    public CryptoService createService(String algorithm) {
        return switch (algorithm.toLowerCase()) {
            case "rsa", "1" -> rsaService;
            case "elgamal", "el-gamal", "2" -> elGamalService;
            case "shamir", "3" -> shamirService;
            case "rabin", "4" -> rabinService;
            case "ellipticcurve", "elliptic-curve", "ec", "5" -> ellipticCurveService;
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        };
    }
}
