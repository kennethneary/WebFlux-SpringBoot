package com.example.mongodb.config;

import com.example.mongodb.dao.EmployeeRepository;
import com.example.mongodb.model.Employee;
import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Log4j2
@Component
public class MongoDbDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EmployeeRepository reactiveEmployeeRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        final Supplier<String> idSupplier = this.getIdSequenceSupplier();
        final Supplier<Integer> salarySupplier = this.getSalarySupplier();
        final Faker faker = new Faker();
        final int totalEmployees = 10;
        final Stream<String> names = Stream.generate(() -> faker.name().fullName()).limit(totalEmployees);

        this.reactiveEmployeeRepository
                .deleteAll()
                .thenMany(
                        Flux.fromStream(names)
                                .map(name -> new Employee(idSupplier.get(), name, salarySupplier.get()))
                                .flatMap(this.reactiveEmployeeRepository::save)
                )
                .thenMany(this.reactiveEmployeeRepository.findAll())
                .subscribe(profile -> log.info("saving " + profile.toString()));
    }

    private Supplier<String> getIdSequenceSupplier() {
        return new Supplier<String>() {
            Long l = 0L;

            @Override
            public String get() {
                return String.format("%05d", this.l++);
            }
        };
    }

    private Supplier<Integer> getSalarySupplier() {
        return new Supplier<Integer>() {
            final Random rand = new Random();

            final int maxSalary = 100_000;
            final int minSalary = 30_000;
            final int salaryRange = ((this.maxSalary - this.minSalary) + 1 )+ this.minSalary;

            @Override
            public Integer get() {
                return this.rand.nextInt(this.salaryRange);
            }
        };
    }
}
