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
        String normalized = input.trim().toLowerCase();
        for (CipherOperation operation : values()) {
            if (operation.operation.equalsIgnoreCase(normalized)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("error.invalid.operation");
    }
}
