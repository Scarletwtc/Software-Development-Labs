package com.example.data.domain;

import jakarta.persistence.*; // for @entity @id @manytoone etc
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "cast")
// include the Person fields in equals/hashCode, but skip cast
public class Singer extends Person {
    private String vocalClassification;

    // this is bidirectional
    @ManyToMany
    private Set<Representation> cast = new HashSet<>();

    public void addRepresentation(Representation representation) {
        cast.add(representation);
        representation.getCast().add(this);
    }
}
