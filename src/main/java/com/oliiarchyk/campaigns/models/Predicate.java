package com.oliiarchyk.campaigns.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "predicates")
public class Predicate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long campId;

    public enum Type {
        type1, type2, type3, type4
    }

    private String attribute;
    private String conditions;

    private Predicate.Type type;

}