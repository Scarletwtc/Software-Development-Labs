package com.example.data.domain;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "seats")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Seat> seats = new HashSet<>();

    public void addSeat(Seat s) {
        seats.add(s);
        s.setHall(this);
    }
}
