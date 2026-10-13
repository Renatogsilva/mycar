package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import br.com.renatogsilva.my_car.model.enums.EnumTypeUser;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.model.validations.PersonBusinessRules;
import br.com.renatogsilva.my_car.model.validations.UserBusinessRules;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private AuthenticationService authenticationService;

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

        doNothing().when(this.userBusinessRules).validateInclusionRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateInclusionRules(any(PersonRequestDTO.class));

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

        verify(this.userBusinessRules).validateInclusionRules(any(UserRequestDTO.class));
        verify(this.personBusinessRules).validateInclusionRules(any(PersonRequestDTO.class));
        verify(this.bCryptPasswordEncoder).encode(userRequestDTO.getPersonRequestDTO().getCpf());

        assertNotNull(personCaptor);
        assertEquals(personPersisted.getFirstName(), personCaptor.getFirstName());
        assertEquals(personPersisted.getLastName(), personCaptor.getLastName());
        assertEquals(personPersisted.getEmail(), personCaptor.getEmail());
        assertEquals(personPersisted.getCpf(), personCaptor.getCpf());
        assertEquals(EnumStatus.ACTIVE, userCaptor.getStatus());
        assertEquals(EnumTypeUser.ADMIN, userCaptor.getTypeUser());
        assertEquals(jwtToken, userCaptor.getPassword());
        Assertions.assertTrue(userCaptor.isPrimaryAccess());
        assertNotNull(userCaptor.getCreationDate());
        Assertions.assertSame(personPersisted, userCaptor.getPerson());

        assertNotNull(userCaptor);
        assertNotNull(userResponseDTO);
        assertEquals(userResponseDTO.getUsername(), userCaptor.getUsername());
    }

    @Test
    @DisplayName("Should update user without revoking token when username remains unchanged")
    public void shouldUpdateUserWithoutRevokingTokenWhenUsernameUnchanged() {
        //GIVEN ARRANGE
        User authenticatedUser = FactoryUser.user().withUsername("username.login").persisted().build();

        Long userId = 1L;
        PersonRequestDTO personRequestDTOToUpdate = FactoryPersonRequestDTO.personRequest().persisted()
                .withName("First Name Updated", "Last Name Updated").build();

        UserRequestDTO userRequestDTOToUpdate = FactoryUserRequestDTO.userRequest().persisted()
                .withUsername("username.login")
                .withPerson(personRequestDTOToUpdate).build();

        Person personFromDB = FactoryPerson.person().persisted().build();

        User userFromDB = FactoryUser
                .user().persisted()
                .withPerson(personFromDB).build();

        Person updatedPersonEntity = FactoryPerson.person().persisted().withName("First Name Updated", "Last Name Updated").build();

        User updatedUserEntity = FactoryUser.user().persisted().withUsername("username.login")
                .withPerson(updatedPersonEntity).build();


        doNothing().when(this.userBusinessRules).validateUpdateRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateUpdateRules(any(PersonRequestDTO.class));

        when(authenticationService.getAuthenticatedUser()).thenReturn(authenticatedUser);
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

        verify(authenticationService).getAuthenticatedUser();
        verify(authenticationService, never()).logout();

        assertNotNull(result);
        assertNotNull(userCaptor);
        assertNotNull(userCaptor.getPerson());

        assertAll(
                () -> assertEquals("username.login", result.getUsername()),
                () -> assertEquals("First Name Updated Last Name Updated",
                        result.getPersonResponseDTO().getFullName())
        );

        assertAll(
                () -> assertEquals("username.login", userCaptor.getUsername()),
                () -> assertNotNull(userCaptor.getPerson()),
                () -> assertEquals("First Name Updated", userCaptor.getPerson().getFirstName()),
                () -> assertEquals("Last Name Updated", userCaptor.getPerson().getLastName())
        );

        assertAll(
                () -> assertEquals("First Name Updated", userCaptor.getPerson().getFirstName()),
                () -> assertEquals("Last Name Updated", userCaptor.getPerson().getLastName())
        );
    }

    @Test
    @DisplayName("Should update user without revoking token when authenticated user is different from the user being modified")
    public void shouldUpdateUserWithoutRevokingTokenWhenAuthenticatedUserIsDifferentFromTheUserBeingModified() {
        // Arrange
        Long userId = 1L;

        User authenticatedUser = FactoryUser.user()
                .persisted()
                .withUsername("authenticated.user")
                .build();

        Person personFromDB = FactoryPerson.person()
                .persisted()
                .build();

        User userFromDB = FactoryUser.user()
                .persisted()
                .withUsername("user.original")
                .withPerson(personFromDB)
                .build();

        PersonRequestDTO personRequestDTOToUpdate =
                FactoryPersonRequestDTO.personRequest()
                        .persisted()
                        .withName("First Name Updated", "Last Name Updated")
                        .build();

        UserRequestDTO userRequestDTOToUpdate =
                FactoryUserRequestDTO.userRequest()
                        .persisted()
                        .withUsername("user.updated")
                        .withPerson(personRequestDTOToUpdate)
                        .build();

        doNothing().when(userBusinessRules)
                .validateUpdateRules(any(UserRequestDTO.class));

        doNothing().when(personBusinessRules)
                .validateUpdateRules(any(PersonRequestDTO.class));

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userFromDB));

        when(personService.update(any(Person.class)))
                .thenReturn(personFromDB);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDTO result =
                userService.update(userId, userRequestDTOToUpdate);

        // Assert
        verify(authenticationService).getAuthenticatedUser();
        verify(authenticationService, never()).logout();

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertNotNull(result);

        assertAll(
                () -> assertEquals("user.updated", result.getUsername()),
                () -> assertEquals(
                        "user.updated",
                        userCaptor.getValue().getUsername()
                )
        );
    }

    @Test
    @DisplayName("Should not revoke token when authenticated user is different and username remains unchanged")
    public void shouldNotRevokeTokenWhenAuthenticatedUserIsDifferentAndUsernameUnchanged() {
        // Arrange
        Long userId = 1L;

        User authenticatedUser = FactoryUser.user()
                .persisted()
                .withUsername("authenticated.user")
                .build();

        Person personFromDB = FactoryPerson.person()
                .persisted()
                .build();

        User userFromDB = FactoryUser.user()
                .persisted()
                .withUsername("user.original")
                .withPerson(personFromDB)
                .build();

        PersonRequestDTO personRequestDTO =
                FactoryPersonRequestDTO.personRequest()
                        .persisted()
                        .build();

        UserRequestDTO userRequestDTO =
                FactoryUserRequestDTO.userRequest()
                        .persisted()
                        .withUsername("user.original")
                        .withPerson(personRequestDTO)
                        .build();

        doNothing().when(userBusinessRules)
                .validateUpdateRules(any(UserRequestDTO.class));

        doNothing().when(personBusinessRules)
                .validateUpdateRules(any(PersonRequestDTO.class));

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userFromDB));

        when(personService.update(any(Person.class)))
                .thenReturn(personFromDB);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDTO result = userService.update(userId, userRequestDTO);

        // Assert
        verify(authenticationService).getAuthenticatedUser();
        verify(authenticationService, never()).logout();

        assertNotNull(result);
        assertEquals("user.original", result.getUsername());
    }

    @Test
    @DisplayName("Should revoke token when authenticated user's username changes")
    public void shouldRevokeTokenWhenAuthenticatedUserUsernameChanges() {
        // Arrange
        Long userId = 1L;

        User authenticatedUser = FactoryUser.user()
                .persisted()
                .withUsername("user.original")
                .build();

        Person personFromDB = FactoryPerson.person()
                .persisted()
                .build();

        User userFromDB = FactoryUser.user()
                .persisted()
                .withUsername("user.original")
                .withPerson(personFromDB)
                .build();

        PersonRequestDTO personRequestDTOToUpdate = FactoryPersonRequestDTO.personRequest()
                .persisted()
                .withName("First Name Updated", "Last Name Updated")
                .build();

        UserRequestDTO userRequestDTOToUpdate = FactoryUserRequestDTO.userRequest()
                .persisted()
                .withUsername("user.updated")
                .withPerson(personRequestDTOToUpdate)
                .build();

        doNothing().when(userBusinessRules)
                .validateUpdateRules(any(UserRequestDTO.class));

        doNothing().when(personBusinessRules)
                .validateUpdateRules(any(PersonRequestDTO.class));

        when(authenticationService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userFromDB));

        when(personService.update(any(Person.class)))
                .thenReturn(personFromDB);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResponseDTO result = userService.update(userId, userRequestDTOToUpdate);

        // Assert
        verify(authenticationService).getAuthenticatedUser();
        verify(authenticationService).logout();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        assertNotNull(result);

        assertAll(
                () -> assertEquals("user.updated", result.getUsername()),
                () -> assertEquals("user.updated", userCaptor.getValue().getUsername())
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
            verify(this.authenticationService).logout();
            verifyNoMoreInteractions(this.userRepository);

            User userCaptor = userArgumentCaptor.getValue();

            assertAll(
                    "Assert UserCaptor",
                    () -> assertNotNull(userCaptor),
                    () -> assertEquals(jwtToken, userCaptor.getPassword())
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

        assertAll(
                "Assert Group",
                () -> assertNotNull(userResponseDTO),
                () -> assertEquals(userEntityFromDB.getUsername(), userResponseDTO.getUsername())
        );
    }

    @Test
    @DisplayName("Should find all users by filters successfully")
    void shouldFindAllUsersByFiltersSuccessfully() {
        // GIVEN
        List<User> usersFromDB = List.of(
                FactoryUser.user()
                        .persisted()
                        .build(),

                FactoryUser.user()
                        .persisted()
                        .withUserId(2L)
                        .withUsername("lista.gmail")
                        .build(),

                FactoryUser.user()
                        .persisted()
                        .withUserId(3L)
                        .withUsername("terceiro.gmail")
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = new PageImpl<>(
                usersFromDB,
                pageable,
                usersFromDB.size()
        );

        when(this.userRepository.findAllByFilters(
                EnumStatus.ACTIVE.name(),
                EnumTypeUser.ADMIN.name(),
                pageable
        )).thenReturn(usersPage);

        // WHEN
        Page<UserResponseListDTO> userResponseDTOPage =
                this.userService.findAllByFilters(
                        EnumStatus.ACTIVE,
                        EnumTypeUser.ADMIN,
                        pageable
                );

        // THEN
        verify(this.userRepository).findAllByFilters(
                EnumStatus.ACTIVE.name(),
                EnumTypeUser.ADMIN.name(),
                pageable
        );

        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",

                () -> assertEquals(
                        3,
                        userResponseDTOPage.getTotalElements()
                ),

                () -> assertEquals(
                        3,
                        userResponseDTOPage.getContent().size()
                ),

                () -> assertNotNull(
                        userResponseDTOPage.getContent().get(0)
                ),

                () -> assertNotNull(
                        userResponseDTOPage.getContent().get(1)
                ),

                () -> assertNotNull(
                        userResponseDTOPage.getContent().get(2)
                ),

                () -> assertEquals(
                        usersFromDB.get(0).getPerson().getFirstName() + " " +
                                usersFromDB.get(0).getPerson().getLastName(),
                        userResponseDTOPage.getContent().get(0).getFullName()
                ),

                () -> assertEquals(
                        EnumStatus.ACTIVE,
                        userResponseDTOPage.getContent().get(0).getStatus()
                ),

                () -> assertEquals(
                        EnumStatus.ACTIVE.getDescription(),
                        userResponseDTOPage.getContent().get(0).getStatus().getDescription()
                )
        );
    }

    @Test
    @DisplayName("Should convert status and typeUser to String successfully")
    void shouldConvertStatusAndTypeUserToStringSuccessfully() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(this.userRepository.findAllByFilters(
                "ACTIVE",
                "ADMIN",
                pageable
        )).thenReturn(usersPage);

        // WHEN
        this.userService.findAllByFilters(
                EnumStatus.ACTIVE,
                EnumTypeUser.ADMIN,
                pageable
        );

        // THEN
        verify(this.userRepository).findAllByFilters(
                "ACTIVE",
                "ADMIN",
                pageable
        );
    }

    @Test
    @DisplayName("Should convert status and keep typeUser as null")
    void shouldConvertStatusAndKeepTypeUserAsNull() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(this.userRepository.findAllByFilters(
                "ACTIVE",
                null,
                pageable
        )).thenReturn(usersPage);

        // WHEN
        this.userService.findAllByFilters(
                EnumStatus.ACTIVE,
                null,
                pageable
        );

        // THEN
        verify(this.userRepository).findAllByFilters(
                "ACTIVE",
                null,
                pageable
        );
    }

    @Test
    @DisplayName("Should keep status as null and convert typeUser to String")
    void shouldKeepStatusAsNullAndConvertTypeUserToString() {

        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(this.userRepository.findAllByFilters(
                null,
                "ADMIN",
                pageable
        )).thenReturn(usersPage);

        // WHEN
        this.userService.findAllByFilters(
                null,
                EnumTypeUser.ADMIN,
                pageable
        );

        // THEN
        verify(this.userRepository).findAllByFilters(
                null,
                "ADMIN",
                pageable
        );
    }

    @Test
    @DisplayName("Should keep status and typeUser as null")
    void shouldKeepStatusAndTypeUserAsNull() {

        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> usersPage = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(this.userRepository.findAllByFilters(
                null,
                null,
                pageable
        )).thenReturn(usersPage);

        // WHEN
        this.userService.findAllByFilters(
                null,
                null,
                pageable
        );

        // THEN
        verify(this.userRepository).findAllByFilters(
                null,
                null,
                pageable
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

        assertAll(
                "Assert Group",
                () -> assertNotNull(userCaptor),
                () -> assertEquals(userEntityFromDB.getUsername(), userCaptor.getUsername()),
                () -> assertNotNull(userCaptor.getPassword()),
                () -> assertEquals(EnumStatus.ACTIVE, userCaptor.getStatus())
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

        verify(this.authenticationService).logout();
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",
                () -> assertNotNull(userCaptor),
                () -> assertEquals(userEntityFromDB.getUsername(), userCaptor.getUsername()),
                () -> assertNotNull(userCaptor.getPassword()),
                () -> assertEquals(EnumStatus.INACTIVE, userCaptor.getStatus())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when user does not exist")
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        //GIVEN ARRANGE
        UserRequestDTO userRequestDTO = FactoryUserRequestDTO.userRequest().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());
        //WHEN ACT

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.update(this.userId, userRequestDTO)
        );

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",
                () -> assertNotNull(userNotFoundException),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when user profile does not exist ")
    public void shouldThrowUserNotFoundExceptionWhenUserProfileDoesNotExist() {
        //GIVEN ARRANGE
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.update(this.userId, userProfileRequestDTO)
        );

        //THEN ASSERT
        assertAll(
                "Assert Group",
                () -> assertNotNull(userNotFoundException),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
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
            UserAuthenticationException userAuthenticationException = Assertions.assertThrows(UserAuthenticationException.class, () ->
                    this.userService.update(this.userId, userProfileRequestDTO)
            );

            //THEN ASSERT
            verify(this.userRepository).findById(this.userId);
            verifyNoMoreInteractions(this.userRepository);

            assertAll(
                    "Assert Group",
                    () -> assertNotNull(userAuthenticationException),
                    () -> assertEquals(EnumMessageUserExceptions.CURRENT_PASSWORD_INVALID.getMessage(), userAuthenticationException.getMessage()),
                    () -> assertEquals(EnumMessageUserExceptions.CURRENT_PASSWORD_INVALID.getCode(), userAuthenticationException.getCode())
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
            UserAuthenticationException userAuthenticationException = Assertions.assertThrows(UserAuthenticationException.class, () ->
                    this.userService.update(this.userId, userProfileRequestDTO)
            );

            //THEN ASSERT
            verify(this.userRepository).findById(this.userId);
            verifyNoMoreInteractions(this.userRepository);

            assertAll(
                    "Assert Group",
                    () -> assertNotNull(userAuthenticationException),
                    () -> assertEquals(EnumMessageUserExceptions.USERNAME_INVALID.getMessage(), userAuthenticationException.getMessage()),
                    () -> assertEquals(EnumMessageUserExceptions.USERNAME_INVALID.getCode(), userAuthenticationException.getCode())
            );
        }
    }

    @Test
    @DisplayName("Should throw user not found exception when does not exist")
    public void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        //GIVEN ARRANGE
        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.findById(this.userId)
        );

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",
                () -> assertNotNull(userNotFoundException),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw user not found exception when enable user")
    public void shouldThrowUserNotFoundExceptionWhenEnableUser() {
        //GIVEN ARRANGE
        when(this.userRepository.findById(this.userId)).thenReturn(Optional.empty());

        //WHEN ACT
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.enable(this.userId)
        );

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",
                () -> assertNotNull(userNotFoundException),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
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
        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () ->
                this.userService.disable(this.userId)
        );

        //THEN ASSERT
        verify(this.userRepository).findById(this.userId);
        verifyNoMoreInteractions(this.userRepository);

        assertAll(
                "Assert Group",
                () -> assertNotNull(userNotFoundException),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), userNotFoundException.getMessage()),
                () -> assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), userNotFoundException.getCode())
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
