package br.com.renatogsilva.my_car.api.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SECRET_KEY = "brcomrenatogsilvamycarapisecretkeysegurancabasicauthentication!@#$%&*%$#@!"; // Defina a chave secreta para decodificar o JWT
    private final String HEADER_STRING = "Authorization"; // Cabeçalho onde o token JWT é esperado

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Ignora requisições para /auth/**, pois elas não devem exigir um token JWT
        if (request.getRequestURI().startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response); // Passa para o próximo filtro sem fazer nada
            return;
        }

        String header = request.getHeader(HEADER_STRING);

        // Verifique se o cabeçalho de autorização está presente e se começa com "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ",""); // Extrai o token (sem o "Bearer ")

            try {
                // Valide e decodifique o token JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY) // Use a chave secreta para verificar a validade do token
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Se o token for válido, crie uma autenticação baseada nas informações do token
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(claims.getSubject(), null, null); // O sujeito é o nome do usuário

                // Adicione a autenticação ao contexto de segurança
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Se o token for inválido, rejeite a solicitação
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                response.getWriter().write("Token inválido ou expirado");
                return;
            }
        }

        // Continue a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
