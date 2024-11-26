package br.com.renatogsilva.my_car.api.config.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private final String credentials;

    public JwtAuthenticationToken(String principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true); // Marca a autenticação como bem-sucedida
    }

    // Construtor para quando o token ainda não for autenticado
    public JwtAuthenticationToken(String principal, String credentials) {
        super(null); // Sem authorities definidas, já que o token não foi validado ainda
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false); // Marca como não autenticado até ser validado
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
