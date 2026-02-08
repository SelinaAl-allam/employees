package com.lab.rest.Department;

import java.util.List;

import com.lab.rest.employees.Employee;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "departments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String location;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Department() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public void addEmployee(Employee e) {
        employees.add(e);
        if (e != null) {
            e.setDepartment(this);
        }
    }

    public void removeEmployee(Employee e) {
        employees.remove(e);
        if (e != null) {
            e.setDepartment(null);
        }
    }
}
