package br.com.renatogsilva.my_car.service.person;

import br.com.renatogsilva.my_car.model.domain.Person;

public interface PersonService {

    Person create(Person person);
    Person update(Person person);
    Person findById(long id);
}
