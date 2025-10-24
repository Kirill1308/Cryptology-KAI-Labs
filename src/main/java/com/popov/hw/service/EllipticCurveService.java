package com.popov.hw.service;

import com.popov.hw.service.crypto.CryptoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;

/**
 * Elliptic Curve Cryptography service
 * Based on elliptic curve: y^2 = x^3 + ax + b (mod p)
 */
@Slf4j
@Service
public class EllipticCurveService implements CryptoService {

    private final SecureRandom random = new SecureRandom();

    @Data
    @AllArgsConstructor
    public static class ECPoint {
        private BigInteger x;
        private BigInteger y;
        private boolean infinity; // Point at infinity

        public ECPoint(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
            this.infinity = false;
        }

        public static ECPoint infinity() {
            return new ECPoint(null, null, true);
        }
    }

    @Data
    @AllArgsConstructor
    public static class ECParameters {
        private BigInteger a;
        private BigInteger b;
        private BigInteger p;
        private ECPoint g; // Generator point
    }

    /**
     * Encrypts file using El-Gamal on Elliptic Curve
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = ECParameters, [1] = publicKey (ECPoint)
     */
    @Override
    public void encryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("EC encryption requires: ECParameters, publicKey");
        }

        ECParameters curve = (ECParameters) params[0];
        ECPoint publicKey = (ECPoint) params[1];

        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (curve.p.bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger messageValue = new BigInteger(1, block);

                // Encode message as a point on the curve (simplified: use x-coordinate)
                ECPoint messagePoint = new ECPoint(messageValue, BigInteger.ZERO);

                // Generate random k
                BigInteger k = new BigInteger(curve.p.bitLength() - 1, random);
                while (k.compareTo(BigInteger.ONE) <= 0) {
                    k = new BigInteger(curve.p.bitLength() - 1, random);
                }

                // C1 = k*G
                ECPoint c1 = multiplyPoint(curve.g, k, curve);

                // C2 = M + k*publicKey
                ECPoint kPublicKey = multiplyPoint(publicKey, k, curve);
                ECPoint c2 = addPoints(messagePoint, kPublicKey, curve);

                // Write encrypted pair (C1, C2)
                writePoint(dos, c1);
                writePoint(dos, c2);
            }
        }
        log.info("File encrypted successfully using Elliptic Curve");
    }

    /**
     * Decrypts file using El-Gamal on Elliptic Curve
     *
     * @param inputPath  path to input file
     * @param outputPath path to output file
     * @param params     [0] = ECParameters, [1] = privateKey (BigInteger)
     */
    @Override
    public void decryptFile(String inputPath, String outputPath, Object... params) throws Exception {
        if (params.length < 2) {
            throw new IllegalArgumentException("EC decryption requires: ECParameters, privateKey");
        }

        ECParameters curve = (ECParameters) params[0];
        BigInteger privateKey = (BigInteger) params[1];

        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                // Read C1
                ECPoint c1 = readPoint(dis);

                // Read C2
                ECPoint c2 = readPoint(dis);

                // M = C2 - privateKey*C1
                ECPoint privC1 = multiplyPoint(c1, privateKey, curve);
                ECPoint negPrivC1 = new ECPoint(privC1.x, curve.p.subtract(privC1.y).mod(curve.p));
                ECPoint messagePoint = addPoints(c2, negPrivC1, curve);

                // Extract message from point (x-coordinate)
                BigInteger message = messagePoint.x;
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
        log.info("File decrypted successfully using Elliptic Curve");
    }

    /**
     * Adds two points on an elliptic curve
     */
    private ECPoint addPoints(ECPoint p1, ECPoint p2, ECParameters curve) {
        if (p1.infinity) return p2;
        if (p2.infinity) return p1;

        if (p1.x.equals(p2.x)) {
            if (p1.y.equals(p2.y)) {
                return doublePoint(p1, curve);
            } else {
                return ECPoint.infinity();
            }
        }

        // λ = (y2 - y1) / (x2 - x1) mod p
        BigInteger numerator = p2.y.subtract(p1.y).mod(curve.p);
        BigInteger denominator = p2.x.subtract(p1.x).mod(curve.p);
        BigInteger lambda = numerator.multiply(denominator.modInverse(curve.p)).mod(curve.p);

        // x3 = λ^2 - x1 - x2 mod p
        BigInteger x3 = lambda.pow(2).subtract(p1.x).subtract(p2.x).mod(curve.p);

        // y3 = λ(x1 - x3) - y1 mod p
        BigInteger y3 = lambda.multiply(p1.x.subtract(x3)).subtract(p1.y).mod(curve.p);

        return new ECPoint(x3, y3);
    }

    /**
     * Doubles a point on an elliptic curve
     */
    private ECPoint doublePoint(ECPoint p, ECParameters curve) {
        if (p.infinity) return p;

        // λ = (3x^2 + a) / (2y) mod p
        BigInteger numerator = p.x.pow(2).multiply(BigInteger.valueOf(3)).add(curve.a).mod(curve.p);
        BigInteger denominator = p.y.multiply(BigInteger.TWO).mod(curve.p);
        BigInteger lambda = numerator.multiply(denominator.modInverse(curve.p)).mod(curve.p);

        // x3 = λ^2 - 2x mod p
        BigInteger x3 = lambda.pow(2).subtract(p.x.multiply(BigInteger.TWO)).mod(curve.p);

        // y3 = λ(x - x3) - y mod p
        BigInteger y3 = lambda.multiply(p.x.subtract(x3)).subtract(p.y).mod(curve.p);

        return new ECPoint(x3, y3);
    }

    /**
     * Multiplies a point by a scalar using double-and-add algorithm
     */
    private ECPoint multiplyPoint(ECPoint point, BigInteger scalar, ECParameters curve) {
        if (scalar.equals(BigInteger.ZERO)) {
            return ECPoint.infinity();
        }

        ECPoint result = ECPoint.infinity();
        ECPoint addend = point;

        while (scalar.compareTo(BigInteger.ZERO) > 0) {
            if (scalar.testBit(0)) {
                result = addPoints(result, addend, curve);
            }
            addend = doublePoint(addend, curve);
            scalar = scalar.shiftRight(1);
        }

        return result;
    }

    /**
     * Writes an ECPoint to output stream
     */
    private void writePoint(DataOutputStream dos, ECPoint point) throws Exception {
        dos.writeBoolean(point.infinity);
        if (!point.infinity) {
            byte[] xBytes = point.x.toByteArray();
            byte[] yBytes = point.y.toByteArray();
            dos.writeInt(xBytes.length);
            dos.write(xBytes);
            dos.writeInt(yBytes.length);
            dos.write(yBytes);
        }
    }

    /**
     * Reads an ECPoint from input stream
     */
    private ECPoint readPoint(DataInputStream dis) throws Exception {
        boolean infinity = dis.readBoolean();
        if (infinity) {
            return ECPoint.infinity();
        }

        int xLength = dis.readInt();
        byte[] xBytes = new byte[xLength];
        dis.readFully(xBytes);
        BigInteger x = new BigInteger(xBytes);

        int yLength = dis.readInt();
        byte[] yBytes = new byte[yLength];
        dis.readFully(yBytes);
        BigInteger y = new BigInteger(yBytes);

        return new ECPoint(x, y);
    }

    /**
     * Calculates public key from private key
     *
     * @param privateKey private key
     * @param curve      elliptic curve parameters
     * @return publicKey = privateKey * G
     */
    public ECPoint calculatePublicKey(BigInteger privateKey, ECParameters curve) {
        return multiplyPoint(curve.g, privateKey, curve);
    }
}
