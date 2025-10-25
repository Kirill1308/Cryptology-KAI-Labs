package com.popov.hw.service;

import com.popov.hw.model.ECCurve;
import com.popov.hw.model.ECPoint;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class EllipticCurveKeyCalculator {

    public ECPoint calculatePublicKey(BigInteger privateKey, ECPoint basePoint, ECCurve curve) {
        return multiplyPoint(basePoint, privateKey, curve);
    }

    private ECPoint multiplyPoint(ECPoint point, BigInteger k, ECCurve curve) {
        if (k.equals(BigInteger.ZERO)) {
            return null;
        }

        ECPoint result = null;
        ECPoint addend = point;

        while (k.compareTo(BigInteger.ZERO) > 0) {
            if (k.testBit(0)) {
                result = addPoints(result, addend, curve);
            }
            addend = addPoints(addend, addend, curve);
            k = k.shiftRight(1);
        }

        return result;
    }

    private ECPoint addPoints(ECPoint p1, ECPoint p2, ECCurve curve) {
        if (p1 == null) return p2;
        if (p2 == null) return p1;

        BigInteger lambda;
        if (p1.x().equals(p2.x()) && p1.y().equals(p2.y())) {
            BigInteger numerator = BigInteger.valueOf(3)
                    .multiply(p1.x().pow(2))
                    .add(curve.a())
                    .mod(curve.p());
            BigInteger denominator = BigInteger.TWO.multiply(p1.y()).modInverse(curve.p());
            lambda = numerator.multiply(denominator).mod(curve.p());
        } else {
            BigInteger numerator = p2.y().subtract(p1.y()).mod(curve.p());
            BigInteger denominator = p2.x().subtract(p1.x()).modInverse(curve.p());
            lambda = numerator.multiply(denominator).mod(curve.p());
        }

        BigInteger x3 = lambda.pow(2).subtract(p1.x()).subtract(p2.x()).mod(curve.p());
        BigInteger y3 = lambda.multiply(p1.x().subtract(x3)).subtract(p1.y()).mod(curve.p());

        return new ECPoint(x3, y3);
    }
}
