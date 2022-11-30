package com.petrovskiy.epm.security;

import com.petrovskiy.epm.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class SecurityUserDetailsBuilder {
    private static final boolean ACTIVE = true;

    private SecurityUserDetailsBuilder() {
    }

    public static UserDetails create(User user) {
        return new AuthUser(
                user.getId(),
                user.getFirstName(),
                user.getPassword(),
                user.getRole().getAuthorities(),
                ACTIVE
        );
    }
}
