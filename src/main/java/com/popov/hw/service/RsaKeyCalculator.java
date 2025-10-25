package com.popov.hw.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RsaKeyCalculator {

    public BigInteger calculateModulus(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

    public BigInteger calculatePrivateKey(BigInteger e, BigInteger p, BigInteger q) {
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        return e.modInverse(phi);
    }
}
