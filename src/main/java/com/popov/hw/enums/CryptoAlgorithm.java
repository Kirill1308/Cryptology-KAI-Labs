package com.popov.hw.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CryptoAlgorithm {
    RSA(1, "RSA"),
    EL_GAMAL(2, "El-Gamal"),
    SHAMIR(3, "Shamir"),
    RABIN(4, "Rabin"),
    ELLIPTIC_CURVE(5, "Elliptic Curve");

    private final int number;
    private final String displayName;

    public static CryptoAlgorithm fromNumber(int number) {
        for (CryptoAlgorithm algorithm : values()) {
            if (algorithm.number == number) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("Invalid lab number: " + number);
    }

    public static CryptoAlgorithm fromString(String input) {
        try {
            int number = Integer.parseInt(input.trim());
            return fromNumber(number);
        } catch (NumberFormatException e) {
            for (CryptoAlgorithm algorithm : values()) {
                if (algorithm.name().equalsIgnoreCase(input.trim()) ||
                    algorithm.displayName.equalsIgnoreCase(input.trim())) {
                    return algorithm;
                }
            }
            throw new IllegalArgumentException("Invalid algorithm: " + input);
        }
    }
}
