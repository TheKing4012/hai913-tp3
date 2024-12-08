package com.example.tp3.configurations;

import com.example.tp3.models.UserD;
import com.example.tp3.services.MyUserDetailsService;
import com.example.tp3.services.UserService;
import com.example.tp3.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private UserService userService;

    public JWTAuthenticationFilter(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            // Extraire le nom d'utilisateur du token
            String username = jwtUtil.extractUsername(token);

            // Vérifier si le token est valide
            if (jwtUtil.validateToken(token, username)) {
                // Charger l'utilisateur à partir du service utilisateur
                UserD user = userService.getUserByEmail(username);  // Assurez-vous que `findByEmail` existe dans votre service
                if (user != null) {
                    // Si l'utilisateur existe, définir l'authentification dans le contexte de sécurité
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Retirer "Bearer " du début du token
        }
        return null;
    }


}

