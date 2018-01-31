package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

public class CardFileDTO {

    private String title;

    private String question;

    private String answer;

    public CardFileDTO() {
    }

    public CardFileDTO(String title, String question, String answer) {
        this.title = title;
        this.question = question;
        this.answer = answer;
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
}
