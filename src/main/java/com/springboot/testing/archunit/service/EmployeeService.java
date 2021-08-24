package com.springboot.testing.archunit.service;

import com.springboot.testing.archunit.entity.Employee;
import com.springboot.testing.archunit.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> retrieveEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return this.employeeRepository.save(employee);
    }
}