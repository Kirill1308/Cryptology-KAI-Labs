package com.popov.hw.model;

import lombok.Builder;

import java.math.BigInteger;

@Builder
public record ShamirParameters(
        BigInteger p,
        BigInteger[] keyPair
) {
}
