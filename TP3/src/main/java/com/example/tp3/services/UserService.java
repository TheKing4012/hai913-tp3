package com.example.tp3.services;

import com.example.tp3.models.UserD;
import com.example.tp3.repositories.UserDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDRepository userRepository;

    public UserD getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Autowired
    private UserDRepository userDRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserD connectUser(String email, String password) {
        Optional<UserD> user = userRepository.findByEmail(email);

        if (user != null && user.isPresent()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, user.get().getPassword())) {
                return user.get(); // Connexion réussie si les mots de passe correspondent
            }
        }
        return null; // Échec de la connexion si l'utilisateur n'existe pas ou les mots de passe ne correspondent pas
    }

    public UserD createUser(UserD user) {
        if (userDRepository.existsByEmail(user.getEmail())) {
            return null; // Utilisateur déjà existant
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash du mot de passe
        return userDRepository.save(user);
    }
}
