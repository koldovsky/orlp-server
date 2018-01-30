package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.List;

public class EntityName{

    private Long id;

    private String name;

    private List<Permission> permissions;

    public EntityName() {
    }

    public EntityName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
