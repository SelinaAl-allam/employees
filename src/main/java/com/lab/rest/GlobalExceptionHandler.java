package com.lab.rest;

import java.time.Instant;

import com.lab.rest.Department.DepartmentAlreadyExistsException;
import com.lab.rest.Department.DepartmentHasEmployeesException;
import com.lab.rest.Department.DepartmentNotFoundException;
import com.lab.rest.employees.EmployeeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            DepartmentNotFoundException ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Optional: catch-all (prevents stack traces from leaking to clients)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                500,
                "Internal Server Error",
                "Something went wrong",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ApiError body = new ApiError(
                Instant.now().toString(),
                400,
                "Bad Request",
                "Must not be blank",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleDepartmentExists(
            DepartmentAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DepartmentHasEmployeesException.class)
    public ResponseEntity<ApiError> handleDepartmentHasEmployees(
            DepartmentHasEmployeesException ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
