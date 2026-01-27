package com.tqk.cartservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý các exception dạng ResponseStatusException
     */
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

    /**
     * Xử lý tất cả các exception còn lại chưa được handle
     * (lỗi không mong muốn, lỗi runtime, bug, v.v.)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnknown(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        ExceptionCode.INTERNAL_ERROR.name(),
                        ExceptionCode.INTERNAL_ERROR.getDefaultMessage()
                ));
    }
}
