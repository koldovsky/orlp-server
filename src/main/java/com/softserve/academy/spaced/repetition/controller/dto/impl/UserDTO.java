package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.domain.*;
import org.springframework.hateoas.Link;

import java.util.HashSet;
import java.util.Set;

public class UserDTO extends DTO<User> {
    public UserDTO(User user, Link link) {
        super(user, link);
    }

    public Long getUserId() {
        return getEntity().getId();
    }

    public String getFirstName() { return getEntity().getPerson().getFirstName(); }

    public String getLastName() { return getEntity().getPerson().getLastName(); }

    public String getEmail() { return getEntity().getAccount().getEmail(); }

    public ImageType getImageType() { return getEntity().getPerson().getImageType();}

    public String getImageBase64() {
        return getEntity().getPerson().getImageBase64();
    }

    public String getImage() {
        return getEntity().getPerson().getImage();
    }

    public AuthenticationType getAuthenticationType() { return getEntity().getAccount().getAuthenticationType();}

    public Set<String> getAuthorities() {
        Set<Authority> authorities = getEntity().getAccount().getAuthorities();
        Set<String> names = new HashSet<>();
        authorities.forEach(authority -> names.add(authority.getName().name()));
        return names;
    }

    public AccountStatus getAccountStatus() { return getEntity().getAccount().getStatus();}

    public boolean isDeactivated() { return getEntity().getAccount().isDeactivated();}
}
