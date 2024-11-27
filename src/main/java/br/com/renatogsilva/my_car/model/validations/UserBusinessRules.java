package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserDuplicationException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBusinessRules {

    private static Logger logger = LoggerFactory.getLogger(UserBusinessRules.class);

    private final UserRepository userRepository;

    private void validateUserDuplicateByUserIdAndLogin(Long userId, String login) {
        logger.info("m: validateUserDuplicateByUserIdAndLogin - Validating user duplicate by userId {} " +
                "and login {}", userId, login);

        if (this.userRepository.findUserDuplicateByUserIdAndLogin(userId, login) != null) {
            logger.error("There is a user registered with this data");

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