package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    private String imagebase64;

    @Column(name = "hash", nullable = false)
    private int hash;

    public Image() {
    }

    public Image(String name, String imagebase64) {
        this.name = name;
        this.imagebase64 = imagebase64;
    }

    public Image(String name, String imagebase64, int hash) {
        this.name = name;
        this.imagebase64 = imagebase64;
        this.hash = hash;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagebase64() {
        return imagebase64;
    }

    public void setImagebase64(String imagebase64) {
        this.imagebase64 = imagebase64;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
