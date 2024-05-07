package com.nagarro.customerservice.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;


//@RestControllerAdvice
//public class GlobalExceptionHandler {
//	
//	   @ExceptionHandler(MethodArgumentNotValidException.class)
//	    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
//	        String errors = ex.getBindingResult().getFieldErrors()
//	                .stream().map(FieldError::getDefaultMessage).toString();
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(errors, HttpStatus.BAD_REQUEST));
//	    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
//    }
//
//    @ExceptionHandler(CustomerNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND));
//    }
//    
//    @ExceptionHandler(CustomerAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ResponseEntity<Map<String, Object>> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(createErrorResponse(ex.getMessage(), HttpStatus.CONFLICT));
//    }
//    
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
//        Map<String, Object> response = createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    private Map<String, Object> createErrorResponse(String message, HttpStatus httpStatus) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", message);
//        response.put("code", httpStatus.value());
//        response.put("timestamp", LocalDateTime.now());
//        return response;
//    }
//}
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors, HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors, HttpStatus.NOT_FOUND), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, Object>> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors, HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public final ResponseEntity<Map<String, Object>> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors, HttpStatus.CONFLICT), new HttpHeaders(), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors, HttpStatus.BAD_REQUEST), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getErrorsMap(List<String> errors, HttpStatus httpStatus) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", errors);
        errorResponse.put("code", httpStatus.value());
        errorResponse.put("timestamp", LocalDateTime.now());
        return errorResponse;
    }
}