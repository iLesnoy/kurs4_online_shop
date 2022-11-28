package com.petrovskiy.epm.security;

import com.petrovskiy.epm.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUserDetailsBuilder {
    private static final boolean ACTIVE = true;

    private SecurityUserDetailsBuilder() {
    }

    public static UserDetails create(User user) {
        return new JwtUser(
                user.getId(),
                user.getFirstName(),
                user.getPassword(),
                user.getRole().getAuthorities(),
                ACTIVE
        );
    }
}
