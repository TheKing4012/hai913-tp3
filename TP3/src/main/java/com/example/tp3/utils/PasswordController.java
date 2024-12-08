package com.example.tp3.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordController {

    // Paramètres pour PBKDF2
    private static final int SALT_LENGTH = 16;  // Longueur du sel en octets
    private static final int HASH_LENGTH = 64; // Longueur du hachage
    private static final int ITERATIONS = 10000; // Nombre d'itérations

    /**
     * Génère un hachage sécurisé pour un mot de passe donné.
     *
     * @param password le mot de passe à hacher
     * @return une chaîne encodée contenant le sel et le hachage
     */
    public static String hashPassword(String password) {
        try {
            // Générer un sel aléatoire
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hacher le mot de passe
            byte[] hash = hashWithPBKDF2(password.toCharArray(), salt);

            // Combiner le sel et le hachage dans une chaîne encodée
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    /**
     * Vérifie si un mot de passe correspond à un hachage stocké.
     *
     * @param password       le mot de passe en clair
     * @param storedPassword la chaîne encodée contenant le sel et le hachage
     * @return true si le mot de passe est valide, false sinon
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        try {
            // Séparer le sel et le hachage stocké
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Format de mot de passe stocké invalide");
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // Hacher le mot de passe donné avec le même sel
            byte[] providedHash = hashWithPBKDF2(password.toCharArray(), salt);

            // Comparer les hachages
            return slowEquals(providedHash, expectedHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erreur lors de la vérification du mot de passe", e);
        }
    }

    /**
     * Hache un mot de passe avec PBKDF2.
     *
     * @param password le mot de passe en clair
     * @param salt     le sel utilisé pour le hachage
     * @return le hachage du mot de passe
     */
    private static byte[] hashWithPBKDF2(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }

    /**
     * Compare deux tableaux de bytes de manière sécurisée.
     *
     * @param a premier tableau
     * @param b deuxième tableau
     * @return true si les tableaux sont égaux, false sinon
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}

