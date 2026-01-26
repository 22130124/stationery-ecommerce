package com.tqk.profileservice.exception;

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
    public ResponseEntity<ErrorResponse> handle(ResponseStatusException ex) {

        ErrorCode errorCode;

        try {
            // Lấy reason trong exception để map sang enum ErrorCode
            // Ví dụ: ex.getReason() = "PROFILE_NOT_FOUND"
            errorCode = ErrorCode.valueOf(ex.getReason());
        } catch (Exception e) {
            // Nếu reason không khớp với bất kỳ ErrorCode nào
            // thì fallback về lỗi hệ thống
            errorCode = ErrorCode.INTERNAL_ERROR;
        }

        // Trả về HTTP status đúng như exception đã khai báo
        // cùng với body chứa mã lỗi và message mặc định
        return ResponseEntity.status(ex.getStatusCode()).
                body(new ErrorResponse(errorCode.name(), errorCode.getDefaultMessage()));
    }

    /**
     * Xử lý tất cả các exception còn lại chưa được handle
     * (lỗi không mong muốn, lỗi runtime, bug, v.v.)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ErrorCode.INTERNAL_ERROR.name(),
                        ErrorCode.INTERNAL_ERROR.getDefaultMessage()
                ));
    }
}
