package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.dto.EntityInterface;
import com.softserve.academy.spaced.repetition.dto.Request;
import com.softserve.academy.spaced.repetition.service.validators.EmailExistsAnnotation.EmailExists;
import com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation.PasswordMatches;
import com.sun.org.apache.regexp.internal.RE;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.*;

@Entity
@Table(name = "account")
public class Account implements EntityInterface {
    public static final int INITIAL_CARDS_NUMBER = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false)
    private Long id;

    @Column(name = "password", nullable = false)
    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_MESSAGE, groups = Request.class)
    @PasswordMatches(groups = Request.class)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @Pattern(regexp = EMAIL_PATTERN, message = EMAIL_PATTERN_MESSAGE, groups = Request.class)
    @EmailExists(groups = Request.class)
    private String email;

    @Column(name = "AUTHENTICATIONTYPE", length = 8)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

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

    @Column(name = "cards_number", columnDefinition = "int default 10")
    @NotNull
    private Integer cardsNumber;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<RememberingLevel> rememberingLevels;

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
