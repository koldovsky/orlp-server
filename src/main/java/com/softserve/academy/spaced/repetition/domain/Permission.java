package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;

public class Permission {

    private Long id;

    private Integer permissionMask;

    private Authority authority;

    private EntityName entityName;

    public Permission() {
    }

    public Permission(EntityName entityName, Integer permissionMask) {
        this.entityName = entityName;
        this.permissionMask = permissionMask;
    }

    public Integer getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(Integer permissionMask) {
        this.permissionMask = permissionMask;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public EntityName getEntityName() {
        return entityName;
    }

    public void setEntityName(EntityName entityName) {
        this.entityName = entityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
