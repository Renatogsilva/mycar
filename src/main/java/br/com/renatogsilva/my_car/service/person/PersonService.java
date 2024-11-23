package br.com.renatogsilva.my_car.service.person;

import br.com.renatogsilva.my_car.model.domain.Person;

public interface PersonService {

    public Person create(Person person);
    public Person update(Person person);
    public Person findById(long id);
}
