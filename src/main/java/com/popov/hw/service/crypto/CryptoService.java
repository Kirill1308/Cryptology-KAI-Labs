package com.popov.hw.service.crypto;

public interface CryptoService {

    void encryptFile(String inputPath, String outputPath, Object... params) throws Exception;

    void decryptFile(String inputPath, String outputPath, Object... params) throws Exception;

}
