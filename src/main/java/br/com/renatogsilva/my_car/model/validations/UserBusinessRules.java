package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserDuplicationException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBusinessRules {

    private final UserRepository userRepository;

    private void validateUserDuplicateByUserIdAndLogin(Long userId, String login) {
        if (this.userRepository.findUserDuplicateByUserIdAndLogin(userId, login) != null) {
            throw new UserDuplicationException(EnumMessageUserExceptions.USER_DUPLICATE.getMessage(),
                    EnumMessageUserExceptions.USER_DUPLICATE.getCode());
        }
    }

    public void validateInclusioRules(UserRequestDTO userRequestDTO) {
        validateUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());
    }

    public void validateUpdateRules(UserRequestDTO userRequestDTO) {
        validateUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());
    }
}