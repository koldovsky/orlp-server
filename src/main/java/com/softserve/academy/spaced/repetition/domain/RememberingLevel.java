package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "remembering_level")
public class RememberingLevel implements EntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @NotNull
    @Column(name = "number_of_postponed_days")
    private Integer numberOfPostponedDays;

    public RememberingLevel() {
    }

    public RememberingLevel(String name, Integer numberOfPostponedDays) {
        this.name = name;
        this.numberOfPostponedDays = numberOfPostponedDays;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfPostponedDays() {
        return numberOfPostponedDays;
    }

    public void setNumberOfPostponedDays(Integer numberOfPostponedDays) {
        this.numberOfPostponedDays = numberOfPostponedDays;
    }
}
