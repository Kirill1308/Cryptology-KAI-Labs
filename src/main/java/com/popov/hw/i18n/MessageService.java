package com.popov.hw.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;
    private final Locale locale = Locale.ENGLISH;

    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    public String getAppTitle() {
        return getMessage("app.title");
    }

    public String getSelectLabMessage() {
        return getMessage("app.select.lab");
    }

    public String getEnterLabNumberPrompt() {
        return getMessage("app.enter.lab.number");
    }

    public String getEnterOperationPrompt() {
        return getMessage("app.enter.operation");
    }

    public String getEnterInputFilePrompt() {
        return getMessage("app.enter.input.file");
    }

    public String getEnterOutputFilePrompt() {
        return getMessage("app.enter.output.file");
    }

    public String getSuccessMessage() {
        return getMessage("app.success");
    }

    public String getErrorMessage(String message) {
        return getMessage("app.error", message);
    }

    public String getAlgorithmName(String algorithmKey) {
        return getMessage("algorithm." + algorithmKey.toLowerCase().replace("_", "."));
    }

    public String getInvalidOperationError(String operation) {
        return getMessage("error.invalid.operation", operation);
    }

    public String getOperationFailedError(String algorithm, String message) {
        return getMessage("error.operation.failed", algorithm, message);
    }
}
