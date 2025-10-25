package com.popov.hw.service.crypto;

import com.popov.hw.model.RabinParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class RabinCryptoService implements CryptoService {

    @Override
    public void encrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        RabinParameters params = (RabinParameters) parameters;
        encryptFile(inputPath, outputPath, params.n());
    }

    @Override
    public void decrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        RabinParameters params = (RabinParameters) parameters;
        decryptFile(inputPath, outputPath, params.p(), params.q(), params.n());
    }

    private void encryptFile(String inputPath, String outputPath, BigInteger n) throws Exception {
        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (n.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger message = new BigInteger(1, block);
                BigInteger encrypted = message.modPow(BigInteger.TWO, n);

                byte[] encryptedBytes = encrypted.toByteArray();
                dos.writeInt(encryptedBytes.length);
                dos.write(encryptedBytes);
            }
        }
        log.info("Rabin encryption completed");
    }

    private void decryptFile(String inputPath, String outputPath, BigInteger p, BigInteger q, BigInteger n) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                int length = dis.readInt();
                byte[] encryptedBytes = new byte[length];
                dis.readFully(encryptedBytes);
                BigInteger c = new BigInteger(encryptedBytes);

                BigInteger message = decryptRabin(c, p, q, n);
                byte[] result = message.toByteArray();

                if (result[0] == 0 && result.length > 1) {
                    byte[] temp = new byte[result.length - 1];
                    System.arraycopy(result, 1, temp, 0, temp.length);
                    result = temp;
                }

                dos.write(result);
            }
        }
        log.info("Rabin decryption completed");
    }

    private BigInteger decryptRabin(BigInteger c, BigInteger p, BigInteger q, BigInteger n) {
        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
            !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            throw new IllegalArgumentException("p and q must be ≡ 3 (mod 4)");
        }

        BigInteger mp = c.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
        BigInteger mq = c.modPow(q.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), q);

        BigInteger[] ext = extendedGCD(p, q);
        BigInteger yp = ext[1];
        BigInteger yq = ext[2];

        BigInteger r1 = yp.multiply(p).multiply(mq).add(yq.multiply(q).multiply(mp)).mod(n);
        BigInteger r2 = n.subtract(r1);
        BigInteger r3 = yp.multiply(p).multiply(mq).subtract(yq.multiply(q).multiply(mp)).mod(n);
        BigInteger r4 = n.subtract(r3);

        return findSmallestPositive(r1, r2, r3, r4);
    }

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

    private BigInteger findSmallestPositive(BigInteger... values) {
        BigInteger min = null;
        for (BigInteger value : values) {
            if (value.compareTo(BigInteger.ZERO) > 0 && (min == null || value.compareTo(min) < 0)) {
                min = value;
            }
        }
        return min;
    }
}
