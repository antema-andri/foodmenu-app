package com.backendsoft.foodmenu.auth.lib;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public JwtServiceImpl(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        long jwtExpiration = 3600;
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        long refreshExpiration = 3600*10;
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String extractUsername(String token) {
        try {
            Jwt jwt = decoder.decode(token);
            return jwt.getSubject(); // Le claim "sub" contient le username
        } catch (JwtException e) {
            // Gestion d'erreur : token invalide, expiré, signature incorrecte...
            // Vous pouvez logger l'erreur et retourner null ou lancer une exception personnalisée
            return null;
        }
    }

    /**
     * Vérifie si un token JWT est valide pour un utilisateur donné.
     * @param token le token JWT à valider
     * @param userDetails les détails de l'utilisateur attendu
     * @return true si le token est valide et correspond à l'utilisateur, false sinon
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Jwt jwt = decoder.decode(token);
            String username = jwt.getSubject();
            // Vérification que le sujet n'est pas null et correspond au username attendu
            return username != null && username.equals(userDetails.getUsername());
        } catch (JwtException e) {
            // La validation échoue (expiration, signature, format...)
            return false;
        }
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet
                .builder()
                .issuer("http://localhost:8080")
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiration))
                .claims(claims -> claims.putAll(extraClaims))
                .build();
        JwtEncoderParameters encoderParameters = this.getEncoderParameters(claimsSet);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public JwtEncoderParameters getEncoderParameters(JwtClaimsSet claims) {
        JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(
//                JwsHeader.with(MacAlgorithm.HS512).build(),
                JwsHeader.with(SignatureAlgorithm.RS256).build(),
                claims
        );
        return encoderParameters;
    }
}
