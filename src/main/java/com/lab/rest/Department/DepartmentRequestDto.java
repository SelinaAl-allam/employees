package com.lab.rest.Department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequestDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private  String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name;}
}
