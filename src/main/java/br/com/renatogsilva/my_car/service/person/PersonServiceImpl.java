package br.com.renatogsilva.my_car.service.person;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService{

    private static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    @Override
    public Person create(Person person) {
        logger.info("Creating person: {}", person);

        for (Phone phone : person.getPhones()){
            phone.setPerson(person);
        }

        return this.personRepository.save(person);
    }

    @Override
    public Person update(Person person) {
        logger.info("Updating person: {}", person);

        for (Phone phone : person.getPhones()){
            phone.setPerson(person);
        }

        return this.personRepository.save(person);
    }

    @Override
    public Person findById(long id) {
        return null;
    }
}
