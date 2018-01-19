package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authority_id")
    private Long id;

    @Column(name = "name", length = AUTH_NAME_MAX_SIZE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    List<Account> accounts;

    public Authority(AuthorityName name) {
        this.name = name;
    }

    public Authority() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
