package com.cursor.library.services;

import com.cursor.library.entity.User;
import com.cursor.library.repository.UserRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    static AccessDeniedException ACCESS_DENIED = new AccessDeniedException("Access denied");

    public UserService(UserRepo userRepo, @Lazy BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByUsername(s).orElseThrow();
    }

    public UserDetails login(String username, String password) throws AccessDeniedException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> ACCESS_DENIED);
        if (!encoder.matches(password, user.getPassword())) {
            throw ACCESS_DENIED;
        }
        return user;
    }
}
