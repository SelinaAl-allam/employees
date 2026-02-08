package com.lab.rest.Department;

import com.lab.rest.employees.Employee;
import com.lab.rest.employees.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartmentServicelmpl implements DepartmentService {

    private final   DepartmentRepository departmentRepository;

    public DepartmentServicelmpl(DepartmentRepository repository) {
        this.departmentRepository = repository;}

    @Transactional(readOnly = true)
    public List<Department>findAll(){
        return departmentRepository.findAll();
    }
    public Department create(DepartmentRequestDto request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DepartmentAlreadyExistsException(request.getName());
        }
        Department dept = DepartmentMapper.toEntity(request);
        return departmentRepository.save(dept);
    }
    @Transactional(readOnly = true)
    public Department findById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(()->new DepartmentNotFoundException(id));
    }
    public Department replace(Long id, DepartmentRequestDto request) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        if (!existing.getName().equalsIgnoreCase(request.getName()) &&
                departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DepartmentAlreadyExistsException(request.getName());
        }

        existing.setName(request.getName());
        return departmentRepository.save(existing);
    }

    public void deleteByIdOrThrow(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        if (department.getEmployees() != null &&
                !department.getEmployees().isEmpty()) {

            throw new DepartmentHasEmployeesException(id);
        }

        departmentRepository.delete(department);
    }
    @Transactional(readOnly = true)
    public Department findByName(String name) {
        return departmentRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new DepartmentNotFoundException(name));
    }
}
