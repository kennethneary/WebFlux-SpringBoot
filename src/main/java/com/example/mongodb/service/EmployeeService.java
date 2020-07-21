package com.example.mongodb.service;

import com.example.mongodb.dao.EmployeeRepository;
import com.example.mongodb.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@Service
public class EmployeeService implements IEmployeeService {
     
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Mono<Employee> create(final Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @Override
    public Mono<Employee> findById(final String id) {
        return this.employeeRepository.findById(id);
    }

    @Override
    public Flux<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Mono<Employee> update(final Employee employee, final String id) {
        return this.employeeRepository
                .findById(id)
                .map(e -> new Employee(e.getId(), employee.getName(), employee.getSalary()))
                .flatMap(this.employeeRepository::save);
    }

    @Override
    public Mono<Employee> delete(final String id) {
        return this.employeeRepository
                .findById(id)
                .flatMap(e -> this.employeeRepository
                                .deleteById(e.getId())
                                .thenReturn(e)
                );
    }

    @Override
    public Flux<Employee> getEmployeePaging(final PageRequest pageRequest) {
        return this.employeeRepository.findAllByIdNotNullOrderByNameAsc(pageRequest);
    }
}