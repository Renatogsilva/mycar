package br.com.renatogsilva.my_car.service.login;

import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;

public interface LoginService {

    public LoginResponseDTO findUserByUsername(LoginRequestDTO loginRequestDTO);
}
