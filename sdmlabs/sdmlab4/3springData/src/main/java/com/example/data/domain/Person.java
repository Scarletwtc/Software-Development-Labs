package com.example.data.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) // subclassing is
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    private Date birthday;
    private char gender;

}
