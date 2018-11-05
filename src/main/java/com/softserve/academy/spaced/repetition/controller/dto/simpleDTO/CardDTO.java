package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.controller.dto.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

public class CardDTO {
    @Length(max = ValidationConstants.MAX_CARD_TITLE_SIZE)
    @NotBlank
    private String title;

    private String question;
    @NotBlank
    private String answer;
    @Size(max = ValidationConstants.MAX_CARD_IMAGE_LIST_SIZE)
    private List<String> images;

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> image) {
        this.images = image;
    }
}
