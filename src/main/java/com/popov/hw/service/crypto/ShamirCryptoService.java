package com.popov.hw.service.crypto;

import com.popov.hw.model.ShamirParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class ShamirCryptoService implements CryptoService {

    @Override
    public void encrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        ShamirParameters params = (ShamirParameters) parameters;
        encryptFile(inputPath, outputPath, params.p(), params.keyPair()[0]);
    }

    @Override
    public void decrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        ShamirParameters params = (ShamirParameters) parameters;
        decryptFile(inputPath, outputPath, params.p(), params.keyPair()[0]);
    }

    private void encryptFile(String inputPath, String outputPath, BigInteger p, BigInteger cA) throws Exception {
        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (p.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);
                BigInteger x1 = message.modPow(cA, p);

                byte[] x1Bytes = x1.toByteArray();
                dos.writeInt(x1Bytes.length);
                dos.write(x1Bytes);
            }
        }
        log.info("Shamir encryption completed");
    }

    private void decryptFile(String inputPath, String outputPath, BigInteger p, BigInteger dA) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] encryptedBytes = new byte[length];
                dis.readFully(encryptedBytes);
                BigInteger encrypted = new BigInteger(encryptedBytes);

                BigInteger message = encrypted.modPow(dA, p);
                byte[] result = message.toByteArray();

                if (result[0] == 0 && result.length > 1) {
                    byte[] temp = new byte[result.length - 1];
                    System.arraycopy(result, 1, temp, 0, temp.length);
                    result = temp;
                }

                dos.write(result);
            }
        }
        log.info("Shamir decryption completed");
    }
}
