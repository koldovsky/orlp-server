package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "image")
public class Image implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;

    @Column(name = "type")
    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "size")
    @NotNull
    private Long size;

    @Column(name = "is_used")
    private boolean isImageUsed;

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Image(Long id, boolean isImageUsed) {
        this.id = id;
        this.isImageUsed = isImageUsed;
    }

    public Image(String imageBase64) {
        this.imageBase64 = imageBase64;
    }


    public Image(String imageBase64, String type) {
        this.imageBase64 = imageBase64;
        this.type = type;
    }

    public Image(Long id, User createdBy) {
        this.id = id;
        this.createdBy = createdBy;
    }

    public Image(String imageBase64, String type, User createdBy, Long size) {
        this.imageBase64 = imageBase64;
        this.type = type;
        this.createdBy = createdBy;
        this.size = size;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public boolean getIsImageUsed() {
        return isImageUsed;
    }

    public void setIsImageUsed(boolean imageUsed) {
        isImageUsed = imageUsed;
    }

}
