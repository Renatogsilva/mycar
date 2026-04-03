package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.converters.*;
import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.model.validations.PersonBusinessRules;
import br.com.renatogsilva.my_car.model.validations.UserBusinessRules;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.person.PersonService;
import br.com.renatogsilva.my_car.service.user.UserServiceImpl;
import br.com.renatogsilva.my_car.utils.user.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

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
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private String jwtToken;

    @BeforeEach
    public void setUp() {

        this.jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWR" +
                "taW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
    }

    @Test
    @DisplayName("Should return a user created with successful")
    public void shouldReturnAUserCreateWithSuccessful() {
        //GIVEN ARRANGE
        UserRequestDTO createRequest = FactoryUserRequestDTO.userRequest().build();
        UserResponseDTO createdResponse = FactoryUserResponseDTO.userResponse().persisted().build();
        User userToPersist = FactoryUser.user().build();
        User userPersisted = FactoryUser.user().persisted().build();
        Person personPersisted = FactoryPerson.person().persisted().build();

        doNothing().when(this.userBusinessRules).validateInclusioRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateInclusioRules(any(PersonRequestDTO.class));

        given(this.userMapper.toUser(any(UserRequestDTO.class))).willReturn(userToPersist);
        given(this.personService.create(any(Person.class))).willReturn(personPersisted);
        given(this.userRepository.save(any(User.class))).willReturn(userPersisted);
        given(this.userMapper.toUserResponseDTO(any(User.class))).willReturn(createdResponse);
        given(this.bCryptPasswordEncoder.encode(any(String.class))).willReturn(this.jwtToken);

        //WHEN ACT
        UserResponseDTO result = this.userService.create(createRequest);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        then(this.userRepository).should().save(userArgumentCaptor.capture());
        then(this.personService).should().create(personArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();
        Person personCaptor = personArgumentCaptor.getValue();

        then(this.userBusinessRules).should().validateInclusioRules(any(UserRequestDTO.class));
        then(this.personBusinessRules).should().validateInclusioRules(any(PersonRequestDTO.class));
        then(this.userMapper).should().toUser(any(UserRequestDTO.class));
        then(this.userMapper).should().toUserResponseDTO(any(User.class));
        then(this.bCryptPasswordEncoder).should().encode(createRequest.getPersonRequestDTO().getCpf());

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

        Assertions.assertNotNull(result);
        Assertions.assertSame(createdResponse, result);
        Assertions.assertSame(personPersisted, userCaptor.getPerson());
    }

    @Test
    @DisplayName("Should update user successfully")
    public void shouldUpdateUserSuccessfully() {
        //GIVEN ARRANGE
        Long userId = 1L;
        PersonRequestDTO pessoaRequestQueSeraAtualizado = FactoryPersonRequestDTO.personRequest().persisted()
                .withName("First Name Updated", "Last Name Updated").build();

        UserRequestDTO usuarioRequestQueSeraAtualizado = FactoryUserRequestDTO.userRequest().persisted()
                .withUsername("update.success")
                .withPerson(pessoaRequestQueSeraAtualizado).build();

        Person pessoaQueVeioDoBancoParaSerAtualizado = FactoryPerson.person().persisted().build();

        User usuarioQueVeioDoBancoParaSerAtualizado = FactoryUser
                .user().persisted()
                .withPerson(pessoaQueVeioDoBancoParaSerAtualizado).build();

        Person pessoaEntityAtualizado = FactoryPerson.person().persisted().withName("First Name Updated", "Last Name Updated").build();

        User usuarioEntityAtualizado = FactoryUser.user().persisted().withUsername("update.success")
                .withPerson(pessoaEntityAtualizado).build();

        UserResponseDTO usuarioResposta = FactoryUserResponseDTO.userResponse().persisted().withUsername("update.success")
                .withPerson(FactoryPersonResponseDTO.personResponse().persisted().withFullName("First Name Updated Last Name Updated").build()).build();

        doNothing().when(this.userBusinessRules).validateUpdateRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateUpdateRules(any(PersonRequestDTO.class));

        given(this.userRepository.findById(userId)).willReturn(Optional.of(usuarioQueVeioDoBancoParaSerAtualizado));
        given(this.userMapper.toUser(any(User.class), any(UserRequestDTO.class))).willReturn(usuarioEntityAtualizado);
        given(this.personService.update(any(Person.class))).willReturn(usuarioEntityAtualizado.getPerson());
        given(this.userRepository.save(any(User.class))).willReturn(usuarioEntityAtualizado);
        given(this.userMapper.toUserResponseDTO(any(User.class))).willReturn(usuarioResposta);

        //WHEN ACT
        UserResponseDTO result = this.userService.update(userId, usuarioRequestQueSeraAtualizado);

        //THEN ASSERT
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);

        then(this.userRepository).should().save(userArgumentCaptor.capture());
        then(this.personService).should().update(personArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();
        Person personCaptor = personArgumentCaptor.getValue();

        then(this.userBusinessRules).should().validateUpdateRules(usuarioRequestQueSeraAtualizado);
        then(this.personBusinessRules).should().validateUpdateRules(pessoaRequestQueSeraAtualizado);
        then(this.userRepository).should().findById(userId);
        then(this.userMapper).should().toUser(usuarioQueVeioDoBancoParaSerAtualizado, usuarioRequestQueSeraAtualizado);
        then(this.userMapper).should().toUserResponseDTO(usuarioEntityAtualizado);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(userCaptor);
        Assertions.assertNotNull(personCaptor);

        Assertions.assertSame(
                usuarioEntityAtualizado.getPerson(),
                userCaptor.getPerson()
        );

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
                () -> Assertions.assertEquals("First Name Updated", personCaptor.getFirstName()),
                () -> Assertions.assertEquals("Last Name Updated", personCaptor.getLastName())
        );
    }
}
