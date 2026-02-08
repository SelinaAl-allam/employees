package com.lab.rest.Department;

public class DepartmentHasEmployeesException extends RuntimeException {
    public DepartmentHasEmployeesException(Long message) {
        super("Department " + message + " cannot be deleted because it has employees");}
}
