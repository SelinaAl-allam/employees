package com.lab.rest.Department;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String message) {
        super("Department already exists: "+message);
    }
}
