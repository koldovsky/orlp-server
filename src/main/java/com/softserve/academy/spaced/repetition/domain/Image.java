package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "image")
public class Image implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private long id;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    private String imagebase64;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "size", nullable = false)
    private Long size;

    @Type(type= "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_used")
    private boolean isUsed;

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Image(Long id, boolean isUsed) {
        this.id = id;
        this.isUsed = isUsed;
    }

    public Image(String imagebase64) {
        this.imagebase64 = imagebase64;
    }


    public Image(String imagebase64, String type) {
        this.imagebase64 = imagebase64;
        this.type = type;
    }

    public Image(Long id, User createdBy) {
        this.id = id;
        this.createdBy = createdBy;
    }

    public Image(String imagebase64, String type, User createdBy, Long size) {
        this.imagebase64 = imagebase64;
        this.type = type;
        this.createdBy = createdBy;
        this.size = size;
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

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Image image = (Image) o;

        return Objects.equals(this.id, image.id);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
