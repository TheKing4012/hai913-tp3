package com.example.tp3.logger;

import java.util.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;

public class LogParser {

    // Motif pour analyser les logs
    private static final Pattern logPattern = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) \\[.*\\] (INFO) .* - User: UserD\\{id=(\\d+), nom='([^']+)', email='([^']+)'\\} performed a (SEARCH|WRITE|READ) operation on method: ([^\\s]+)"
    );

    // Classe représentant un utilisateur
    public static class UserLogImpl {
        private int id;
        private String name;
        private String email;
        private Map<String, Integer> eventCount; // Nombre d'occurrences par action (READ, WRITE, etc.)
        private Map<String, MethodUsage> methodUsageCount; // Utilisation des méthodes avec les timestamps
        private String lastActionTimestamp; // Dernier timestamp d'action

        // Constructeur
        public UserLogImpl(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.eventCount = new HashMap<>();
            this.methodUsageCount = new HashMap<>();
            this.lastActionTimestamp = null; // Initialement null
        }

        // Ajouter un timestamp pour l'action et la méthode
        public void addOperation(String timestamp, String action, String method) {
            lastActionTimestamp = timestamp; // Dernière action
            eventCount.put(action, eventCount.getOrDefault(action, 0) + 1);

            // Ajouter le timestamp pour la méthode spécifique
            methodUsageCount.computeIfAbsent(method, k -> new MethodUsage())
                    .addTimestamp(timestamp);
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public Map<String, Integer> getEventCount() {
            return eventCount;
        }

        public Map<String, MethodUsage> getMethodUsageCount() {
            return methodUsageCount;
        }

        public String getLastActionTimestamp() {
            return lastActionTimestamp;
        }

        @Override
        public String toString() {
            return "UserLogImpl{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", eventCount=" + eventCount +
                    ", methodUsageCount=" + methodUsageCount +
                    ", lastActionTimestamp='" + lastActionTimestamp + '\'' +
                    '}';
        }
    }

    // Classe représentant une entrée de log
    public static class LogEntry {
        private String timestamp;
        private UserLogImpl user;
        private String action;
        private String method;

        // Constructeur privé
        private LogEntry(LogEntryBuilder builder) {
            this.timestamp = builder.timestamp;
            this.user = builder.user;
            this.action = builder.action;
            this.method = builder.method;
        }

        // Getters et ToString() pour afficher
        public String getTimestamp() {
            return timestamp;
        }

        public UserLogImpl getUser() {
            return user;
        }

        public String getAction() {
            return action;
        }

        public String getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "LogEntry [timestamp=" + timestamp + ", user=" + user + ", action=" + action + ", method=" + method + "]";
        }

        // Builder pour la création de LogEntry
        public static class LogEntryBuilder {
            private String timestamp;
            private UserLogImpl user;
            private String action;
            private String method;

            public LogEntryBuilder timestamp(String timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public LogEntryBuilder user(UserLogImpl user) {
                this.user = user;
                return this;
            }

            public LogEntryBuilder action(String action) {
                this.action = action;
                return this;
            }

            public LogEntryBuilder method(String method) {
                this.method = method;
                return this;
            }

            public LogEntry build() {
                return new LogEntry(this);
            }
        }
    }

    // Classe pour suivre l'utilisation des méthodes avec leurs timestamps
    public static class MethodUsage {
        private int count;
        private List<String> timestamps;

        public MethodUsage() {
            this.count = 0;
            this.timestamps = new ArrayList<>();
        }

        public void addTimestamp(String timestamp) {
            count++;
            timestamps.add(timestamp);
        }

        public int getCount() {
            return count;
        }

        public List<String> getTimestamps() {
            return timestamps;
        }

        @Override
        public String toString() {
            return "MethodUsage{" +
                    "count=" + count +
                    ", timestamps=" + timestamps +
                    '}';
        }
    }

    // Méthode pour parser les logs
    public static LogEntry parseLog(String logLine, Map<Integer, UserLogImpl> users) {
        Matcher matcher = logPattern.matcher(logLine);

        if (matcher.find()) {
            String timestamp = matcher.group(1);
            int userId = Integer.parseInt(matcher.group(3));
            String userName = matcher.group(4);
            String userEmail = matcher.group(5);
            String action = matcher.group(6);
            String method = matcher.group(7);

            // Récupérer ou créer l'utilisateur
            UserLogImpl user = users.computeIfAbsent(userId, id -> new UserLogImpl(userId, userName, userEmail));

            // Ajouter l'opération avec timestamp
            user.addOperation(timestamp, action, method);

            return new LogEntry.LogEntryBuilder()
                    .timestamp(timestamp)
                    .user(user)
                    .action(action)
                    .method(method)
                    .build();
        } else {
            throw new IllegalArgumentException("Log line format incorrect.");
        }
    }

    // Méthode pour lire les logs depuis un fichier et les parser
    public static Map<Integer, UserLogImpl> parseLogsFromFile(String filePath) {
        Map<Integer, UserLogImpl> users = new HashMap<>(); // Regrouper les utilisateurs par ID
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    parseLog(line, users);
                } catch (IllegalArgumentException e) {
                    System.err.println("Erreur de format de log: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier de logs: " + e.getMessage());
        }
        return users;
    }

    // Sauvegarder les profils dans un fichier JSON
    public static void saveProfilesToJson(Map<Integer, UserLogImpl> users, String outputFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), users.values());
            System.out.println("Les profils ont été sauvegardés dans le fichier JSON.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des profils au format JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Lecture et analyse des logs depuis app.log
        Map<Integer, UserLogImpl> users = parseLogsFromFile("logs/app.log");

        // Sauvegarder les profils dans un fichier JSON
        saveProfilesToJson(users, "logs/user_profiles.json");

        // Afficher les profils pour vérification
        users.values().forEach(System.out::println);
    }
}
