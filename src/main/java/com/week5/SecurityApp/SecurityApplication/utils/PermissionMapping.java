package com.week5.SecurityApp.SecurityApplication.utils;

import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.week5.SecurityApp.SecurityApplication.entities.enums.Role;
import jakarta.persistence.metamodel.SetAttribute;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.week5.SecurityApp.SecurityApplication.entities.enums.Permission.*;
import static com.week5.SecurityApp.SecurityApplication.entities.enums.Role.*;

/**
 * The type Permission mapping.
 */
public class PermissionMapping {

    private static final Map<Role, Set<Permission>> map = Map.of(
            USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR, Set.of(POST_CREATE, POST_UPDATE, USER_UPDATE),
            ADMIN, Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE, USER_DELETE, USER_CREATE, POST_DELETE)
    );


    /**
     * Gets authorities for role.
     *
     * @param role the role
     * @return the authorities for role
     */
    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        return map.get(role).stream().
                map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
