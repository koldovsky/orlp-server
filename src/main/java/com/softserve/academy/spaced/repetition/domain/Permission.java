package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.domain.enums.EntityName;

public class Permission {

    private Integer permissionMask;

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

    public EntityName getEntityName() {
        return entityName;
    }

    public void setEntityName(EntityName entityName) {
        this.entityName = entityName;
    }

}
