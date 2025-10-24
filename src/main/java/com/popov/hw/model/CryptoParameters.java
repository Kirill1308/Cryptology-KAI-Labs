package com.popov.hw.model;

import java.math.BigInteger;

public class CryptoParameters {

    public record RsaParameters(BigInteger e, BigInteger p, BigInteger q, BigInteger n, BigInteger d) {
    }

    public record ElGamalParameters(BigInteger p, BigInteger g, BigInteger x, BigInteger publicKey) {
    }

    public record ShamirParameters(BigInteger p, BigInteger[] keyPair) {
    }

    public record RabinParameters(BigInteger p, BigInteger q, BigInteger n) {
    }

    public record EllipticCurveParameters(BigInteger privateKey, Object publicKey, Object curve) {
    }
}
