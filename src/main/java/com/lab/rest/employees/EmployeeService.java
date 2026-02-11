package com.lab.rest.employees;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee create(Employee employee);
    Employee findById(Long id);
    Employee findByEmail(String email);

    Employee replace(Long id, Employee newEmployee);

    Employee updateExisting(Long id, Employee newEmployee);

    void deleteById(Long id);

    void deleteByIdOrThrow(Long id);

    @Transactional
    Employee assignToDepartment(Long employeeId, Long departmentId);

    public PagedResponse<EmployeeResponseDto> listEmployees(Pageable pageable);

    PagedResponse<EmployeeResponseDto> listEmployees(
            Pageable pageable,
            String role,
            String nameContains
    );
}
