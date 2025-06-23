package com.example.data.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double cancelationFee;


    @ManyToOne
    private Person client;


    @OneToOne
    @JoinColumn(unique = true)
    private Ticket ticket;
}
