package com.example.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document
@Data
@AllArgsConstructor
public class Employee {
    @Id
    final String id;
    final String name;
    final long salary;
}
