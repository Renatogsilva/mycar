package br.com.renatogsilva.my_car.service.auth;

import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;

public interface AuthenticationService {

    LoginResponseDTO findUserByUsername(LoginRequestDTO loginRequestDTO);
    User getAuthenticatedUser();
}
