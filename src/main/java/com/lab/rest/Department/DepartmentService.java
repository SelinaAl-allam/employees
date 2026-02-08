package com.lab.rest.Department;

import com.lab.rest.employees.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    Department create(DepartmentRequestDto request);
    Department findById(Long id);
    Department replace(Long id, DepartmentRequestDto request);
    void deleteByIdOrThrow(Long id);
    Department findByName(String name);

}
