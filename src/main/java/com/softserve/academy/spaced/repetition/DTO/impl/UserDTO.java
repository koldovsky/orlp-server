package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Authority;
import com.softserve.academy.spaced.repetition.domain.AuthorityName;
import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.hateoas.Link;

import java.util.HashSet;
import java.util.Set;

public class UserDTO extends DTO<User> {
    public UserDTO(User user, Link link) {
        super(user, link);
    }

    public String getFirstName() { return getEntity().getPerson().getFirstName(); }

    public String getLastName() { return getEntity().getPerson().getLastName(); }

    public String getEmail() { return getEntity().getAccount().getEmail(); }

    public String getImage() {
        return getEntity().getPerson().getImage();
    }

    public Set<String> getAuthorities() {
        Set<Authority> authorities = getEntity().getAccount().getAuthorities();
        Set<String> names = new HashSet<>();
        authorities.forEach(authority -> names.add(authority.getName().name()));
        return names;
    }

}
