package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.enums.EnumMessagePersonExceptions;
import br.com.renatogsilva.my_car.model.exceptions.person.PersonDuplicationException;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import br.com.renatogsilva.my_car.utils.person.FactoryPerson;
import br.com.renatogsilva.my_car.utils.person.FactoryPersonRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@DisplayName(value = "Testing class Person Business Rules")
@ExtendWith(MockitoExtension.class)
public class PersonBusinessRulesTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonBusinessRules personBusinessRules;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Should throw person duplication exception when creating a person that already exists")
    public void shouldThrowPersonDuplicationException_whenCreatingPersonThatAlreadyExists() {
        //GIVEN ARRANGE
        PersonRequestDTO personRequestDTO = FactoryPersonRequestDTO.personRequest().build();
        Person person = FactoryPerson.person().persisted().build();

        when(this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail())).thenReturn(person);

        //WHEN ACT
        PersonDuplicationException personDuplicationException = Assertions.assertThrows(PersonDuplicationException.class, () -> {
            this.personBusinessRules.validateInclusioRules(personRequestDTO);
        });

        //THEN ASSERT
        verify(this.personRepository).findPersonDuplicateByPersonIdAndCpfOrEmail(
                personRequestDTO.getPersonId(), personRequestDTO.getCpf(), personRequestDTO.getEmail()
        );

        verifyNoMoreInteractions(this.personRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(personDuplicationException),
                () -> Assertions.assertEquals(EnumMessagePersonExceptions.PERSON_DUPLICATE.getMessage(), personDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessagePersonExceptions.PERSON_DUPLICATE.getCode(), personDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw person duplication exception when updating a person that already exists")
    public void shouldThrowPersonDuplicationException_whenUpdatingPersonThatAlreadyExists() {
        //GIVEN ARRANGE
        PersonRequestDTO personRequestDTO = FactoryPersonRequestDTO.personRequest().persisted().build();
        Person person = FactoryPerson.person().persisted().build();

        when(this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail())).thenReturn(person);

        //WHEN ACT
        PersonDuplicationException personDuplicationException = Assertions.assertThrows(PersonDuplicationException.class, () -> {
            this.personBusinessRules.validateUpdateRules(personRequestDTO);
        });

        //THEN ASSERT
        verify(this.personRepository).findPersonDuplicateByPersonIdAndCpfOrEmail(
                personRequestDTO.getPersonId(), personRequestDTO.getCpf(), personRequestDTO.getEmail()
        );

        verifyNoMoreInteractions(this.personRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(personDuplicationException),
                () -> Assertions.assertEquals(EnumMessagePersonExceptions.PERSON_DUPLICATE.getMessage(), personDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessagePersonExceptions.PERSON_DUPLICATE.getCode(), personDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw person duplication exception when creating a person that already exists")
    public void shouldReturnTrue_whenInclusionRulesPersonIsNotExists() {
        //GIVEN ARRANGE
        PersonRequestDTO personRequestDTO = FactoryPersonRequestDTO.personRequest().build();

        when(this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail())).thenReturn(null);

        //WHEN ACT
        this.personBusinessRules.validateInclusioRules(personRequestDTO);

        //THEN ASSERT
        verify(this.personRepository).findPersonDuplicateByPersonIdAndCpfOrEmail(
                personRequestDTO.getPersonId(), personRequestDTO.getCpf(), personRequestDTO.getEmail()
        );
        verifyNoMoreInteractions(this.personRepository);
    }

    @Test
    @DisplayName("Should throw person duplication exception when updating a person that already exists")
    public void shouldReturnTrue_whenUpdatingRulesPersonIsNotExists() {
        //GIVEN ARRANGE
        PersonRequestDTO personRequestDTO = FactoryPersonRequestDTO.personRequest().build();

        when(this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail())).thenReturn(null);

        //WHEN ACT
        this.personBusinessRules.validateUpdateRules(personRequestDTO);

        //THEN ASSERT
        verify(this.personRepository).findPersonDuplicateByPersonIdAndCpfOrEmail(
                personRequestDTO.getPersonId(), personRequestDTO.getCpf(), personRequestDTO.getEmail()
        );
        verifyNoMoreInteractions(this.personRepository);
    }
}
