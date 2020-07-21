package com.example.mongodb.dao;

import com.example.mongodb.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Flux<Employee> findAllByIdNotNullOrderByNameAsc(final Pageable page);
}