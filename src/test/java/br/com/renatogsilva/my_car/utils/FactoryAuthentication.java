package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;

public class FactoryAuthentication {

    public static LoginRequestDTO createLoginRequestDTOObjectValid() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("teste.teste");
        loginRequestDTO.setPassword("355.137.120-24");

        return loginRequestDTO;
    }

    public static LoginResponseDTO createLoginResponseDTOObjectValid() {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setExpiresIn(System.currentTimeMillis() / 1000);
        loginResponseDTO.setToken("qwerasf.dgsdagasfafasfdaswereg.@##$%asdfasfafafasdfafd.agasdfwwwafdafaerqw");

        return loginResponseDTO;
    }

    public static LoginRequestDTO createLoginRequestDTOObjectInvalid() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(null);
        loginRequestDTO.setPassword("");

        return loginRequestDTO;
    }

    public static LoginRequestDTO createLoginRequestDTOObjectPasswordInvalid() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("teste.teste");
        loginRequestDTO.setPassword("asdfafççlkjdasd");

        return loginRequestDTO;
    }

    public static LoginRequestDTO createLoginRequestDTOObjectUsernameInvalid() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("teste.invalid");
        loginRequestDTO.setPassword("355.137.120-24");

        return loginRequestDTO;
    }
}
