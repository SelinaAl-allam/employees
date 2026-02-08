package com.lab.rest.Department;

import com.lab.rest.employees.Employee;

public class DepartmentMapper {
    private DepartmentMapper() {}
    public static Department toEntity(DepartmentRequestDto dto) {
        Department department = new Department();
        department.setName(dto.getName());
        return department;
    }
    public static DepartmentResponseDto toDto(Department department) {
        int employeeCount = 0;
        if (department.getEmployees() != null) {
            employeeCount = department.getEmployees().size();
        }
        return new DepartmentResponseDto(
                department.getId(),
                department.getName(),
                employeeCount);

    }

}
