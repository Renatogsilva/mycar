package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.enums.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.exceptions.user.UserDuplicationException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.utils.user.FactoryUser;
import br.com.renatogsilva.my_car.utils.user.FactoryUserRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@DisplayName(value = "Testing class User Business Rules")
@ExtendWith(MockitoExtension.class)
public class UserBusinessRulesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserBusinessRules userBusinessRules;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Should throw user duplication exception when creating a user that already exist")
    public void shouldThrowUserDuplicationExceptionWhenCreatingUserThatAlreadyExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().build();
        User userEntityFromDB = FactoryUser.user().persisted().build();

        when(this.userRepository.findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(),
                userRequestDTO.getUsername())).thenReturn(userEntityFromDB);

        //WHEN ACT
        UserDuplicationException userDuplicationException = Assertions.assertThrows(UserDuplicationException.class, () -> {
            this.userBusinessRules.validateInclusioRules(userRequestDTO);
        });

        //THEN ASSERT
        verify(this.userRepository).findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());

        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userDuplicationException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_DUPLICATE.getMessage(), userDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_DUPLICATE.getCode(), userDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user duplication exception when updating a user that already exist")
    public void shouldThrowUserDuplicationExceptionWhenUpdatingUserThatAlreadyExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().build();
        User userEntityFromDB = FactoryUser.user().persisted().build();

        when(this.userRepository.findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(),
                userRequestDTO.getUsername())).thenReturn(userEntityFromDB);

        //WHEN ACT
        UserDuplicationException userDuplicationException = Assertions.assertThrows(UserDuplicationException.class, () -> {
            this.userBusinessRules.validateUpdateRules(userRequestDTO);
        });

        //THEN ASSERT
        verify(this.userRepository).findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());

        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userDuplicationException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_DUPLICATE.getMessage(), userDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_DUPLICATE.getCode(), userDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should return true when validate inclusion rules user is not exist")
    public void shouldReturnTrue_whenValidateInclusionRulesUserIsNotExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().build();

        when(this.userRepository.findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(),
                userRequestDTO.getUsername())).thenReturn(null);

        //WHEN ACT
        this.userBusinessRules.validateInclusioRules(userRequestDTO);

        //THEN ASSERT
        verify(this.userRepository).findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    @DisplayName("Should return true when validate update rules user is not exist")
    public void shouldReturnTrue_whenValidateUpdateRulesUserIsNotExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().build();

        when(this.userRepository.findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(),
                userRequestDTO.getUsername())).thenReturn(null);

        //WHEN ACT
        this.userBusinessRules.validateUpdateRules(userRequestDTO);

        //THEN ASSERT
        verify(this.userRepository).findUserDuplicateByUserIdAndLogin(userRequestDTO.getUserId(), userRequestDTO.getUsername());
        verifyNoMoreInteractions(this.userRepository);
    }
}
