package com.lab.rest;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));
    }

    @Override
    public Employee replace(Long id, Employee newEmployee) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(newEmployee.getName());
                    existing.setRole(newEmployee.getRole());
                    // keep id stable; do NOT replace it from the body
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    // If you want PUT to create when not found:
                    // ensure the entity uses the path id
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @Override
    public void deleteById(Long id) {
        // Optional: validate existence first (gives nicer error behavior)
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
