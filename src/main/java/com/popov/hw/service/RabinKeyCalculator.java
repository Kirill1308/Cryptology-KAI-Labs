package com.popov.hw.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RabinKeyCalculator {

    public BigInteger calculateModulus(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
}
