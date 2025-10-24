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
public class RsaService implements CryptoService {

    public BigInteger calculatePrivateKey(BigInteger e, BigInteger p, BigInteger q) {
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        return e.modInverse(phi);
    }

    @Override
    public void encryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("RSA encryption requires: e, n");
        }
        BigInteger e = (BigInteger) params[0];
        BigInteger n = (BigInteger) params[1];
        encryptFileWithParams(inputPath, outputPath, e, n);
    }

    public void encryptFileWithParams(String inputPath, String outputPath, BigInteger e, BigInteger n) throws Exception {
        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (n.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);
                BigInteger encrypted = message.modPow(e, n);
                byte[] encryptedBytes = encrypted.toByteArray();

                dos.writeInt(encryptedBytes.length);
                dos.write(encryptedBytes);
            }
        }
        log.info("File encrypted successfully");
    }

    @Override
    public void decryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("RSA decryption requires: d, n");
        }
        BigInteger d = (BigInteger) params[0];
        BigInteger n = (BigInteger) params[1];
        decryptFileWithParams(inputPath, outputPath, d, n);
    }

    public void decryptFileWithParams(String inputPath, String outputPath, BigInteger d, BigInteger n) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] encryptedBytes = new byte[length];
                dis.readFully(encryptedBytes);

                BigInteger encrypted = new BigInteger(encryptedBytes);
                BigInteger decrypted = encrypted.modPow(d, n);
                byte[] result = decrypted.toByteArray();

                if (result[0] == 0 && result.length > 1) {
                    byte[] temp = new byte[result.length - 1];
                    System.arraycopy(result, 1, temp, 0, temp.length);
                    result = temp;
                }

                dos.write(result);
            }
        }
        log.info("File decrypted successfully");
    }
}
