package com.oliiarchyk.campaigns.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "campaigns")
public class Campaign implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotNull
    private String campaign;

    @NotEmpty
    @NotNull
    private String ranking;

    @NotEmpty
    @NotNull
    private LocalDate updateAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "campId", referencedColumnName = "id")
    List<Predicate> predicates = new ArrayList<>();
}