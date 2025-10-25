package com.popov.hw.service.crypto;

import com.popov.hw.model.ElGamalParameters;
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
public class ElGamalCryptoService implements CryptoService {

    private final SecureRandom random = new SecureRandom();

    @Override
    public void encrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        ElGamalParameters params = (ElGamalParameters) parameters;
        encryptFile(inputPath, outputPath, params.p(), params.g(), params.publicKey());
    }

    @Override
    public void decrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        ElGamalParameters params = (ElGamalParameters) parameters;
        decryptFile(inputPath, outputPath, params.p(), params.x());
    }

    private void encryptFile(String inputPath, String outputPath, BigInteger p, BigInteger g, BigInteger publicKey) throws Exception {
        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (p.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);
                BigInteger k = generateSessionKey(p);
                BigInteger a = g.modPow(k, p);
                BigInteger b = publicKey.modPow(k, p).multiply(message).mod(p);

                writeEncryptedPair(dos, a, b);
            }
        }
        log.info("ElGamal encryption completed");
    }

    private void decryptFile(String inputPath, String outputPath, BigInteger p, BigInteger privateKey) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                BigInteger a = readBigInteger(dis);
                BigInteger b = readBigInteger(dis);

                BigInteger aToX = a.modPow(privateKey, p);
                BigInteger aToXInverse = aToX.modInverse(p);
                BigInteger message = b.multiply(aToXInverse).mod(p);

                dos.write(removeLeadingZero(message.toByteArray()));
            }
        }
        log.info("ElGamal decryption completed");
    }

    private BigInteger generateSessionKey(BigInteger p) {
        BigInteger pMinusTwo = p.subtract(BigInteger.TWO);
        BigInteger k;
        do {
            k = new BigInteger(p.bitLength(), random);
        } while (k.compareTo(BigInteger.ONE) <= 0 || k.compareTo(pMinusTwo) > 0);
        return k;
    }

    private void writeEncryptedPair(DataOutputStream dos, BigInteger a, BigInteger b) throws Exception {
        byte[] aBytes = a.toByteArray();
        byte[] bBytes = b.toByteArray();
        dos.writeInt(aBytes.length);
        dos.write(aBytes);
        dos.writeInt(bBytes.length);
        dos.write(bBytes);
    }

    private BigInteger readBigInteger(DataInputStream dis) throws Exception {
        int length = dis.readInt();
        byte[] bytes = new byte[length];
        dis.readFully(bytes);
        return new BigInteger(bytes);
    }

    private byte[] removeLeadingZero(byte[] data) {
        if (data[0] == 0 && data.length > 1) {
            byte[] result = new byte[data.length - 1];
            System.arraycopy(data, 1, result, 0, result.length);
            return result;
        }
        return data;
    }
}
