package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
    @TableGenerator(name = "generator", allocationSize = 1)
    @Column(name = "transaction_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reference_id")
    private Transaction reference;

    @Column(name = "date_creation")
    private Date creationDate;

    public Transaction getReference() {
        return reference;
    }

    public void setReference(Transaction reference) {
        this.reference = reference;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
