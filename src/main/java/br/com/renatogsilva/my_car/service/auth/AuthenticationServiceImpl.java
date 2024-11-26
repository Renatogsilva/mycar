package br.com.renatogsilva.my_car.service.auth;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Value("${jwt.expiration.time}")
    private Long EXPIRATION_TIME;

    @Override
    public LoginResponseDTO findUserByUsername(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());

        if (user == null || !passwordMatch(user.getPassword(), loginRequestDTO.getPassword())) {
            throw new UserAuthenticationException(EnumMessageUserExceptions.CREDENTIALS_INVALID.getMessage(),
                    EnumMessageUserExceptions.CREDENTIALS_INVALID.getCode());
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("Bearer "
                + jwtTokenProvider.generateToken(loginRequestDTO.getUsername(), user.getTypeUser().getDescription()));

        loginResponseDTO.setExpiresIn(new Date(System.currentTimeMillis() + EXPIRATION_TIME).getMinutes());

        return loginResponseDTO;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                    EnumMessageUserExceptions.USER_NOT_FOUND.getCode());
        }
        return this.userRepository.findUserByUsername((String) authentication.getPrincipal());
    }

    private boolean passwordMatch(String usernameLoginResponseDTO, String usernameLoginRequestDTO) {
        return bCryptPasswordEncoder.matches(usernameLoginRequestDTO, usernameLoginResponseDTO);
    }
}
