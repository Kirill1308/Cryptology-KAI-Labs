package com.popov.service.crypto;

/**
 * Strategy interface for cryptographic operations
 */
public interface CryptoService {
    
    /**
     * Encrypts file content
     * @param inputPath path to input file
     * @param outputPath path to output file
     * @param params encryption parameters
     * @throws Exception if encryption fails
     */
    void encryptFile(String inputPath, String outputPath, Object... params) throws Exception;
    
    /**
     * Decrypts file content
     * @param inputPath path to input file
     * @param outputPath path to output file
     * @param params decryption parameters
     * @throws Exception if decryption fails
     */
    void decryptFile(String inputPath, String outputPath, Object... params) throws Exception;
    
    /**
     * Returns the name of the cryptographic algorithm
     * @return algorithm name
     */
    String getAlgorithmName();
}
