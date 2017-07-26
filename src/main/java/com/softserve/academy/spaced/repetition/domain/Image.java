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

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    private String imagebase64;

    @Column(name = "hash", nullable = false)
    private int hash;

    @Column(name = "type", nullable = false)
    private String type;

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Image(String imagebase64) {
        this.imagebase64 = imagebase64;
    }

    public Image(String imagebase64, int hash) {
        this.imagebase64 = imagebase64;
        this.hash = hash;
    }

    public Image(String imagebase64, int hash, String type) {
        this.imagebase64 = imagebase64;
        this.hash = hash;
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
