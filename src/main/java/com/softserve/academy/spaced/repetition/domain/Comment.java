package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.utils.dto.EntityInterface;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.EMPTY_MESSAGE;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Comment implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @NotBlank(message = EMPTY_MESSAGE, groups = Request.class)
    @Column(name = "commentText", nullable = false, columnDefinition = "LONGTEXT")
    private String commentText;

    @Column(name = "comment_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "parentCommentId")
    private Long parentCommentId;

    public Comment() {
    }

    public Comment(String commentText, Date commentDate) {
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(this.id, comment.id);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
}
