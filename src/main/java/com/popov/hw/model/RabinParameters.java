package com.popov.hw.model;

import lombok.Builder;

import java.math.BigInteger;

@Builder
public record RabinParameters(
        BigInteger p,
        BigInteger q,
        BigInteger n
) {
}
