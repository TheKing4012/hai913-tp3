package com.example.tp3.repositories;

public interface TokenRepository {
    // Sauvegarder un token avec l'ID de l'utilisateur
    void saveToken(String token, String userId);

    // Récupérer l'ID de l'utilisateur à partir du token
    String getUserIdByToken(String token);

    // Supprimer un token (par exemple, pour une déconnexion)
    void deleteToken(String token);
}

