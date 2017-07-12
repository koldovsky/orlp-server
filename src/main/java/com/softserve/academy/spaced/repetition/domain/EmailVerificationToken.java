package com.softserve.academy.spaced.repetition.domain;


import javax.persistence.*;

@Entity
@Table(name = "email_token")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_token_id", nullable = false)
    private long tokenId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "token")
    private String emailToken;

    public EmailVerificationToken(Account account, String emailToken) {
        this.account = account;
        this.emailToken = emailToken;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }
}
