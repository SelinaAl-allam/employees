package com.lab.rest.Department;

public class DepartmentResponseDto {
    private long id;
    private String name;
    private Integer employeeCount;
    public DepartmentResponseDto(long id, String name, Integer employeeCount) {
        this.id = id;
        this.name = name;
        this.employeeCount = employeeCount;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getEmployeeCount() {
        return employeeCount;
    }
}
