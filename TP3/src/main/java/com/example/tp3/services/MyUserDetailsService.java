package com.example.tp3.services;

import com.example.tp3.models.UserD;
import com.example.tp3.repositories.UserDRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserDRepository userDRepository;

    public MyUserDetailsService(UserDRepository userDRepository) {
        this.userDRepository = userDRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserD user = userDRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER") // Ajoutez des rôles si nécessaire
                .build();
    }
}
