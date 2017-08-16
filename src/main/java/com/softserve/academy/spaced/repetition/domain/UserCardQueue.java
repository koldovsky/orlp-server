package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "user_card_queue")
public class UserCardQueue implements EntityInterface{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_card_queue_id")
    private Long id;

    @Column(name = "account_email",nullable = false)
    private String accountEmail;

    @Column(name = "card_id", nullable = false)
    private long cardId;

    @Column(name = "deck_id", nullable = false)
    private long deckId;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserCardQueueStatus status;

    @Column(name = "card_data")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date cardDate;

    public UserCardQueue() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public UserCardQueueStatus getStatus() {
        return status;
    }

    public void setStatus(UserCardQueueStatus status) {
        this.status = status;
    }

    public Date getCardDate() {
        return cardDate;
    }

    public void setCardDate(Date cardDate) {
        this.cardDate = cardDate;
    }
}
