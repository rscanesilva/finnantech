package com.finnantech.infrastructure.security;

import com.finnantech.domain.entities.User;
import com.finnantech.domain.ports.UserRepositoryPort;
import com.finnantech.domain.valueobjects.Email;
import com.finnantech.infrastructure.services.JwtServiceAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filtro JWT para autenticação de requisições
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtServiceAdapter jwtService;
    private final UserRepositoryPort userRepository;
    
    public JwtAuthenticationFilter(JwtServiceAdapter jwtService, UserRepositoryPort userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        // Verificar se o header Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extrair o token JWT
        jwt = authHeader.substring(7);
        
        try {
            userEmail = jwtService.extractEmail(jwt);
            
            // Se temos um email e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Buscar usuário no banco
                Optional<User> userOpt = userRepository.findByEmail(Email.of(userEmail));
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    // Verificar se o token é válido
                    if (jwtService.isTokenValid(jwt, userEmail)) {
                        
                        // Criar UserDetails para Spring Security
                        UserDetails userDetails = createUserDetails(user);
                        
                        // Criar token de autenticação
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                            );
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            // Log do erro (em produção, usar logger apropriado)
            logger.error("Erro ao processar token JWT: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail().getValue())
                .password("") // Não precisamos da senha aqui
                .authorities("ROLE_USER")
                .build();
    }
} 