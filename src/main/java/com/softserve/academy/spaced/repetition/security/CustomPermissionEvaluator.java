package com.softserve.academy.spaced.repetition.security;

import com.softserve.academy.spaced.repetition.domain.EntityName;
import com.softserve.academy.spaced.repetition.domain.Permission;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.softserve.academy.spaced.repetition.domain.enums.AuthorityName.ROLE_ADMIN;
import static com.softserve.academy.spaced.repetition.domain.enums.AuthorityName.ROLE_ANONYMOUS;
import static com.softserve.academy.spaced.repetition.domain.enums.AuthorityName.ROLE_USER;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    //TODO move to custom Permission
    public static final Integer CREATE = 0b0001;

    public static final Integer READ = 0b0010;

    public static final Integer UPDATE = 0b0100;

    public static final Integer DELETE = 0b1000;

    //Collection for permission matrix
    //In future will be replaced by using DB
    private static final Map<AuthorityName, Set<Permission>> permissionMatrix = new HashMap<>();

    static {
        Set<Permission> anonymousPermission = new HashSet<>();
        Set<Permission> userPermission = new HashSet<>();
        Set<Permission> adminPermission = new HashSet<>();

        //Collection of permissions for role ANONYMOUS
        anonymousPermission.add(new Permission(new EntityName("DeckComment"), 0b0010));

        //Collection of permissions for role USER
        userPermission.add(new Permission(new EntityName("DeckComment"), 0b1111));

        //Collection of permissions for role ADMIN
        adminPermission.add(new Permission(new EntityName("DeckComment"), 0b1111));

        permissionMatrix.put(ROLE_ANONYMOUS, anonymousPermission);
        permissionMatrix.put(ROLE_USER, userPermission);
        permissionMatrix.put(ROLE_ADMIN, adminPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        System.out.println("first **********");//just for temporary debugging
        if ((targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        Integer permissionMask = decodeStringToMask((String) permission);

//        check on ownership will be used in future
//        if (!authentication.getAuthorities().contains(ROLE_ADMIN) && (permissionMask & (UPDATE | DELETE)) != 0) {
//            return isOwnerOperation(authentication, (EntityClass) targetDomainObject);
//        }
        System.out.println(authentication.getAuthorities());
        System.out.println(permissionMask);
        return hasPrivilege(authentication, (String) targetDomainObject, permissionMask);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        System.out.println("second **********");//just for temporary debugging
        if ((targetType == null) || !(permission instanceof String)) {
            return false;
        }
        Integer permissionMask = decodeStringToMask((String) permission);
        return hasPrivilege(authentication, targetType, permissionMask);
    }

    private boolean hasPrivilege(Authentication authentication, String targetType, Integer operation) {
        for (AuthorityName authority : permissionMatrix.keySet()) {
            for (Permission permission : permissionMatrix.get(authority)) {
                if (authentication.getAuthorities().contains(authority) && permission.getEntityName().toString().equals(targetType)) {

                    if ((permission.getPermissionMask() & operation) != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

//    will be used in future
//    private boolean isOwnerOperation(Authentication authentication, EntityClass entityClass) {
//            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
//        return jwtUser.getId() == entityClass.getCreatedBy();
//    }

    private Integer decodeStringToMask(String operation) {
        switch (operation) {
            case "CREATE":
                return CREATE;
            case "READ":
                return READ;
            case "UPDATE":
                return UPDATE;
            case "DELETE":
                return DELETE;
            default:
                return 0;
        }
    }

}
