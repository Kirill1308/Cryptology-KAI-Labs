package com.popov.hw.service;

import com.popov.hw.service.crypto.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class RabinService implements CryptoService {

    /**
     * Encrypts file using Rabin algorithm
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = n (n = p * q)
     */
    @Override
    public void encryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 1) {
            throw new IllegalArgumentException("Rabin encryption requires: n (p*q)");
        }

        BigInteger n = (BigInteger) params[0];

        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (n.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);

                // Encryption: C = M^2 mod n
                BigInteger encrypted = message.modPow(BigInteger.TWO, n);

                byte[] encryptedBytes = encrypted.toByteArray();
                dos.writeInt(encryptedBytes.length);
                dos.write(encryptedBytes);
            }
        }
        log.info("File encrypted successfully using Rabin");
    }

    /**
     * Decrypts file using Rabin algorithm
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = p, [1] = q (prime factors of n)
     */
    @Override
    public void decryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("Rabin decryption requires: p, q");
        }

        BigInteger p = (BigInteger) params[0];
        BigInteger q = (BigInteger) params[1];
        BigInteger n = p.multiply(q);

        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] encryptedBytes = new byte[length];
                dis.readFully(encryptedBytes);
                BigInteger c = new BigInteger(encryptedBytes);

                // Decrypt using Chinese Remainder Theorem
                BigInteger message = decryptRabin(c, p, q, n);

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
        log.info("File decrypted successfully using Rabin");
    }

    /**
     * Decrypts Rabin ciphertext using Chinese Remainder Theorem
     * Note: Rabin decryption produces 4 possible plaintexts, we select the smallest positive one
     */
    private BigInteger decryptRabin(BigInteger c, BigInteger p, BigInteger q, BigInteger n) {
        // For p ≡ 3 (mod 4) and q ≡ 3 (mod 4)
        if (p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) &&
            q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {

            // Calculate square roots
            BigInteger mp = c.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
            BigInteger mq = c.modPow(q.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), q);

            // Extended Euclidean algorithm
            BigInteger[] ext = extendedGCD(p, q);
            BigInteger yp = ext[1];
            BigInteger yq = ext[2];

            // Calculate four possible square roots using CRT
            BigInteger r1 = yp.multiply(p).multiply(mq).add(yq.multiply(q).multiply(mp)).mod(n);
            BigInteger r2 = n.subtract(r1);
            BigInteger r3 = yp.multiply(p).multiply(mq).subtract(yq.multiply(q).multiply(mp)).mod(n);
            BigInteger r4 = n.subtract(r3);

            // Return the smallest positive root (most likely the original message)
            BigInteger min = r1;
            if (r2.compareTo(BigInteger.ZERO) > 0 && r2.compareTo(min) < 0) min = r2;
            if (r3.compareTo(BigInteger.ZERO) > 0 && r3.compareTo(min) < 0) min = r3;
            if (r4.compareTo(BigInteger.ZERO) > 0 && r4.compareTo(min) < 0) min = r4;

            return min;
        } else {
            throw new IllegalArgumentException("For this implementation, p and q must be ≡ 3 (mod 4)");
        }
    }

    /**
     * Extended Euclidean algorithm
     * Returns [gcd, x, y] such that ax + by = gcd(a, b)
     */
    private BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }

        BigInteger[] result = extendedGCD(b, a.mod(b));
        BigInteger gcd = result[0];
        BigInteger x = result[2];
        BigInteger y = result[1].subtract(a.divide(b).multiply(result[2]));

        return new BigInteger[]{gcd, x, y};
    }

}
