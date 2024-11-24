package br.com.renatogsilva.my_car.api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private String SECRET_KEY = "my_car_secret_key_@#$%&*";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // O "subject" geralmente é o nome de usuário ou ID do usuário
                .setIssuedAt(new Date())  // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Expiração em 1 hora
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Algoritmo de assinatura e chave secreta
                .compact();
    }

    // Método para validar o token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token); // Se o token for válido, ele será analisado
            return true;
        } catch (Exception e) {
            return false; // Se ocorrer qualquer erro, o token é inválido
        }
    }

    // Método para extrair o nome de usuário do token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Retorna o nome de usuário que foi configurado no "setSubject"
    }
}