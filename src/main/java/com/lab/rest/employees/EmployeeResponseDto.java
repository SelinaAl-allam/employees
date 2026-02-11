package com.lab.rest.employees;

import java.time.LocalDate;

public class EmployeeResponseDto {

    private Long id;
    private String name;
    private String role;
    private String email;
    private String departmentName;
    private Integer yearsOfExperience;
    private LocalDate hiredDate;

    public EmployeeResponseDto(Long id, String name, String role, String email,
                               String departmentName, Integer yearsOfExperience,
                               LocalDate hiredDate) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.departmentName = departmentName;
        this.yearsOfExperience = yearsOfExperience;
        this.hiredDate = hiredDate;
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getDepartmentName() { return departmentName; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public LocalDate getHiredDate() { return hiredDate; }
}