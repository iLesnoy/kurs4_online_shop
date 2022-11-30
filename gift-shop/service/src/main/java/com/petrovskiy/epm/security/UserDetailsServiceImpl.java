package com.petrovskiy.epm.security;

import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.petrovskiy.epm.exception.ExceptionCode.NON_EXISTENT_ENTITY;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
        return SecurityUserDetailsBuilder.create(user);
    }

}
