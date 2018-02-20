package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "remembering_level")
public class RememberingLevel implements EntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "order_number")
    private Integer orderNumber;

    private String name;

    @NotNull
    @Column(name = "number_of_postponed_days")
    private Integer numberOfPostponedDays;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    public RememberingLevel() {
    }

    public RememberingLevel(Integer orderNumber, String name, Integer numberOfPostponedDays, Account account) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.numberOfPostponedDays = numberOfPostponedDays;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RememberingLevel that = (RememberingLevel) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
