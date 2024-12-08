package com.example.tp3.controllers;

import com.example.tp3.exceptions.UserNotFoundException;
import com.example.tp3.models.*;
import com.example.tp3.repositories.UserDRepository;
import com.example.tp3.services.UserService;
import com.example.tp3.utils.JWTUtil;
import com.example.tp3.utils.PasswordController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserDController {

	@Autowired
	private UserDRepository repository;
	@Autowired
	private UserService userService;
	@Autowired
	private JWTUtil jwtUtil; // Ajout de JWTUtil
	private static final String uri = "userservice/api";
    @Autowired
    private PasswordEncoder passwordEncoder;

	@GetMapping(uri + "/users")
	public List<UserD> getAllUsers() {
		return repository.findAll();
	}

	@GetMapping(uri + "/users/count")
	public String count() {
		return String.format("{\"%s\": %d}", "count", repository.count());
	}

	@GetMapping(uri + "/users/{id}")
	public UserD getUserById(@PathVariable long id) throws UserNotFoundException {
		return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Erreur : impossible de trouver une chambre avec cet ID"));
	}

	@GetMapping("/users/email/{email}")
	public UserD getUserByEmail(@PathVariable String email) {
		return userService.getUserByEmail(email);
	}

	@PostMapping(uri + "/connect")
	public ResponseEntity<?> connect(@RequestBody LoginRequest loginRequest) {
		System.out.println("Requête de connexion reçue pour email: " + loginRequest.getEmail());
		UserD user = userService.connectUser(loginRequest.getEmail(), loginRequest.getPassword());
		if (user != null) {
			String token = jwtUtil.generateToken(user.getEmail());
			user.setToken(token);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(401).body("Échec de l'authentification");
		}
	}


	@PostMapping(uri + "/create")
	public ResponseEntity<UserD> createUser(@RequestBody UserD newUser) {
		// Vérifier si l'email existe déjà
		if (repository.existsByEmail(newUser.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Email déjà pris
		}

		// Hacher le mot de passe de l'utilisateur
		String hashedPassword = passwordEncoder.encode(newUser.getPassword());
		newUser.setPassword(hashedPassword);

		// Générer un token JWT
		String token = jwtUtil.generateToken(newUser.getEmail());
		newUser.setToken(token);

		// Sauvegarder l'utilisateur dans la base de données
		UserD savedUser = repository.save(newUser);

		// Retourner l'utilisateur avec le token
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}


	@PutMapping(uri + "/users/{id}")
	public UserD updateUser(@RequestBody UserD newUserD, @PathVariable long id, @RequestHeader("Authorization") String token) {
		if (token == null || !jwtUtil.validateToken(token.replace("Bearer ", ""), newUserD.getEmail())) {
			throw new RuntimeException("Token invalide ou expiré");
		}

		return repository.findById(id).map(userD -> {
			userD.setNom(newUserD.getNom());
			userD.setEmail(newUserD.getEmail());
			return userD;
		}).orElseGet(() -> {
			newUserD.setId(id);
			newUserD.setPassword(PasswordController.hashPassword(newUserD.getPassword()));
			repository.save(newUserD);
			return newUserD;
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(uri + "/users/{id}")
	public void deleteUser(@PathVariable long id, @RequestHeader("Authorization") String token) throws UserNotFoundException {
		if (token == null || !jwtUtil.validateToken(token.replace("Bearer ", ""), null)) {
			throw new RuntimeException("Token invalide ou expiré");
		}

		UserD userD = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Erreur : impossible de trouver un utilisateur avec cet ID"));
		repository.delete(userD);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, null);
		return ResponseEntity.ok("Déconnecté avec succès");
	}

	// Classe interne pour retourner l'utilisateur avec le token
	public static class LoginResponse {
		private UserD user;
		private String token;

		public LoginResponse(UserD user, String token) {
			this.user = user;
			this.token = token;
		}

		public UserD getUser() {
			return user;
		}

		public String getToken() {
			return token;
		}
	}
}
