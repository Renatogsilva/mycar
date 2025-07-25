package br.com.renatogsilva.my_car.service.auth;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.api.config.auth.TokenRevocationConfig;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.utils.GeneralFunctions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRevocationConfig tokenRevocationConfig;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Value("${jwt.expiration.time}")
    public Long EXPIRATION_TIME;

    @Override
    public LoginResponseDTO findUserByUsername(LoginRequestDTO loginRequestDTO) {
        logger.info("m: findUserByUsername - get user by username {}", loginRequestDTO.getUsername());

        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());

        if (user == null || !(GeneralFunctions.passwordMatch(bCryptPasswordEncoder,
                user.getPassword(), loginRequestDTO.getPassword()))) {
            logger.error("m: findUserByUsername - user with {} not found", loginRequestDTO.getUsername());

            throw new UserAuthenticationException(EnumMessageUserExceptions.CREDENTIALS_INVALID.getMessage(),
                    EnumMessageUserExceptions.CREDENTIALS_INVALID.getCode());
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtTokenProvider.generateToken(loginRequestDTO.getUsername(), user.getTypeUser().getDescription()));

        loginResponseDTO.setExpiresIn(new Date(System.currentTimeMillis() + EXPIRATION_TIME).getTime());

        return loginResponseDTO;
    }

    @Override
    public User getAuthenticatedUser() {
        logger.info("m: getAuthenticatedUser - capturing user session");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("m: getAuthenticatedUser - authentication is null");

            throw new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                    EnumMessageUserExceptions.USER_NOT_FOUND.getCode());
        }
        return this.userRepository.findUserByUsername((String) authentication.getPrincipal());
    }

    @Override
    public void logout() {
        logger.info("m: logout - capturing token session");

        String token = SecurityContextHolder.getContext().getAuthentication()
                .getCredentials().toString().replace("Bearer ", "");

        if (jwtTokenProvider.validateToken(token)) {
            logger.info("m: logout - token in the revocation process");
            tokenRevocationConfig.revokeToken(token);
        }

        logger.info("m: logout - token revoked successfully");
    }
}