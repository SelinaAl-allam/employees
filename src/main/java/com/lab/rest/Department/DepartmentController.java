package com.lab.rest.Department;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentServicelmpl departmentService;

    DepartmentController(DepartmentServicelmpl departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody DepartmentRequestDto newDepartment, UriComponentsBuilder uriBuilder) {

            Department saved = departmentService.create(newDepartment);
            DepartmentResponseDto departmentResponseDto = DepartmentMapper.toDto(saved);

        URI location = uriBuilder
                .path("/departments/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(departmentResponseDto);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletDepartment(@PathVariable Long id) {
        departmentService.deleteByIdOrThrow(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    ResponseEntity<DepartmentResponseDto> replaceDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDto newDepartment
    ) {
        Department updated = departmentService.replace(id,newDepartment);
        return ResponseEntity.ok(DepartmentMapper.toDto(updated));
    }
    @GetMapping("/{id}")
    DepartmentResponseDto one(@PathVariable Long id) {
        return DepartmentMapper.toDto(departmentService.findById(id));
    }

    @GetMapping("/name/{name}")
    DepartmentResponseDto name(@PathVariable String name) {
        return DepartmentMapper.toDto(departmentService.findByName(name));
    }

    @GetMapping
    List<DepartmentResponseDto> all() {
        return departmentService.findAll()
                .stream()
                .map(DepartmentMapper::toDto)
                .toList();
    }
}
