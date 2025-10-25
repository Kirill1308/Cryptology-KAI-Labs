package com.popov.hw.service.crypto;

public interface CryptoService {

    void encrypt(String inputPath, String outputPath, Object parameters) throws Exception;

    void decrypt(String inputPath, String outputPath, Object parameters) throws Exception;
}
