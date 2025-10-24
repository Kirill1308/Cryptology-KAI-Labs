# Cryptology KAI Labs

This project implements various cryptographic algorithms as part of university laboratory work.

## Implemented Labs

### Lab 1: RSA Algorithm
- Asymmetric encryption based on prime factorization
- Uses public key (e, n) for encryption and private key (d, n) for decryption

### Lab 2: El-Gamal Algorithm
- Asymmetric encryption based on discrete logarithm problem
- Parameters: prime p, primitive root g, private key x
- Public key: g^x mod p

### Lab 3: Shamir and Rabin Algorithms

#### Shamir's Three-Pass Protocol
- Allows secure communication without pre-shared keys
- Uses encryption/decryption key pairs (c, d) where c*d ≡ 1 (mod p-1)

#### Rabin Cryptosystem
- Based on difficulty of factorization
- Encryption: C = M^2 mod n
- Requires primes p and q where p ≡ 3 (mod 4) and q ≡ 3 (mod 4)

### Lab 4: Elliptic Curve Cryptography
- Based on elliptic curve discrete logarithm problem
- Uses standard curve: y² = x³ - x + 1 (mod 751)
- Generator point G = (0, 1)

## Design Patterns Used

1. **Strategy Pattern**: `CryptoService` interface with different algorithm implementations
2. **Factory Pattern**: `CryptoServiceFactory` creates appropriate service instances
3. **Template Method**: Common encryption/decryption flow in interface

## Building and Running

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/Cryptology-KAI-Labs-1.0-SNAPSHOT.jar
```

The application will present an interactive menu to select the lab and provide parameters.

## Test Files

Test input files and parameter examples are located in:
- `src/main/resources/test-inputs/`

### Files:
- `test1.txt`, `test2.txt`, `test3.txt` - Sample text files for encryption
- `rsa_params.txt` - Example RSA parameters
- `elgamal_params.txt` - Example El-Gamal parameters
- `rabin_params.txt` - Example Rabin parameters
- `ec_params.txt` - Example Elliptic Curve parameters

## Example Usage

### RSA Encryption/Decryption
```
Enter lab number: 1
Enter input file path: src/main/resources/test-inputs/test1.txt
Enter output file path: encrypted.bin
Enter cipher type (encrypt/decrypt): encrypt
Enter public key e: 65537
Enter prime number p: 61
Enter prime number q: 53
```

### El-Gamal Encryption/Decryption
```
Enter lab number: 2
Enter input file path: src/main/resources/test-inputs/test1.txt
Enter output file path: encrypted.bin
Enter cipher type (encrypt/decrypt): encrypt
Enter large prime number p: 467
Enter primitive root g: 2
Enter private key x: 153
```

## Security Notes

- The example parameters provided are for **demonstration and testing only**
- For production use, employ much larger primes (1024+ bits for RSA, 2048+ recommended)
- Use cryptographically secure random number generators
- Never reuse keys across different messages

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/popov/
│   │       ├── Main.java                      # Application entry point
│   │       ├── factory/
│   │       │   └── CryptoServiceFactory.java  # Factory pattern
│   │       └── service/
│   │           ├── crypto/
│   │           │   └── CryptoService.java     # Strategy interface
│   │           ├── RsaService.java            # Lab 1
│   │           ├── ElGamalService.java        # Lab 2
│   │           ├── ShamirService.java         # Lab 3
│   │           ├── RabinService.java          # Lab 3
│   │           └── EllipticCurveService.java  # Lab 4
│   └── resources/
│       ├── test-inputs/                       # Test files and parameters
│       └── labs/                              # Lab PDFs (Ukrainian)
```

## Requirements

- Java 21
- Maven 3.x
- Spring Boot 3.2.0

## License

Educational project for Karazin Kharkiv National University
