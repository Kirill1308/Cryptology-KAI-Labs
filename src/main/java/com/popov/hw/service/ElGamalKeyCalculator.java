package com.popov.hw.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ElGamalKeyCalculator {

    public BigInteger calculatePublicKey(BigInteger g, BigInteger x, BigInteger p) {
        return g.modPow(x, p);
    }
}
