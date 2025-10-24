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
public class ShamirService implements CryptoService {

    private final SecureRandom random = new SecureRandom();

    /**
     * Encrypts file using Shamir's protocol (sender's perspective)
     * This is a simplified version that stores intermediate values
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = p (large prime), [1] = cA (sender's encryption key), [2] = dA (sender's decryption key)
     */
    @Override
    public void encryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 3) {
            throw new IllegalArgumentException("Shamir encryption requires: p, cA, dA");
        }

        BigInteger p = (BigInteger) params[0];
        BigInteger cA = (BigInteger) params[1];
        BigInteger dA = (BigInteger) params[2];

        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (p.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);

                // Step 1: x1 = M^cA mod p (sender encrypts)
                BigInteger x1 = message.modPow(cA, p);

                byte[] x1Bytes = x1.toByteArray();
                dos.writeInt(x1Bytes.length);
                dos.write(x1Bytes);
            }
        }
        log.info("File encrypted successfully using Shamir's protocol (Step 1)");
    }

    /**
     * Decrypts file using Shamir's protocol (final step)
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = p (large prime), [1] = dA (sender's decryption key)
     */
    @Override
    public void decryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("Shamir decryption requires: p, dA");
        }

        BigInteger p = (BigInteger) params[0];
        BigInteger dA = (BigInteger) params[1];

        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] encryptedBytes = new byte[length];
                dis.readFully(encryptedBytes);
                BigInteger encrypted = new BigInteger(encryptedBytes);

                // Step 3: M = C^dA mod p (sender's final decryption)
                BigInteger message = encrypted.modPow(dA, p);

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
        log.info("File decrypted successfully using Shamir's protocol (Step 3)");
    }

    /**
     * Intermediate step - receiver's encryption (Step 2)
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param p          large prime
     * @param cB         receiver's encryption key
     * @param dB         receiver's decryption key
     */
    public void receiverProcess(String inputPath, String outputPath, BigInteger p, BigInteger cB, BigInteger dB) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] x1Bytes = new byte[length];
                dis.readFully(x1Bytes);
                BigInteger x1 = new BigInteger(x1Bytes);

                // Step 2: x2 = x1^cB mod p (receiver encrypts)
                // Then: C = x2^dB mod p (receiver decrypts their layer)
                BigInteger x2 = x1.modPow(cB, p);
                BigInteger c = x2.modPow(dB, p);

                byte[] cBytes = c.toByteArray();
                dos.writeInt(cBytes.length);
                dos.write(cBytes);
            }
        }
        log.info("Receiver processing completed (Steps 2 and combined)");
    }

    /**
     * Generates encryption and decryption key pair
     *
     * @param p large prime
     * @return array [c, d] where c*d ≡ 1 (mod p-1)
     */
    public BigInteger[] generateKeyPair(BigInteger p) {
        BigInteger phi = p.subtract(BigInteger.ONE);
        BigInteger c;

        do {
            c = new BigInteger(p.bitLength() - 1, random);
        } while (c.compareTo(BigInteger.ONE) <= 0 || c.compareTo(phi) >= 0 || !c.gcd(phi).equals(BigInteger.ONE));

        BigInteger d = c.modInverse(phi);
        return new BigInteger[]{c, d};
    }
}
