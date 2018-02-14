package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.utils.validators.annotations.EmailNotUsed;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;

@Entity
@Table(name = "account")
public class Account implements EntityInterface {
    public static final int INITIAL_CARDS_NUMBER = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "password", length = PASSWORD_MAX_SIZE_HASH)
    @NotNull
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    private String password;

    @Column(name = "email", unique = true, length = EMAIL_MAX_SIZE)
    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = EMAIL_MIN_SIZE, max = EMAIL_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = EMAIL_PATTERN, message = "{message.validation.emailWrongFormat}", groups = Request.class)
    @EmailNotUsed(groups = Request.class)
    private String email;

    @Column(name = "authentication_type", length = AUTH_TYPE_MAX_SIZE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "deactivated")
    @NotNull
    private boolean deactivated;

    @Column(name = "last_password_reset_date")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_authority", joinColumns = {
            @JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    private Set<Authority> authorities;

    @Column(name = "learning_regime",
            columnDefinition = "varchar(45) default 'CARDS_POSTPONING_USING_SPACED_REPETITION'")
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private LearningRegime learningRegime;

    @Column(name = "cards_number", columnDefinition = "int default 10")
    @NotNull
    @Min(value = MIN_NUMBER_OF_CARDS, message = "{message.exception.numbersOfCardsNegative}", groups = Request.class)
    private Integer cardsNumber;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<RememberingLevel> rememberingLevels;

    public Account() {
    }

    public Account(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public Account(String password, String email, Date lastPasswordResetDate,
                   Set<Authority> authorities, AccountStatus accountStatus) {
        this.password = password;
        this.email = email;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
        this.status = accountStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
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

    public Integer getCardsNumber() {
        return cardsNumber;
    }

    public void setCardsNumber(Integer cardsNumber) {
        this.cardsNumber = cardsNumber;
    }

    public List<RememberingLevel> getRememberingLevels() {
        return rememberingLevels;
    }

    public void setRememberingLevels(List<RememberingLevel> rememberingLevels) {
        this.rememberingLevels = rememberingLevels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        return id != null ? id.equals(account.id) : account.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
