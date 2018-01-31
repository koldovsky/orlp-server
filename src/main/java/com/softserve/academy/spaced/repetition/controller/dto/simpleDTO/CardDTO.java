package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import java.util.List;

public class CardDTO {

    private String title;
    private String question;
    private String answer;
    private List<String> images;
    private Integer cardId;
    private Integer rating;

    public CardDTO() {
    }

    public CardDTO(String title, String question, String answer, List<String> images) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.images = images;
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

    public List<String> getImage() {
        return images;
    }

    public void setImage(List<String> image) {
        this.images = image;
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
}
