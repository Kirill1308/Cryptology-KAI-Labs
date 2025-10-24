package com.popov.hw.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CipherOperation {
    ENCRYPT("encrypt"),
    DECRYPT("decrypt");

    private final String operation;

    public static CipherOperation fromString(String input) {
        for (CipherOperation operation : values()) {
            if (operation.operation.equalsIgnoreCase(input.trim())) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Invalid cipher operation: " + input + ". Use 'encrypt' or 'decrypt'");
    }
}
