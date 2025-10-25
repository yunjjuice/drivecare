package com.gitauto.drivecare.exception;

import com.gitauto.drivecare.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handle(ApiException e) {
        return ResponseEntity
                .status(e.getCode().getStatus())
                .body(ApiResponse.<String>builder()
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleOthers(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.<String>builder().success(false).message("server error").build());
    }
}
