package com.example.mongodb.handler;

import com.example.mongodb.model.Employee;
import com.example.mongodb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

@Component
public class EmployeeHandler {

    private static final int DELAY_PER_ITEM_MS = 1000;

    @Autowired
    private EmployeeService employeeService;

    public Mono<ServerResponse> getEmployees(final ServerRequest request) {
        final int page = this.getParam(request, "page").map(Integer::valueOf).orElse(0);
        final int size = this.getParam(request, "size").map(Integer::valueOf).orElse(0);

        final Flux<Employee> employees = this.employeeService.getEmployeePaging(PageRequest.of(page, size))
                .delayElements(Duration.ofMillis(DELAY_PER_ITEM_MS));
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(employees, Employee.class);
    }

    public Mono<ServerResponse> getById(final ServerRequest request) {
        final Mono<Employee> employee = this.employeeService.findById(this.getId(request));
        return employee
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(employee, Employee.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAll(final ServerRequest request) {
        final Flux<Employee> employees = this.employeeService.findAll();
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(employees, Employee.class);
    }

    public Mono<ServerResponse> deleteById(final ServerRequest request) {
        final Mono<Employee> employee = this.employeeService.delete(this.getId(request));
        return employee
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(employee, Employee.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateById(final ServerRequest request) {
        final Mono<Employee> employee = request.bodyToMono(Employee.class)
                .flatMap(e -> this.employeeService.update(e, this.getId(request)));

        return employee
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(employee, Employee.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(final ServerRequest request) {
        final Mono<Employee> employee = request.bodyToMono(Employee.class)
                            .flatMap(e -> this.employeeService.create(e));

        return employee
                .flatMap(e -> ServerResponse
                        .created(URI.create("/reactive/classic/employee/" + e.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private String getId(final ServerRequest request) {
        return request.pathVariable("id");
    }

    private Optional<String> getParam(ServerRequest request, String paramName) {
        return request.queryParam(paramName);
    }
}
