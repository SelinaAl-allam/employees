package com.lab.rest.employees;

import com.lab.rest.Department.Department;
import com.lab.rest.Department.DepartmentNotFoundException;
import com.lab.rest.Department.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "name", "role", "email",
            "yearsOfExperience", "hiredDate"
    );

    public EmployeeServiceImpl(EmployeeRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    private void validateSortFields(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid sort field: '" + order.getProperty() + "'. " +
                                "Allowed fields: " + String.join(", ", ALLOWED_SORT_FIELDS)
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Override
    public Employee create(Employee employee) {
        repository.findByEmail(employee.getEmail())
                .ifPresent(e -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Employee with email '" + employee.getEmail() + "' already exists"
                    );
                });
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
        return updateExisting(id, newEmployee);
    }

    public Employee updateExisting(Long id, Employee newEmployee) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (!existing.getEmail().equals(newEmployee.getEmail())) {
            repository.findByEmail(newEmployee.getEmail())
                    .ifPresent(e -> {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Email '" + newEmployee.getEmail() + "' already exists"
                        );
                    });
        }

        existing.setName(newEmployee.getName());
        existing.setRole(newEmployee.getRole());
        existing.setEmail(newEmployee.getEmail());
        existing.setYearsOfExperience(newEmployee.getYearsOfExperience());
        existing.setHiredDate(newEmployee.getHiredDate());

        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        deleteByIdOrThrow(id);
    }

    public void deleteByIdOrThrow(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public Employee assignToDepartment(Long employeeId, Long departmentId) {
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        employee.setDepartment(department);
        return repository.save(employee);
    }

    @Override
    public PagedResponse<EmployeeResponseDto> listEmployees(Pageable pageable) {
        validateSortFields(pageable);
        Page<Employee> page = repository.findAll(pageable);
        List<EmployeeResponseDto> content = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();
        return PagedResponse.from(page, content);
    }

    @Override
    public PagedResponse<EmployeeResponseDto> listEmployees(Pageable pageable, String role, String nameContains) {
        return listEmployees(pageable, role, nameContains, null, null, null, null, null);
    }

    public PagedResponse<EmployeeResponseDto> listEmployees(
            Pageable pageable,
            String role,
            String nameContains,
            String emailContains,          // Task 2
            Integer minExperience,
            Integer maxExperience,
            LocalDate hiredAfter,
            LocalDate hiredBefore
    ) {
        validateSortFields(pageable);

        @SuppressWarnings("ConstantConditions")
        Specification<Employee> spec = Specification.where((Specification<Employee>) null);

        if (role != null && !role.isBlank()) {
            spec = spec.and(EmployeeSpecifications.hasRole(role));
        }

        if (nameContains != null && !nameContains.isBlank()) {
            spec = spec.and(EmployeeSpecifications.nameContains(nameContains));
        }

        if (emailContains != null && !emailContains.isBlank()) {
            spec = spec.and(EmployeeSpecifications.emailContains(emailContains));
        }

        if (minExperience != null || maxExperience != null) {
            spec = spec.and(EmployeeSpecifications.experienceBetween(minExperience, maxExperience));
        }

        if (hiredAfter != null || hiredBefore != null) {
            spec = spec.and(EmployeeSpecifications.hiredDateBetween(hiredAfter, hiredBefore));
        }

        Page<Employee> page = repository.findAll(spec, pageable);
        List<EmployeeResponseDto> content = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();
        return PagedResponse.from(page, content);
    }
}