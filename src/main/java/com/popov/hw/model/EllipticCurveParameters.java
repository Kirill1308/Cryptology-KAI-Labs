package com.popov.hw.model;

import lombok.Builder;

import java.math.BigInteger;

@Builder
public record EllipticCurveParameters(
        BigInteger privateKey,
        ECPoint publicKey,
        ECCurve curve
) {
}
