package br.com.renatogsilva.my_car.service.login;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public LoginResponseDTO findUserByUsername(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());

        if (user == null || !passwordMatch(user.getPassword(), loginRequestDTO.getPassword())) {
            throw new UserAuthenticationException(EnumMessageUserExceptions.CREDENTIALS_INVALID.getMessage(),
                    EnumMessageUserExceptions.CREDENTIALS_INVALID.getCode());
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO("Bearer "
                + jwtTokenProvider.generateToken(loginRequestDTO.getUsername(), user.getTypeUser().getDescription()));

        return loginResponseDTO;
    }

    private boolean passwordMatch(String usernameLoginResponseDTO, String usernameLoginRequestDTO) {
        return bCryptPasswordEncoder.matches(usernameLoginRequestDTO, usernameLoginResponseDTO);
    }
}
