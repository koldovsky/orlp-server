package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.dto.Request;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

import static com.softserve.academy.spaced.repetition.service.validators.ValidationConstants.*;


@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "first_name")
    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @Length(min = PERSON_FIELD_MIN_LENGTH, max = PERSON_FIELD_MAX_LENGTH, message = PERSON_FIELD_LENGTH_MESSAGE, groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = SPECIAL_SYMBOLS_PATTERN_MESSAGE, groups = Request.class)
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = NULL_MESSAGE, groups = Request.class)
    @NotEmpty(message = EMPTY_MESSAGE, groups = Request.class)
    @Length(min = PERSON_FIELD_MIN_LENGTH, max = PERSON_FIELD_MAX_LENGTH, message = PERSON_FIELD_LENGTH_MESSAGE, groups = Request.class)
    @Pattern(regexp = SPECIAL_SYMBOLS_PATTERN, message = SPECIAL_SYMBOLS_PATTERN_MESSAGE, groups = Request.class)
    private String lastName;


    @Column(name = "IMAGETYPE", length = 6)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ImageType typeImage;

    private String image;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.LAZY)
    private String imageBase64;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, ImageType typeImage, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.typeImage = typeImage;
        this.image = image;
    }

    public long getId() {
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

    public ImageType getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(ImageType typeImage) {
        this.typeImage = typeImage;
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
