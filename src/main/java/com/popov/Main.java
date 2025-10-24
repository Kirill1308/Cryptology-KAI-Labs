package com.popov;

import com.popov.factory.CryptoServiceFactory;
import com.popov.service.*;
import com.popov.service.crypto.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

    private final CryptoServiceFactory factory;
    private final RsaService rsaService;
    private final ElGamalService elGamalService;
    private final ShamirService shamirService;
    private final RabinService rabinService;
    private final EllipticCurveService ellipticCurveService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Cryptology Labs ===");
        System.out.println("Select lab:");
        System.out.println("1. RSA");
        System.out.println("2. El-Gamal");
        System.out.println("3. Shamir");
        System.out.println("4. Rabin");
        System.out.println("5. Elliptic Curve");
        System.out.print("Enter lab number: ");
        String labNumber = scanner.nextLine();

        CryptoService service = factory.createService(labNumber);
        System.out.println("Selected algorithm: " + service.getAlgorithmName());

        System.out.print("Enter input file path: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();

        System.out.print("Enter cipher type (encrypt/decrypt): ");
        String cipherType = scanner.nextLine();

        try {
            switch (labNumber) {
                case "1" -> handleRsa(scanner, inputFile, outputFile, cipherType);
                case "2" -> handleElGamal(scanner, inputFile, outputFile, cipherType);
                case "3" -> handleShamir(scanner, inputFile, outputFile, cipherType);
                case "4" -> handleRabin(scanner, inputFile, outputFile, cipherType);
                case "5" -> handleEllipticCurve(scanner, inputFile, outputFile, cipherType);
                default -> System.out.println("Invalid lab number");
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleRsa(Scanner scanner, String inputFile, String outputFile, String cipherType) throws Exception {
        System.out.print("Enter public key e: ");
        BigInteger e = new BigInteger(scanner.nextLine());

        System.out.print("Enter prime number p: ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.print("Enter prime number q: ");
        BigInteger q = new BigInteger(scanner.nextLine());

        BigInteger n = p.multiply(q);
        BigInteger d = rsaService.calculatePrivateKey(e, p, q);

        System.out.println("Calculated private key d: " + d);
        System.out.println("Modulus n: " + n);

        if ("encrypt".equalsIgnoreCase(cipherType)) {
            rsaService.encryptFileWithParams(inputFile, outputFile, e, n);
        } else if ("decrypt".equalsIgnoreCase(cipherType)) {
            rsaService.decryptFileWithParams(inputFile, outputFile, d, n);
        } else {
            System.out.println("Invalid cipher type");
        }
    }

    private void handleElGamal(Scanner scanner, String inputFile, String outputFile, String cipherType) throws Exception {
        System.out.print("Enter large prime number p: ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.print("Enter primitive root g: ");
        BigInteger g = new BigInteger(scanner.nextLine());

        System.out.print("Enter private key x: ");
        BigInteger x = new BigInteger(scanner.nextLine());

        BigInteger publicKey = elGamalService.calculatePublicKey(g, x, p);
        System.out.println("Calculated public key: " + publicKey);

        if ("encrypt".equalsIgnoreCase(cipherType)) {
            elGamalService.encryptFile(inputFile, outputFile, p, g, publicKey);
        } else if ("decrypt".equalsIgnoreCase(cipherType)) {
            elGamalService.decryptFile(inputFile, outputFile, p, x);
        } else {
            System.out.println("Invalid cipher type");
        }
    }

    private void handleShamir(Scanner scanner, String inputFile, String outputFile, String cipherType) throws Exception {
        System.out.print("Enter large prime number p: ");
        BigInteger p = new BigInteger(scanner.nextLine());

        BigInteger[] keyPair = shamirService.generateKeyPair(p);
        System.out.println("Generated encryption key cA: " + keyPair[0]);
        System.out.println("Generated decryption key dA: " + keyPair[1]);

        if ("encrypt".equalsIgnoreCase(cipherType)) {
            shamirService.encryptFile(inputFile, outputFile, p, keyPair[0], keyPair[1]);
        } else if ("decrypt".equalsIgnoreCase(cipherType)) {
            shamirService.decryptFile(inputFile, outputFile, p, keyPair[1]);
        } else {
            System.out.println("Invalid cipher type");
        }
    }

    private void handleRabin(Scanner scanner, String inputFile, String outputFile, String cipherType) throws Exception {
        System.out.print("Enter prime number p (must be ≡ 3 mod 4): ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.print("Enter prime number q (must be ≡ 3 mod 4): ");
        BigInteger q = new BigInteger(scanner.nextLine());

        BigInteger n = p.multiply(q);
        System.out.println("Modulus n: " + n);

        if ("encrypt".equalsIgnoreCase(cipherType)) {
            rabinService.encryptFile(inputFile, outputFile, n);
        } else if ("decrypt".equalsIgnoreCase(cipherType)) {
            rabinService.decryptFile(inputFile, outputFile, p, q);
        } else {
            System.out.println("Invalid cipher type");
        }
    }

    private void handleEllipticCurve(Scanner scanner, String inputFile, String outputFile, String cipherType) throws Exception {
        System.out.println("Using standard curve: p=751, a=-1, b=1, G=(0,1)");
        
        BigInteger p = BigInteger.valueOf(751);
        BigInteger a = BigInteger.valueOf(-1);
        BigInteger b = BigInteger.ONE;
        EllipticCurveService.ECPoint g = new EllipticCurveService.ECPoint(BigInteger.ZERO, BigInteger.ONE);
        EllipticCurveService.ECParameters curve = new EllipticCurveService.ECParameters(a, b, p, g);

        System.out.print("Enter private key: ");
        BigInteger privateKey = new BigInteger(scanner.nextLine());

        EllipticCurveService.ECPoint publicKey = ellipticCurveService.calculatePublicKey(privateKey, curve);
        System.out.println("Calculated public key: (" + publicKey.getX() + ", " + publicKey.getY() + ")");

        if ("encrypt".equalsIgnoreCase(cipherType)) {
            ellipticCurveService.encryptFile(inputFile, outputFile, curve, publicKey);
        } else if ("decrypt".equalsIgnoreCase(cipherType)) {
            ellipticCurveService.decryptFile(inputFile, outputFile, curve, privateKey);
        } else {
            System.out.println("Invalid cipher type");
        }
    }
}
