package com.lab.rest.employees;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "List employees",
            description = "Get a paginated, sorted, and filtered list of employees. Supports filtering by role, name, email, experience, and hire date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employees"),
            @ApiResponse(responseCode = "400", description = "Invalid sort field or parameter")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<EmployeeResponseDto>> listEmployees(
            // pagination + sorting handled automatically by Spring
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            })
            Pageable pageable,

            // filters (optional)
            @Parameter(description = "Filter by exact role match")
            @RequestParam(required = false) String role,

            @Parameter(description = "Filter by name containing text (case-insensitive)")
            @RequestParam(required = false) String nameContains,

            @Parameter(description = "Filter by email containing text (case-insensitive)")
            @RequestParam(required = false) String emailContains,

            @Parameter(description = "Minimum years of experience")
            @RequestParam(required = false) Integer minExperience,

            @Parameter(description = "Maximum years of experience")
            @RequestParam(required = false) Integer maxExperience,

            @Parameter(description = "Hired after date (format: yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hiredAfter,

            @Parameter(description = "Hired before date (format: yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hiredBefore
    ) {
        PagedResponse<EmployeeResponseDto> response = employeeService.listEmployees(
                pageable, role, nameContains, emailContains,
                minExperience, maxExperience, hiredAfter, hiredBefore
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    ResponseEntity<EmployeeResponseDto> newEmployee(
            @Parameter(description = "Employee data to create")
            @RequestBody EmployeeRequestDto newEmployee,
            UriComponentsBuilder uriBuilder
    ) {
        Employee saved = employeeService.create(EmployeeMapper.toEntity(newEmployee));
        EmployeeResponseDto employeeResponseDto = EmployeeMapper.toDto(saved);

        URI location = uriBuilder
                .path("/api/employees/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(employeeResponseDto);
    }

    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    EmployeeResponseDto one(@PathVariable Long id) {
        return EmployeeMapper.toDto(employeeService.findById(id));
    }

    @Operation(summary = "Get employee by email")
    @GetMapping("/email/{email}")
    EmployeeResponseDto email(@PathVariable String email) {
        return EmployeeMapper.toDto(employeeService.findByEmail(email));
    }

    @Operation(summary = "Update employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    ResponseEntity<EmployeeResponseDto> replaceEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto newEmployee
    ) {
        Employee updated = employeeService.updateExisting(id, EmployeeMapper.toEntity(newEmployee));
        return ResponseEntity.ok(EmployeeMapper.toDto(updated));
    }

    @Operation(summary = "Delete employee")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteByIdOrThrow(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign employee to department")
    @PutMapping("/{id}/department/{deptId}")
    public ResponseEntity<EmployeeResponseDto> assignToDepartment(
            @PathVariable Long id,
            @PathVariable Long deptId) {
        Employee updated = employeeService.assignToDepartment(id, deptId);
        return ResponseEntity.ok(EmployeeMapper.toDto(updated));
    }
}