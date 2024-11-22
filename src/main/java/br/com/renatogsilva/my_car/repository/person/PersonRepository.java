package br.com.renatogsilva.my_car.repository.person;

import br.com.renatogsilva.my_car.model.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
