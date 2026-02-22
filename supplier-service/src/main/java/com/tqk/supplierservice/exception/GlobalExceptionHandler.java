package com.tqk.supplierservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Xử lý các exception dạng ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handle(ResponseStatusException ex) {

        ExceptionCode exceptionCode;

        try {
            // Lấy reason trong exception để map sang enum ErrorCode
            // Ví dụ: ex.getReason() = "PROFILE_NOT_FOUND"
            exceptionCode = ExceptionCode.valueOf(ex.getReason());
        } catch (Exception e) {
            // Nếu reason không khớp với bất kỳ ErrorCode nào
            // thì fallback về lỗi hệ thống
            exceptionCode = ExceptionCode.INTERNAL_ERROR;
        }

        // Trả về HTTP status đúng như exception đã khai báo
        // cùng với body chứa mã lỗi và message mặc định
        return ResponseEntity.status(ex.getStatusCode()).
                body(new ExceptionResponse(exceptionCode.name(), exceptionCode.getDefaultMessage()));
    }

    // Xử lý các lỗi thuộc validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Dữ liệu không hợp lệ");

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(
                        "VALIDATION_ERROR",
                        message
                ));
    }

    /**
     * Xử lý tất cả các exception còn lại chưa được handle
     * (lỗi không mong muốn, lỗi runtime, bug, v.v.)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnknown(Exception ex) {

        log.error("Unexpected error occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        ExceptionCode.INTERNAL_ERROR.name(),
                        ExceptionCode.INTERNAL_ERROR.getDefaultMessage()
                ));
    }
}
