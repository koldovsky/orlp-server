package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.domain.Permission;
import com.softserve.academy.spaced.repetition.domain.enums.Operations;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.softserve.academy.spaced.repetition.domain.enums.AuthorityName.*;
import static com.softserve.academy.spaced.repetition.domain.enums.EntityName.*;
import static com.softserve.academy.spaced.repetition.domain.enums.Operations.*;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    //Collection for permission matrix
    //In future will be replaced by using DB
    //TODO create entity in DB for Permission matrix
    private static final Map<String, Set<Permission>> permissionMatrix = new HashMap<>();

    static {
        Set<Permission> anonymousPermission = new HashSet<>();
        Set<Permission> userPermission = new HashSet<>();
        Set<Permission> adminPermission = new HashSet<>();

        //Collection of permissions for role ANONYMOUS
        anonymousPermission.add(new Permission(CARD, createMask(READ)));
        anonymousPermission.add(new Permission(CARD_RATING, createMask(READ)));
        anonymousPermission.add(new Permission(CATEGORY, createMask(READ)));
        anonymousPermission.add(new Permission(COURSE, createMask(READ)));
        anonymousPermission.add(new Permission(IMAGE, createMask(READ)));
        anonymousPermission.add(new Permission(COURSE_COMMENT, createMask(READ)));
        anonymousPermission.add(new Permission(COURSE_RATING, createMask(READ)));
        anonymousPermission.add(new Permission(DECK_COMMENT, createMask(READ)));
        anonymousPermission.add(new Permission(DECK, createMask(READ)));
        anonymousPermission.add(new Permission(DECK_RATING, createMask(READ)));

        //Collection of permissions for role USER
        userPermission.add(new Permission(CARD, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(CARD_IMAGE, createMask(DELETE)));
        userPermission.add(new Permission(CARD_RATING, createMask(READ, CREATE)));
        userPermission.add(new Permission(DECK_IMPORT, createMask(CREATE, READ)));
        userPermission.add(new Permission(CATEGORY, createMask(READ)));
        userPermission.add(new Permission(COURSE, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(COURSE_COMMENT, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(COURSE_RATING, createMask(CREATE, READ)));
        userPermission.add(new Permission(DECK_COMMENT, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(DECK, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(IMAGE, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(DECK_RATING, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(FOLDER, createMask(CREATE, READ, UPDATE, DELETE)));
        userPermission.add(new Permission(CARD_QUEUE, createMask(READ, UPDATE)));
        userPermission.add(new Permission(USER, createMask(READ, UPDATE)));
        userPermission.add(new Permission(PROFILE, createMask(READ, UPDATE, DELETE)));
        userPermission.add(new Permission(PROFILE_LEARNING, createMask(READ, UPDATE)));

        //Collection of permissions for role ADMIN
        adminPermission.add(new Permission(AUDIT, createMask(READ)));
        adminPermission.add(new Permission(ADMIN_CARD, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(CARD, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(CARD_RATING, createMask(CREATE, READ)));
        adminPermission.add(new Permission(CARD_IMAGE, createMask(DELETE)));
        adminPermission.add(new Permission(DECK_IMPORT, createMask(CREATE, READ)));
        adminPermission.add(new Permission(CATEGORY, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(COURSE, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(COURSE_COMMENT, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(COURSE_RATING, createMask(CREATE, READ)));
        adminPermission.add(new Permission(DECK, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(IMAGE, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(ADMIN_DECK, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(DECK_COMMENT, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(DECK_RATING, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(FOLDER, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(MANAGE_USER, createMask(CREATE, READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(USER, createMask(READ, UPDATE)));
        adminPermission.add(new Permission(CARD_QUEUE, createMask(READ, UPDATE)));
        adminPermission.add(new Permission(PROFILE, createMask(READ, UPDATE, DELETE)));
        adminPermission.add(new Permission(PROFILE_LEARNING, createMask(READ, UPDATE)));

        permissionMatrix.put(ROLE_ANONYMOUS.name(), anonymousPermission);
        permissionMatrix.put(ROLE_USER.name(), userPermission);
        permissionMatrix.put(ROLE_ADMIN.name(), adminPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return hasPrivilege(authentication, (String) targetDomainObject, Operations.valueOf((String) permission).getMask());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPrivilege(authentication, targetType, Operations.valueOf((String) permission).getMask());
    }

    private boolean hasPrivilege(Authentication authentication, String targetType, int operation) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            for (Permission permission : permissionMatrix.get(grantedAuthority.getAuthority())) {
                if (permission.getEntityName().name().equals(targetType) && (permission.getPermissionMask() & operation) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
