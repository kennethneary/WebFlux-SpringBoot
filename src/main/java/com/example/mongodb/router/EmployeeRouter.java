package com.example.mongodb.router;

import com.example.mongodb.handler.EmployeeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class EmployeeRouter {

    @Bean
    public RouterFunction<ServerResponse> route(final EmployeeHandler handler) {
        return RouterFunctions
             .route(GET("/reactive/handler/employee/paged"), handler::getEmployees)
             .andRoute(GET("/reactive/handler/employee"), handler::getAll)
             .andRoute(GET("/reactive/handler/employee/{id}"), handler::getById)
             .andRoute(POST("/reactive/handler/employee").and(accept(MediaType.APPLICATION_JSON)), handler::create)
             .andRoute(DELETE("/reactive/handler/employee/{id}"), handler::deleteById)
             .andRoute(PUT("/reactive/handler/employee/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::updateById);
    }
}
