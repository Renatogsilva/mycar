package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.converters.*;
import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.model.validations.PersonBusinessRules;
import br.com.renatogsilva.my_car.model.validations.UserBusinessRules;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.person.PersonService;
import br.com.renatogsilva.my_car.service.user.UserServiceImpl;
import br.com.renatogsilva.my_car.utils.GeneralFunctions;
import br.com.renatogsilva.my_car.utils.person.FactoryPerson;
import br.com.renatogsilva.my_car.utils.person.FactoryPersonRequestDTO;
import br.com.renatogsilva.my_car.utils.user.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName(value = "Testing class User Service")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBusinessRules userBusinessRules;

    @Mock
    private PersonBusinessRules personBusinessRules;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private String jwtToken;

    private Long userId;

    @BeforeEach
    public void setUp() {
        this.userId = 1L;

        this.jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWR" +
                "taW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
    }

    @Test
    @DisplayName("Should return user created with successful")
    public void shouldCreateUserSuccessfully() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().build();
        User userPersisted = FactoryUser.user().persisted().build();
        Person personPersisted = FactoryPerson.person().persisted().build();

        doNothing().when(this.userBusinessRules).validateInclusioRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateInclusioRules(any(PersonRequestDTO.class));

        when(this.personService.create(any(Person.class))).thenReturn(personPersisted);
        when(this.userRepository.save(any(User.class))).thenReturn(userPersisted);
        when(this.bCryptPasswordEncoder.encode(any(String.class))).thenReturn(this.jwtToken);

        //WHEN ACT
        UserResponseDTO userResponseDTO = this.userService.create(userRequestDTO);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        verify(this.userRepository).save(userArgumentCaptor.capture());
        verify(this.personService).create(personArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();
        Person personCaptor = personArgumentCaptor.getValue();

        verify(this.userBusinessRules).validateInclusioRules(any(UserRequestDTO.class));
        verify(this.personBusinessRules).validateInclusioRules(any(PersonRequestDTO.class));
        verify(this.bCryptPasswordEncoder).encode(userRequestDTO.getPersonRequestDTO().getCpf());

        Assertions.assertNotNull(personCaptor);
        Assertions.assertEquals(personPersisted.getFirstName(), personCaptor.getFirstName());
        Assertions.assertEquals(personPersisted.getLastName(), personCaptor.getLastName());
        Assertions.assertEquals(personPersisted.getEmail(), personCaptor.getEmail());
        Assertions.assertEquals(personPersisted.getCpf(), personCaptor.getCpf());
        Assertions.assertEquals(EnumStatus.ACTIVE, userCaptor.getStatus());
        Assertions.assertEquals(EnumTypeUser.ADMIN, userCaptor.getTypeUser());
        Assertions.assertEquals(jwtToken, userCaptor.getPassword());
        Assertions.assertTrue(userCaptor.isPrimaryAccess());
        Assertions.assertNotNull(userCaptor.getCreationDate());
        Assertions.assertSame(personPersisted, userCaptor.getPerson());

        Assertions.assertNotNull(userCaptor);
        Assertions.assertNotNull(userResponseDTO);
        Assertions.assertEquals(userResponseDTO.getUsername(), userCaptor.getUsername());
    }

    @Test
    @DisplayName("Should update user successfully")
    public void shouldUpdateUserSuccessfully() {
        //GIVEN ARRANGE
        Long userId = 1L;
        PersonRequestDTO personRequestDTOToUpdate = FactoryPersonRequestDTO.personRequest().persisted()
                .withName("First Name Updated", "Last Name Updated").build();

        UserRequestDTO userRequestDTOToUpdate = FactoryUserRequestDTO.userRequest().persisted()
                .withUsername("update.success")
                .withPerson(personRequestDTOToUpdate).build();

        Person personFromDB = FactoryPerson.person().persisted().build();

        User userFromDB = FactoryUser
                .user().persisted()
                .withPerson(personFromDB).build();

        Person updatedPersonEntity = FactoryPerson.person().persisted().withName("First Name Updated", "Last Name Updated").build();

        User updatedUserEntity = FactoryUser.user().persisted().withUsername("update.success")
                .withPerson(updatedPersonEntity).build();


        doNothing().when(this.userBusinessRules).validateUpdateRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateUpdateRules(any(PersonRequestDTO.class));

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(userFromDB));
        when(this.personService.update(any(Person.class))).thenReturn(updatedUserEntity.getPerson());
        when(this.userRepository.save(any(User.class))).thenReturn(updatedUserEntity);

        //WHEN ACT
        UserResponseDTO result = this.userService.update(userId, userRequestDTOToUpdate);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(this.userRepository).save(userArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();

        verify(this.userBusinessRules).validateUpdateRules(userRequestDTOToUpdate);
        verify(this.personBusinessRules).validateUpdateRules(personRequestDTOToUpdate);
        verify(this.userRepository).findById(userId);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(userCaptor);
        Assertions.assertNotNull(userCaptor.getPerson());

        Assertions.assertAll(
                () -> Assertions.assertEquals("update.success", result.getUsername()),
                () -> Assertions.assertEquals("First Name Updated Last Name Updated",
                        result.getPersonResponseDTO().getFullName())
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("update.success", userCaptor.getUsername()),
                () -> Assertions.assertNotNull(userCaptor.getPerson()),
                () -> Assertions.assertEquals("First Name Updated", userCaptor.getPerson().getFirstName()),
                () -> Assertions.assertEquals("Last Name Updated", userCaptor.getPerson().getLastName())
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals("First Name Updated", userCaptor.getPerson().getFirstName()),
                () -> Assertions.assertEquals("Last Name Updated", userCaptor.getPerson().getLastName())
        );
    }

    @Test
    @DisplayName("Should update user profile successfully")
    public void shouldUpdateUserProfileSuccessfully() {
        //GIVEN ARRANGE
        User userProfileFromDB = FactoryUser.user().persisted().build();
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();

        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(userProfileFromDB));
        when(this.bCryptPasswordEncoder.encode(userProfileRequestDTO.getNewPassword())).thenReturn(this.jwtToken);
        when(this.userRepository.save(any(User.class))).thenReturn(userProfileFromDB);

        try (MockedStatic<GeneralFunctions> mocked = Mockito.mockStatic(GeneralFunctions.class)) {

            mocked.when(() -> GeneralFunctions.passwordMatch(
                    bCryptPasswordEncoder,
                    userProfileFromDB.getPassword(),
                    userProfileRequestDTO.getCurrentPassword()
            )).thenReturn(true);

            //WHEN ACT
            this.userService.update(1L, userProfileRequestDTO);

            //THEN ASSERT
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(this.userRepository).save(userArgumentCaptor.capture());
            verifyNoMoreInteractions(this.userRepository);

            User userCaptor = userArgumentCaptor.getValue();

            Assertions.assertAll(
                    "Assert UserCaptor",
                    () -> Assertions.assertNotNull(userCaptor),
                    () -> Assertions.assertEquals(jwtToken, userCaptor.getPassword())
            );
        }
    }

    @Test
    @DisplayName("Should find user by id successfully")
    public void shouldFindUserByIdSuccessfully() {
        //GIVEN ARRANGE
        User userEntityFromDB = FactoryUser.user().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFromDB));
        //WHEN ACT
        UserResponseDTO userResponseDTO = this.userService.findById(this.userId);

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userResponseDTO),
                () -> Assertions.assertEquals(userEntityFromDB.getUsername(), userResponseDTO.getUsername())
        );
    }

    @Test
    @DisplayName("Should find all users successfully")
    public void shouldFindAllUsersSuccessfully() {
        //GIVEN ASSERT
        List<User> usersFromDB = new ArrayList<>(
                List.of(FactoryUser.user().persisted().build(),
                        FactoryUser.user().persisted().withUserId(2L).withUsername("lista.gmail").build(),
                        FactoryUser.user().persisted().withUserId(3L).withUsername("terceiro.gmail").build()));

        when(this.userRepository.findAll()).thenReturn(usersFromDB);
        //WHEN ACT
        List<UserResponseListDTO> userResponseDTOList = this.userService.findAll();

        //THEN ASSERT
        verify(this.userRepository).findAll();
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertEquals(3, userResponseDTOList.size()),
                () -> Assertions.assertNotNull(userResponseDTOList.get(0)),
                () -> Assertions.assertNotNull(userResponseDTOList.get(1)),
                () -> Assertions.assertNotNull(userResponseDTOList.get(2)),
                () -> Assertions.assertEquals(usersFromDB.get(0).getPerson().getFirstName() + " " +
                        usersFromDB.get(0).getPerson().getLastName(), userResponseDTOList.get(0).getFullName())
        );
    }

    @Test
    @DisplayName("Should enable user by id successfully")
    public void shouldEnableUserByIdSuccessfully() {
        //GIVEN ARRANGE
        User userEntityFromDB = FactoryUser.user().persisted().withStatus(EnumStatus.INACTIVE).build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFromDB));
        when(this.userRepository.save(any(User.class))).thenReturn(userEntityFromDB);

        //WHEN ACT

        this.userService.enable(this.userId);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository).save(userArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();

        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userCaptor),
                () -> Assertions.assertEquals(userEntityFromDB.getUsername(), userCaptor.getUsername()),
                () -> Assertions.assertNotNull(userCaptor.getPassword()),
                () -> Assertions.assertEquals(EnumStatus.ACTIVE, userCaptor.getStatus())
        );
    }

    @Test
    @DisplayName("Should disable user by id successfully")
    public void shouldDisableUserByIdSuccessfully() {
        //GIVEN ARRANGE
        User userEntityFromDB = FactoryUser.user().persisted().withStatus(EnumStatus.ACTIVE).build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFromDB));
        when(this.userRepository.save(userEntityFromDB)).thenReturn(userEntityFromDB);

        //WHEN ACT
        this.userService.disable(this.userId);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository).save(userArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();

        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userCaptor),
                () -> Assertions.assertEquals(userEntityFromDB.getUsername(), userCaptor.getUsername()),
                () -> Assertions.assertNotNull(userCaptor.getPassword()),
                () -> Assertions.assertEquals(EnumStatus.INACTIVE, userCaptor.getStatus())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when user does not exist")
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());
        //WHEN ACT

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.update(this.userId, userRequestDTO);
        });

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userNotFoundException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when user profile does not exist ")
    public void shouldThrowUserNotFoundExceptionWhenUserProfileDoesNotExist() {
        //GIVEN ARRANGE
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.update(this.userId, userProfileRequestDTO);
        });

        //THEN ASSERT
        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userNotFoundException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user authentication exception when password is invalid")
    public void shouldThrowUserAuthenticationExceptionWhenPasswordIsInvalid() {
        //GIVEN ARRANGE
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();
        User userEntityFomDB = FactoryUser.user().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFomDB));

        try (MockedStatic<GeneralFunctions> mocked = Mockito.mockStatic(GeneralFunctions.class)) {

            mocked.when(() -> GeneralFunctions.passwordMatch(
                    bCryptPasswordEncoder,
                    userEntityFomDB.getPassword(),
                    userProfileRequestDTO.getCurrentPassword()
            )).thenReturn(false);

            //WHEN ACT
            UserAuthenticationException userAuthenticationException = Assertions.assertThrows(UserAuthenticationException.class, () -> {
                this.userService.update(this.userId, userProfileRequestDTO);
            });

            //THEN ASSERT
            verify(this.userRepository).findById(this.userId);
            verifyNoMoreInteractions(this.userRepository);

            Assertions.assertAll(
                    "Assert Group",
                    () -> Assertions.assertNotNull(userAuthenticationException),
                    () -> Assertions.assertEquals(EnumMessageUserExceptions.CURRENT_PASSWORD_INVALID.getMessage(), userAuthenticationException.getMessage()),
                    () -> Assertions.assertEquals(EnumMessageUserExceptions.CURRENT_PASSWORD_INVALID.getCode(), userAuthenticationException.getCode())
            );
        }
    }

    @Test
    @DisplayName("Should throw user authentication exception when username is invalid")
    public void shouldThrowUserAuthenticationExceptionWhenUsernameIsInvalid() {
        //GIVEN ARRANGE
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();
        User userEntityFomDB = FactoryUser.user().persisted().withUsername("errado.error@gmail.com").build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFomDB));
        try (MockedStatic<GeneralFunctions> mocked = Mockito.mockStatic(GeneralFunctions.class)) {
            mocked.when(() -> GeneralFunctions.passwordMatch(
                    bCryptPasswordEncoder,
                    userEntityFomDB.getPassword(),
                    userProfileRequestDTO.getCurrentPassword()
            )).thenReturn(true);

            //WHEN ACT
            UserAuthenticationException userAuthenticationException = Assertions.assertThrows(UserAuthenticationException.class, () -> {
                this.userService.update(this.userId, userProfileRequestDTO);
            });

            //THEN ASSERT
            verify(this.userRepository).findById(this.userId);
            verifyNoMoreInteractions(this.userRepository);

            Assertions.assertAll(
                    "Assert Group",
                    () -> Assertions.assertNotNull(userAuthenticationException),
                    () -> Assertions.assertEquals(EnumMessageUserExceptions.USERNAME_INVALID.getMessage(), userAuthenticationException.getMessage()),
                    () -> Assertions.assertEquals(EnumMessageUserExceptions.USERNAME_INVALID.getCode(), userAuthenticationException.getCode())
            );
        }
    }

    @Test
    @DisplayName("Should throw user not found exception when does not exist")
    public void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        //GIVEN ARRANGE
        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findById(this.userId);
        });

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userNotFoundException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when enable user")
    public void shouldThrowUserNotFoundExceptionWhenEnableUser() {
        //GIVEN ARRANGE
        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.enable(this.userId);
        });

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userNotFoundException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should not enable user when is already active")
    public void shouldNotEnableUserWhenUserIsAlreadyActive() {
        //GIVEN ARRANGE
        User userEntityFomDB = FactoryUser.user().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFomDB));

        //WHEN ACT
        this.userService.enable(this.userId);

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    @DisplayName("Should throw user not found exception when disable user")
    public void shouldThrowUserNotFoundExceptionWhenDisableUser() {
        //GIVEN ARRANGE
        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.disable(this.userId);
        });

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(userNotFoundException),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should not disable user when already inactive")
    public void shouldNotDisableUserWhenIsAlreadyDisable() {
        //GIVEN ARRANGE
        User userEntityFomDB = FactoryUser.user().persisted().withStatus(EnumStatus.INACTIVE).build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(userEntityFomDB));

        //WHEN ACT
        this.userService.disable(this.userId);

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);
    }
}
