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
import br.com.renatogsilva.my_car.utils.person.FactoryPerson;
import br.com.renatogsilva.my_car.utils.person.FactoryPersonRequestDTO;
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
}
