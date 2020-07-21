package com.example.mongodb.controller;

import com.example.mongodb.model.Employee;
import com.example.mongodb.service.EmployeeService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping(value = "reactive/classic/employee")
public class EmployeeController {

    private static final int DELAY_PER_ITEM_MS = 1000;
    private final MediaType jsonMediaType = MediaType.APPLICATION_JSON;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/paged")
    public Publisher<Employee> getEmployees(
            final @RequestParam(name = "page") int page,
            final @RequestParam(name = "size") int size) {
        return this.employeeService.getEmployeePaging(PageRequest.of(page, size))
                .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
    }

    @GetMapping
    public Publisher<Employee> getAll() {
        return this.employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Publisher<Employee> getById(@PathVariable("id") final String id) {
        return this.employeeService.findById(id);
    }

    @PostMapping
    public Publisher<ResponseEntity<Employee>> create(@RequestBody final Employee employee) {
        return this.employeeService
                .create(employee)
                .map(e -> ResponseEntity.created(URI.create("/reactive/classic/employee/" + e.getId()))
                        .contentType(this.jsonMediaType)
                        .build());
    }

//    @PostMapping
//    public Mono<ResponseEntity<Employee>> create5(@RequestBody final Employee employee) {
//        Mono<ResponseEntity<Employee>> p = Mono.<ResponseEntity<Employee>>fromSupplier(() -> ResponseEntity.notFound().<Employee>build());
//
//        Mono<ResponseEntity<Employee>> pl = Mono.fromSupplier(() -> ResponseEntity.notFound().build());
//
//        Mono<? extends ResponseEntity<? extends Employee>> l = Mono.just(ResponseEntity.notFound().build());
//
//        return this.employeeService
//                .create(employee)
//                .map(e -> ResponseEntity.created(URI.create("/reactive/classic/employee/" + e.getId()))
//                        .contentType(this.mediaType)
//                        .build())
//                .switchIfEmpty(pl);
//    }

    @DeleteMapping("/{id}")
    public Publisher<Employee> deleteById(@PathVariable final String id) {
        return this.employeeService.delete(id);
    }

    @PutMapping("/{id}")
    public Publisher<ResponseEntity<Employee>> updateById(@PathVariable final String id, @RequestBody final Employee employee) {
        return Mono
                .just(employee)
                .flatMap(e -> this.employeeService.update(e, id))
                .map(e -> ResponseEntity
                        .ok()
                        .contentType(this.jsonMediaType)
                        .build());
    }
}


