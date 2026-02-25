package br.usp.walmir.walletwise.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorItem> fieldErrors;

    @Getter
    @Builder
    public static class FieldErrorItem {
        private String field;
        private String message;
    }
}

