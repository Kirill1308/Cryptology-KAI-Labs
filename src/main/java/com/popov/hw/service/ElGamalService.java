package com.popov.hw.service;

import com.popov.hw.service.crypto.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;

@Slf4j
@Service
public class ElGamalService implements CryptoService {

    private final SecureRandom random = new SecureRandom();

    /**
     * Encrypts file using El-Gamal algorithm
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = p (large prime), [1] = g (primitive root), [2] = publicKey (g^x mod p)
     */
    @Override
    public void encryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 3) {
            throw new IllegalArgumentException("El-Gamal encryption requires: p, g, publicKey");
        }

        BigInteger p = (BigInteger) params[0];
        BigInteger g = (BigInteger) params[1];
        BigInteger publicKey = (BigInteger) params[2];

        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (p.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);

                // Generate random session key k: 1 < k < p-1
                BigInteger pMinusTwo = p.subtract(BigInteger.TWO);
                BigInteger k;
                do {
                    k = new BigInteger(p.bitLength(), random);
                } while (k.compareTo(BigInteger.ONE) <= 0 || k.compareTo(pMinusTwo) > 0);

                // Calculate a = g^k mod p
                BigInteger a = g.modPow(k, p);

                // Calculate b = (publicKey^k * message) mod p
                BigInteger b = publicKey.modPow(k, p).multiply(message).mod(p);

                // Write encrypted pair (a, b)
                byte[] aBytes = a.toByteArray();
                byte[] bBytes = b.toByteArray();

                dos.writeInt(aBytes.length);
                dos.write(aBytes);
                dos.writeInt(bBytes.length);
                dos.write(bBytes);
            }
        }
        log.info("File encrypted successfully using El-Gamal");
    }

    /**
     * Decrypts file using El-Gamal algorithm
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = p (large prime), [1] = privateKey (x)
     */
    @Override
    public void decryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("El-Gamal decryption requires: p, privateKey");
        }

        BigInteger p = (BigInteger) params[0];
        BigInteger privateKey = (BigInteger) params[1];

        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                // Read a
                int aLength = dis.readInt();
                byte[] aBytes = new byte[aLength];
                dis.readFully(aBytes);
                BigInteger a = new BigInteger(aBytes);

                // Read b
                int bLength = dis.readInt();
                byte[] bBytes = new byte[bLength];
                dis.readFully(bBytes);
                BigInteger b = new BigInteger(bBytes);

                // Calculate message = b * (a^x)^(-1) mod p = b * a^(-x) mod p
                BigInteger aToX = a.modPow(privateKey, p);
                BigInteger aToXInverse = aToX.modInverse(p);
                BigInteger message = b.multiply(aToXInverse).mod(p);

                byte[] result = message.toByteArray();

                // Remove leading zero byte if present
                if (result[0] == 0 && result.length > 1) {
                    byte[] temp = new byte[result.length - 1];
                    System.arraycopy(result, 1, temp, 0, temp.length);
                    result = temp;
                }

                dos.write(result);
            }
        }
        log.info("File decrypted successfully using El-Gamal");
    }

    /**
     * Calculates public key from private key
     *
     * @param g primitive root
     * @param x private key
     * @param p large prime
     * @return public key = g^x mod p
     */
    public BigInteger calculatePublicKey(BigInteger g, BigInteger x, BigInteger p) {
        return g.modPow(x, p);
    }
}
