package com.popov.hw.model;

import lombok.Builder;

import java.math.BigInteger;

@Builder
public record ElGamalParameters(
        BigInteger p,
        BigInteger g,
        BigInteger x,
        BigInteger publicKey
) {
}
