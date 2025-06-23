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
@EqualsAndHashCode(exclude = "representations")
//tells Lombok not to include the representations collection in equals/hashCode,
// avoiding infinite loops in bidirectional links.

public class Opera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //the database will auto-increment it for you.
    private Long id;
    private Date firstRepresentation;
    private String plot;
    private int numberOfActs;


    @ManyToOne
    private Person composer;

    @OneToMany(mappedBy = "opera", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Representation> representations = new HashSet<>();

    public void addRepresentation(Representation representation) {
        representations.add(representation);
        representation.setOpera(this);
    }

}
