package com.popov.hw.model;

import java.math.BigInteger;

public record ECCurve(BigInteger a, BigInteger b, BigInteger p, ECPoint basePoint) {
}
