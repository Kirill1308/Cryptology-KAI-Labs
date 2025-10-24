package com.popov;

import com.popov.service.RsaService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

    private final RsaService rsaService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input file path: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();

        System.out.print("Enter cipher type (encrypt/decrypt): ");
        String cipherType = scanner.nextLine();

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

        try {
            if ("encrypt".equalsIgnoreCase(cipherType)) {
                rsaService.encryptFile(inputFile, outputFile, e, n);
            } else if ("decrypt".equalsIgnoreCase(cipherType)) {
                rsaService.decryptFile(inputFile, outputFile, d, n);
            } else {
                System.out.println("Invalid cipher type");
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
