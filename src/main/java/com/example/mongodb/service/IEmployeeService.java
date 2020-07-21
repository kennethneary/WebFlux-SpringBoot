package com.example.mongodb.service;

import com.example.mongodb.model.Employee;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
public interface IEmployeeService {
    Mono<Employee> create(Employee employee);
    Mono<Employee> findById(String id);
    Flux<Employee> findAll();
    Mono<Employee> update(Employee employee, String id);
    Mono<Employee> delete(String id);
    Flux<Employee> getEmployeePaging(PageRequest pageRequest);
}