package com.example.tp3.repositories;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenRepository implements TokenRepository {

    // Stockage des tokens en mémoire
    private final ConcurrentHashMap<String, String> tokenStorage = new ConcurrentHashMap<>();

    // Sauvegarde du token avec l'ID de l'utilisateur
    @Override
    public void saveToken(String token, String userId) {
        tokenStorage.put(token, userId);
    }

    // Récupération de l'ID de l'utilisateur à partir du token
    @Override
    public String getUserIdByToken(String token) {
        return tokenStorage.get(token);  // Retourne null si le token n'est pas trouvé
    }

    // Suppression du token
    @Override
    public void deleteToken(String token) {
        tokenStorage.remove(token);
    }
}

