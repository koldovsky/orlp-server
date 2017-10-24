package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "user_card_queue")
public class UserCardQueue implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_card_queue_id")
    private Long id;

    @Column(name = "account_email", nullable = false)
    private String accountEmail;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "deck_id", nullable = false)
    private Long deckId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserCardQueueStatus status;

    @Column(name = "card_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date cardDate;

    @Column(name = "date_to_repeat")
    private Date dateToRepeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remembering_level_id")
    private RememberingLevel rememberingLevel;

    public UserCardQueue() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
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

    public Date getDateToRepeat() {
        return dateToRepeat;
    }

    public void setDateToRepeat(Date dateToRepeat) {
        this.dateToRepeat = dateToRepeat;
    }

    public RememberingLevel getRememberingLevel() {
        return rememberingLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserCardQueue that = (UserCardQueue) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setRememberingLevel(RememberingLevel rememberingLevel) {
        this.rememberingLevel = rememberingLevel;
    }
}
