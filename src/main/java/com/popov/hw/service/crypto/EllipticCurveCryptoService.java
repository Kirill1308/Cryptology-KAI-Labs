package com.popov.hw.service.crypto;

import com.popov.hw.model.ECCurve;
import com.popov.hw.model.ECPoint;
import com.popov.hw.model.EllipticCurveParameters;
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
public class EllipticCurveCryptoService implements CryptoService {

    private final SecureRandom random = new SecureRandom();

    @Override
    public void encrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        EllipticCurveParameters params = (EllipticCurveParameters) parameters;
        encryptFile(inputPath, outputPath, params.curve(), params.publicKey());
    }

    @Override
    public void decrypt(String inputPath, String outputPath, Object parameters) throws Exception {
        EllipticCurveParameters params = (EllipticCurveParameters) parameters;
        decryptFile(inputPath, outputPath, params.curve(), params.privateKey());
    }

    private void encryptFile(String inputPath, String outputPath, ECCurve curve, ECPoint publicKey) throws Exception {
        byte[] data = Files.readAllBytes(Path.of(inputPath));
        int blockSize = (curve.p().bitLength() - 1) / 8;

        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            for (int i = 0; i < data.length; i += blockSize) {
                int length = Math.min(blockSize, data.length - i);
                byte[] block = new byte[length];
                System.arraycopy(data, i, block, 0, length);

                BigInteger messageValue = new BigInteger(1, block);
                ECPoint messagePoint = new ECPoint(messageValue, BigInteger.ZERO);

                BigInteger k = generateRandomScalar(curve.p());
                ECPoint c1 = multiplyPoint(curve.basePoint(), k, curve);
                ECPoint kPublicKey = multiplyPoint(publicKey, k, curve);
                ECPoint c2 = addPoints(messagePoint, kPublicKey, curve);

                writePoint(dos, c1);
                writePoint(dos, c2);
            }
        }
        log.info("Elliptic Curve encryption completed");
    }

    private void decryptFile(String inputPath, String outputPath, ECCurve curve, BigInteger privateKey) throws Exception {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Path.of(inputPath)));
             DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Path.of(outputPath)))) {

            while (dis.available() > 0) {
                ECPoint c1 = readPoint(dis);
                ECPoint c2 = readPoint(dis);

                ECPoint privC1 = multiplyPoint(c1, privateKey, curve);
                ECPoint negPrivC1 = new ECPoint(privC1.x(), curve.p().subtract(privC1.y()).mod(curve.p()));
                ECPoint messagePoint = addPoints(c2, negPrivC1, curve);

                byte[] result = messagePoint.x().toByteArray();
                dos.write(removeLeadingZero(result));
            }
        }
        log.info("Elliptic Curve decryption completed");
    }

    private ECPoint addPoints(ECPoint p1, ECPoint p2, ECCurve curve) {
        if (p1 == null) return p2;
        if (p2 == null) return p1;

        if (p1.x().equals(p2.x())) {
            if (p1.y().equals(p2.y())) {
                return doublePoint(p1, curve);
            } else {
                return null;
            }
        }

        BigInteger numerator = p2.y().subtract(p1.y()).mod(curve.p());
        BigInteger denominator = p2.x().subtract(p1.x()).mod(curve.p());
        BigInteger lambda = numerator.multiply(denominator.modInverse(curve.p())).mod(curve.p());

        BigInteger x3 = lambda.pow(2).subtract(p1.x()).subtract(p2.x()).mod(curve.p());
        BigInteger y3 = lambda.multiply(p1.x().subtract(x3)).subtract(p1.y()).mod(curve.p());

        return new ECPoint(x3, y3);
    }

    private ECPoint doublePoint(ECPoint p, ECCurve curve) {
        if (p == null) return null;

        BigInteger numerator = p.x().pow(2).multiply(BigInteger.valueOf(3)).add(curve.a()).mod(curve.p());
        BigInteger denominator = p.y().multiply(BigInteger.TWO).mod(curve.p());
        BigInteger lambda = numerator.multiply(denominator.modInverse(curve.p())).mod(curve.p());

        BigInteger x3 = lambda.pow(2).subtract(p.x().multiply(BigInteger.TWO)).mod(curve.p());
        BigInteger y3 = lambda.multiply(p.x().subtract(x3)).subtract(p.y()).mod(curve.p());

        return new ECPoint(x3, y3);
    }

    private ECPoint multiplyPoint(ECPoint point, BigInteger scalar, ECCurve curve) {
        if (scalar.equals(BigInteger.ZERO) || point == null) {
            return null;
        }

        ECPoint result = null;
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

    private BigInteger generateRandomScalar(BigInteger p) {
        BigInteger k;
        do {
            k = new BigInteger(p.bitLength() - 1, random);
        } while (k.compareTo(BigInteger.ONE) <= 0);
        return k;
    }

    private void writePoint(DataOutputStream dos, ECPoint point) throws Exception {
        if (point == null) {
            dos.writeBoolean(true);
        } else {
            dos.writeBoolean(false);
            byte[] xBytes = point.x().toByteArray();
            byte[] yBytes = point.y().toByteArray();
            dos.writeInt(xBytes.length);
            dos.write(xBytes);
            dos.writeInt(yBytes.length);
            dos.write(yBytes);
        }
    }

    private ECPoint readPoint(DataInputStream dis) throws Exception {
        boolean isNull = dis.readBoolean();
        if (isNull) {
            return null;
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

    private byte[] removeLeadingZero(byte[] data) {
        if (data[0] == 0 && data.length > 1) {
            byte[] result = new byte[data.length - 1];
            System.arraycopy(data, 1, result, 0, result.length);
            return result;
        }
        return data;
    }
}
