package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audit")
public class Audit implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audit_id")
    private long id;

    @Column(name = "accountEmail", nullable = false)
    private String accountEmail;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public Audit() {
    }

    public Audit(String accountEmail, String action, Date time) {
        this.accountEmail = accountEmail;
        this.action = action;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", accountEmail='" + accountEmail + '\'' +
                ", action='" + action + '\'' +
                ", time=" + time +
                '}';
    }
}
