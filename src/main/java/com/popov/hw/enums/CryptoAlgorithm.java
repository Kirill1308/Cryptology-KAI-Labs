package com.popov.hw.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CryptoAlgorithm {
    RSA(1, "rsa"),
    EL_GAMAL(2, "elgamal"),
    SHAMIR(3, "shamir"),
    RABIN(4, "rabin"),
    ELLIPTIC_CURVE(5, "elliptic.curve");

    private final int number;
    private final String messageKey;

    public static CryptoAlgorithm fromNumber(int number) {
        for (CryptoAlgorithm algorithm : values()) {
            if (algorithm.number == number) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("error.invalid.lab.number");
    }
}
