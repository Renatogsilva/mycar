package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidations {

    public void checkRegistration(UserRequestDTO userRequestDTO) {
    }

    public void checkUpdate(UserRequestDTO userRequestDTO) {
    }
}