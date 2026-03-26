package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import br.com.renatogsilva.my_car.service.person.PersonServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryPerson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName(value = "Testing class Person Service")
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    private Person personCreated;
    private Person personUpdated;
    private Person person;

    @BeforeEach
    public void setUp() {
        this.personCreated = FactoryPerson.createdValidPerson();
        this.personUpdated = FactoryPerson.updateValidPerson();
        this.person = FactoryPerson.createValidPerson();
    }

    @Test
    @DisplayName("Should return a person create with successful")
    public void createPerson_WithValidData_ShouldReturnPerson() {
        given(this.personRepository.save(any(Person.class))).willReturn(personCreated);

        Person result = this.personServiceImpl.create(this.person);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        then(this.personRepository).should().save(captor.capture());

        Person personSaved = captor.getValue();

        personSaved.getPhones().forEach(phone -> {
            Assertions.assertSame(personSaved, phone.getPerson());
            Assertions.assertNotNull(phone.getPerson());
        });

        then(this.personRepository).shouldHaveNoMoreInteractions();

        Assertions.assertSame(result, this.personCreated);
    }

    @Test
    @DisplayName("Should return a person update with successful")
    public void updatePerson_WithValidData_ShouldReturnPerson() {
        given(this.personRepository.save(any(Person.class))).willReturn(this.personUpdated);

        Person result = this.personServiceImpl.update(this.personUpdated);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        then(this.personRepository).should().save(captor.capture());

        Person personUpdate = captor.getValue();

        personUpdate.getPhones().forEach(phone -> {
            Assertions.assertEquals(personUpdate, phone.getPerson());
            Assertions.assertNotNull(phone.getPerson());
        });

        then(this.personRepository).shouldHaveNoMoreInteractions();

        Assertions.assertSame(result, this.personUpdated);
        Assertions.assertEquals("Update", result.getFirstName());
        Assertions.assertEquals("Sucessful", result.getLastName());
        Assertions.assertEquals("update.successful@gmail.com", result.getEmail());
    }

    @Test
    @DisplayName("Should return null when get person by id")
    public void findPersonById_WithValidData_ShouldReturnNull(){
        //GIVEN ARRANGE

        //WHE ACT
        Person result = this.personServiceImpl.findById(1L);

        //THEN ASSERT
        Assertions.assertNull(result);
    }
}
