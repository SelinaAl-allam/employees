package com.lab.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    List<EmployeeResponseDto> all() {
        return employeeService.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    @PostMapping("/employees")
    EmployeeResponseDto newEmployee(@Valid @RequestBody EmployeeRequestDto request) {
        Employee saved = employeeService.create(EmployeeMapper.toEntity(request));
        return EmployeeMapper.toDto(saved);
    }

    @GetMapping("/employees/{id}")
    EmployeeResponseDto one(@PathVariable Long id) {
        return EmployeeMapper.toDto(employeeService.findById(id));
    }

    @GetMapping("/employees/email/{email}")
    EmployeeResponseDto email(@PathVariable String email) {
        return EmployeeMapper.toDto(employeeService.findByEmail(email));
    }

    @PutMapping("/employees/{id}")
    EmployeeResponseDto replaceEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto request
    ) {
        Employee updated = employeeService.replace(id, EmployeeMapper.toEntity(request));
        return EmployeeMapper.toDto(updated);
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
    }
}
