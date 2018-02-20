package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.*;


@Entity
@Table(name = "person")
public class Person implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "first_name", length = PERSON_FIELD_MAX_SIZE)
    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PERSON_FIELD_MIN_SIZE, max = PERSON_FIELD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = "{message.validation.fieldCantContainReservedSymbols}", groups = Request.class)
    private String firstName;

    @Column(name = "last_name", length = PERSON_FIELD_MAX_SIZE)
    @NotNull(message = "{message.validation.fieldNotNull}", groups = Request.class)
    @Size(min = PERSON_FIELD_MIN_SIZE, max = PERSON_FIELD_MAX_SIZE, message = "{message.validation.fieldSizeLimits}", groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = "{message.validation.fieldCantContainReservedSymbols}", groups = Request.class)
    private String lastName;

    @Column(name = "image_type", length = IMAGE_TYPE_MAX_SIZE)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private String image;

    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.LAZY)
    private String imageBase64;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, ImageType imageType, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageType = imageType;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(this.id, person.id);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
}
