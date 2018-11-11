package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "transaction_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reference_id")
    private Transaction reference;

    @Column(name = "date_creation")
    private Date date;

    public Transaction getReference() {
        return reference;
    }

    public void setReference(Transaction reference) {
        this.reference = reference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
