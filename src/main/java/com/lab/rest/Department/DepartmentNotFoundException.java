package com.lab.rest.Department;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Could not find department " + id);
    }

    public DepartmentNotFoundException(String name) {super("Department not found with name: " + name);}
}
