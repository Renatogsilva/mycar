package br.com.renatogsilva.my_car.utils.person;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.utils.phone.FactoryPhone;

import java.time.LocalDate;
import java.util.List;

public class FactoryPerson {
    public static PersonBuilder person() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {

        private final Person person;

        public PersonBuilder() {
            this.person = new Person();

            // DEFAULT (estado base válido)
            person.setPersonId(null);
            person.setFirstName("Emanuel");
            person.setLastName("Benjamin Benício Drumond");
            person.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
            person.setSex(EnumSex.MALE);
            person.setBirthDate(LocalDate.of(1994, 10, 12));
            person.setCpf("295.464.615-20");
            person.setPhones(FactoryPhone.listPersisted());
        }

        public PersonBuilder persisted() {
            person.setPersonId(1L);
            return this;
        }

        public PersonBuilder withId(Long id) {
            person.setPersonId(id);
            return this;
        }

        public PersonBuilder withName(String firstName, String lastName) {
            person.setFirstName(firstName);
            person.setLastName(lastName);
            return this;
        }

        public PersonBuilder withEmail(String email) {
            person.setEmail(email);
            return this;
        }

        public PersonBuilder withCpf(String cpf) {
            person.setCpf(cpf);
            return this;
        }

        public PersonBuilder withBirthDate(LocalDate birthDate) {
            person.setBirthDate(birthDate);
            return this;
        }

        public PersonBuilder withPhones(List<Phone> phones) {
            person.setPhones(phones);
            return this;
        }

        public Person build() {
            return person;
        }
    }
}
