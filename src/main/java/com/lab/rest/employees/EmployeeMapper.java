package com.lab.rest.employees;

public class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toEntity(EmployeeRequestDto dto) {
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setEmail(dto.getEmail());
        e.setYearsOfExperience(dto.getYearsOfExperience());
        e.setHiredDate(dto.getHiredDate());
        return e;
    }

    public static EmployeeResponseDto toDto(Employee e) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getName(),
                e.getRole(),
                e.getEmail(),
                e.getDepartment() != null ? e.getDepartment().getName() : null,
                e.getYearsOfExperience(),
                e.getHiredDate()
        );
    }
}