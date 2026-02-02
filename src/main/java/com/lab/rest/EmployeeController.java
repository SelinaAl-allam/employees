package com.lab.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/employees")
class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    List<Employee> all() {
        return employeeService.findAll();
    }

    // POST /employees -> 201 Created + Location: /employees/{id}
    @PostMapping
    ResponseEntity<Employee> newEmployee(
            @RequestBody Employee newEmployee,
            UriComponentsBuilder uriBuilder
    ) {
        Employee saved = employeeService.create(newEmployee);

        URI location = uriBuilder
                .path("/employees/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    Employee one(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    // PUT update-only: 200 OK on update; 404 if missing
    @PutMapping("/{id}")
    ResponseEntity<Employee> replaceEmployee(
            @PathVariable Long id,
            @RequestBody Employee newEmployee
    ) {
        Employee updated = employeeService.updateExisting(id, newEmployee);
        return ResponseEntity.ok(updated);
    }

    // DELETE -> 204 No Content; 404 if missing
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteByIdOrThrow(id);
        return ResponseEntity.noContent().build();
    }
}
