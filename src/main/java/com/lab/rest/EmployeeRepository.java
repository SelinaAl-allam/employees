package com.lab.rest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}