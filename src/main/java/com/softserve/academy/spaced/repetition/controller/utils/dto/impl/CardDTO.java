package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import org.springframework.web.multipart.MultipartFile;

public class CardDTO {

    private String title;

    private String question;

    private String answer;

    private MultipartFile image;

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
}
