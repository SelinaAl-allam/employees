package com.lab.rest.employees;

import java.util.List;

import com.lab.rest.Department.Department;
import com.lab.rest.Department.DepartmentNotFoundException;
import com.lab.rest.Department.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl  {

    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Employee create(Employee employee) {
        // Optional: ensure client doesn't control ID
        // employee.setId(null);
        return repository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));
    }

    // Update-only: throws 404 if not found
    public Employee updateExisting(Long id, Employee newEmployee) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        existing.setName(newEmployee.getName());
        existing.setRole(newEmployee.getRole());
        // If you have email:
        // existing.setEmail(newEmployee.getEmail());

        return repository.save(existing);
    }

    public void deleteByIdOrThrow(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public Employee assignToDepartment(Long employeeId, Long departmentId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        employee.setDepartment(department);
        return repository.save(employee);
    }
}
