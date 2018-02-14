package com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import org.springframework.hateoas.Link;

public class ProfileDataDTO extends DTO<User> {

    public ProfileDataDTO(User user, Link self) {
        super(user, self);
    }

    public String getFirstName() {
        return getEntity().getPerson().getFirstName();
    }

    public String getLastName() {
        return getEntity().getPerson().getLastName();
    }

    public String getImageBase64() {
        return getEntity().getPerson().getImageBase64();
    }

    public String getEmail() {
        return getEntity().getAccount().getEmail();
    }

    public AuthenticationType getAuthenticationType() {
        return getEntity().getAccount().getAuthenticationType();
    }
}
