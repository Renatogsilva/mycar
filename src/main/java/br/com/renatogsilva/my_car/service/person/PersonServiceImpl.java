package br.com.renatogsilva.my_car.service.person;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    @Override
    public Person create(Person person) {
        for (Phone phone : person.getPhones()){
            phone.setPerson(person);
        }

        return this.personRepository.save(person);
    }

    @Override
    public Person update(Person person) {
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
