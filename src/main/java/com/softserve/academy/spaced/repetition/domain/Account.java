package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false)
    private long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastPasswordResetDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_authority", joinColumns = {
            @JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    private Set<Authority> authorities;

    @NotNull
    @Column(name = "learning_regime",
            columnDefinition = "varchar(45) default 'CARDS_POSTPONING_USING_SPACED_REPETITION'")
    @Enumerated(value = EnumType.STRING)
    private LearningRegime learningRegime;

    public Account() {
    }

    public Account(String email) {
        this.email = email;
    }

    public Account(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public Account(String password, String email, Date lastPasswordResetDate, Set<Authority> authorities) {
        this.password = password;
        this.email = email;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
    }

    public Account(String password, String email, Date lastPasswordResetDate, Set<Authority> authorities, AccountStatus accountStatus) {
        this.password = password;
        this.email = email;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
        this.status = accountStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LearningRegime getLearningRegime() {
        return learningRegime;
    }

    public void setLearningRegime(LearningRegime learningRegime) {
        this.learningRegime = learningRegime;
    }
}
