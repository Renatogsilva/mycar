package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import br.com.renatogsilva.my_car.service.person.PersonServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName(value = "Testing class Person Service")
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personServiceImpl;
}
