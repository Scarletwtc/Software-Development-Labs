package com.example.data.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cast","tickets"})
public class Representation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @ManyToOne
    private Opera opera;

    @ManyToOne
    private Hall hall;

    @ManyToMany(mappedBy = "cast") // owning side is in singer
    private Set<Singer> cast = new HashSet<>();

    @OneToMany(mappedBy = "representation", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    public void addTicket(Ticket t) {
        tickets.add(t);
        t.setRepresentation(this);
    }

}
