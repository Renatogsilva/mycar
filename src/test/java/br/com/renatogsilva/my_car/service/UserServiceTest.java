package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.converters.UserMapper;
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
import br.com.renatogsilva.my_car.utils.FactoryPerson;
import br.com.renatogsilva.my_car.utils.FactoryUser;
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

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private User user;
    private Person personEntity;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        this.userRequestDTO = FactoryUser.createUserRequestDTOObjectValid();
        this.userResponseDTO = FactoryUser.createUserResponseDTOObjectValid();
        this.user = FactoryUser.createUserEntityObjectValid();
        this.personEntity = FactoryPerson.createPersonEntityObjectValid();
        this.jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWR" +
                "taW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
    }

    @Test
    @DisplayName("Should return a user create with successful")
    public void shouldReturnAUserCreateWithSuccessful() {
        //GIVEN ARRANGE
        doNothing().when(this.userBusinessRules).validateInclusioRules(any(UserRequestDTO.class));
        doNothing().when(this.personBusinessRules).validateInclusioRules(any(PersonRequestDTO.class));

        given(this.userMapper.toUser(any(UserRequestDTO.class))).willReturn(this.user);
        given(this.personService.create(any(Person.class))).willReturn(this.personEntity);
        given(this.userRepository.save(any(User.class))).willReturn(this.user);
        given(this.userMapper.toUserResponseDTO(any(User.class))).willReturn(this.userResponseDTO);
        given(this.bCryptPasswordEncoder.encode(any(String.class))).willReturn(this.jwtToken);

        //WHEN ACT
        UserResponseDTO result = this.userService.create(this.userRequestDTO);

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
        then(this.bCryptPasswordEncoder).should().encode(this.userRequestDTO.getPersonRequestDTO().getCpf());

        Assertions.assertNotNull(personCaptor);
        Assertions.assertEquals(this.user.getPerson(), personCaptor);
        Assertions.assertSame(this.personEntity, userCaptor.getPerson());
        Assertions.assertEquals(EnumStatus.ACTIVE, userCaptor.getStatus());
        Assertions.assertEquals(EnumTypeUser.ADMIN, userCaptor.getTypeUser());
        Assertions.assertEquals(jwtToken, userCaptor.getPassword());
        Assertions.assertTrue(userCaptor.isPrimaryAccess());
        Assertions.assertNotNull(userCaptor.getCreationDate());

        Assertions.assertNotNull(result);
        Assertions.assertSame(this.userResponseDTO, result);
    }
}
