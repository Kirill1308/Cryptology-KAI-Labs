package com.popov.hw.model;

import lombok.Builder;

import java.math.BigInteger;

@Builder
public record RsaParameters(
        BigInteger e,
        BigInteger p,
        BigInteger q,
        BigInteger n,
        BigInteger d
) {
}
