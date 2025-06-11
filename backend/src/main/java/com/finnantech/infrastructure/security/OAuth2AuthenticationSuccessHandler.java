package com.finnantech.infrastructure.security;

import com.finnantech.domain.entities.AuthProvider;
import com.finnantech.domain.entities.User;
import com.finnantech.domain.ports.JwtServicePort;
import com.finnantech.domain.ports.UserRepositoryPort;
import com.finnantech.domain.valueobjects.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

/**
 * Handler para tratar sucesso de autenticação OAuth2
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final UserRepositoryPort userRepository;
    private final JwtServicePort jwtService;
    
    @Value("${cors.allowed-origins[0]:http://localhost:3001}")
    private String frontendUrl;
    
    public OAuth2AuthenticationSuccessHandler(UserRepositoryPort userRepository, 
                                            JwtServicePort jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        try {
            // Extrair informações do usuário OAuth2
            String email = extractEmail(oauth2User);
            String name = extractName(oauth2User);
            String providerId = extractProviderId(oauth2User);
            AuthProvider provider = determineProvider(request);
            
            // Buscar ou criar usuário
            User user = findOrCreateUser(email, name, provider, providerId);
            
            // Gerar token JWT
            String token = jwtService.generateToken(user);
            
            // Redirecionar para o frontend com o token
            String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/dashboard")
                    .queryParam("token", token)
                    .build().toUriString();
            
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
        } catch (Exception e) {
            logger.error("Erro durante autenticação OAuth2: " + e.getMessage(), e);
            
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/login")
                    .queryParam("error", "oauth2_error")
                    .build().toUriString();
            
            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }
    
    private String extractEmail(OAuth2User oauth2User) {
        return oauth2User.getAttribute("email");
    }
    
    private String extractName(OAuth2User oauth2User) {
        String name = oauth2User.getAttribute("name");
        if (name == null || name.isEmpty()) {
            // Fallback para Google
            String givenName = oauth2User.getAttribute("given_name");
            String familyName = oauth2User.getAttribute("family_name");
            if (givenName != null && familyName != null) {
                return givenName + " " + familyName;
            }
            // Fallback genérico
            return oauth2User.getAttribute("email");
        }
        return name;
    }
    
    private String extractProviderId(OAuth2User oauth2User) {
        // Tentar diferentes atributos dependendo do provedor
        String id = oauth2User.getAttribute("sub"); // Google
        if (id == null) {
            id = oauth2User.getAttribute("id"); // Facebook
        }
        return id;
    }
    
    private AuthProvider determineProvider(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.contains("google")) {
            return AuthProvider.GOOGLE;
        } else if (requestUri.contains("facebook")) {
            return AuthProvider.FACEBOOK;
        }
        return AuthProvider.LOCAL; // fallback
    }
    
    private User findOrCreateUser(String email, String name, AuthProvider provider, String providerId) {
        Email userEmail = Email.of(email);
        
        // Verificar se usuário já existe
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            
            // Se é um usuário OAuth com o mesmo provider, retornar
            if (user.getProvider() == provider) {
                return user;
            }
            
            // Se é usuário local, não permitir OAuth com mesmo email
            if (user.isLocalUser()) {
                throw new RuntimeException("Email já cadastrado com login local");
            }
            
            // Se é OAuth mas provider diferente, não permitir
            throw new RuntimeException("Email já cadastrado com outro provedor social");
        }
        
        // Criar novo usuário OAuth
        User newUser = User.createOAuthUser(name, userEmail, provider, providerId, true);
        return userRepository.save(newUser);
    }
} 