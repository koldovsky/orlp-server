package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jarki on 6/28/2017.
 */
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userId;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_course", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "course_id",
                    nullable = false, updatable = false)})
    private List <Course> courses;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deckOwner")
    private List <Deck> listOfCreatedDecks;

    @Column(name = "deleted", columnDefinition = "INT(1) DEFAULT '0'")
    private boolean isDeleted;


    public User() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public List <Course> getCourses() {
        return courses;
    }

    public void setCourses(List <Course> courses) {
        this.courses = courses;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
