package com.backendsoft.foodmenu.auth.config;

import com.backendsoft.foodmenu.auth.lib.SecurityConstants;
import com.backendsoft.foodmenu.auth.lib.RsaKeyUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("${jwt.private-key}")
    private String privateKeyString;
    @Value("${jwt.public-key}")
    private String publicKeyString;
    @Value("${app.cors.allowed-origin}")
    private String allowedOrigin;
    @Autowired
    private ResourceLoader resourceLoader;

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .csrf(csrf -> csrf.disable())
           .cors(Customizer.withDefaults())
           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .authorizeHttpRequests(auth -> auth
                   .requestMatchers(
                           SecurityConstants.AUTH_TOKEN_ENDPOINT,
                           SecurityConstants.SWAGGER_UI_ENDPOINT+"/**",
                           SecurityConstants.SWAGGER_V3_DOCS_ENDPOINT+"/**"
                   ).permitAll()

                   .requestMatchers(HttpMethod.GET,
                           "/api/customers/**",
                           "/api/meals/**",
                           "/api/menus/**",
                           "/api/orders/*/*"
                   ).permitAll()

                   .requestMatchers(HttpMethod.PUT,
                           "/api/orders/**"
                   ).permitAll()

                   .anyRequest().authenticated()
           )
           .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
           .headers(headers -> headers.frameOptions(frame -> frame.disable()));

       return http.build();
   }

    public String loadKey(String keyValue) throws Exception {
        if (keyValue.startsWith("file:") || keyValue.startsWith("classpath:")) {
            Resource resource = resourceLoader.getResource(keyValue);
            return new String(resource.getInputStream().readAllBytes());
        }
        return keyValue;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
   JwtEncoder jwtEncoder() throws Exception {
        PrivateKey privateKey = RsaKeyUtil.getPrivateKey(this.loadKey(privateKeyString));
        PublicKey publicKey = RsaKeyUtil.getPublicKey(this.loadKey(publicKeyString));

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) publicKey)
                .privateKey((RSAPrivateKey) privateKey)
                .keyID("rsa-key")
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
   }

   @Bean
   public JwtDecoder jwtDecoder() throws Exception {
       PublicKey publicKey = RsaKeyUtil.getPublicKey(this.loadKey(publicKeyString));

       return NimbusJwtDecoder
               .withPublicKey((RSAPublicKey) publicKey)
               .build();
   }

   @Bean
   CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();

       configuration.setAllowedOrigins(List.of(allowedOrigin));
       configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
       configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
       configuration.setExposedHeaders(List.of("Authorization"));

       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", configuration);
       return source;
   }

}
