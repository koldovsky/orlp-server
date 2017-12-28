package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.utils.dto.EntityInterface;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;

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
    @Enumerated(EnumType.STRING)
    private AuditingAction action;

    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "role", nullable = false)
    private String role;

    public Audit() {
    }

    public Audit(String accountEmail, AuditingAction action, Date time, String ipAddress, String role) {
        this.accountEmail = accountEmail;
        this.action = action;
        this.time = time;
        this.ipAddress = ipAddress;
        this.role = role;
    }

    @Override
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

    public AuditingAction getAction() {
        return action;
    }

    public void setAction(AuditingAction action) {
        this.action = action;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
