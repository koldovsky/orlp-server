package com.softserve.academy.spaced.repetition.controller.utils.dto;

import org.springframework.hateoas.Link;
import org.springframework.web.multipart.MultipartFile;

public class CardDTO {

    private String title;

    private String question;

    private String answer;

    private MultipartFile image;

    private Integer cardId;

    private Integer rating;

    private Link self;


    public CardDTO() {
    }

    public CardDTO(String title, String question, String answer, MultipartFile image) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Link getSelf() {
        return self;
    }

    public void setSelf(Link self) {
        this.self = self;
    }
}
