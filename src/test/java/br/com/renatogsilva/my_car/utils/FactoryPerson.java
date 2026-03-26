package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactoryPerson {

    public static Person createdValidPerson() {
        Person person = new Person();
        person.setPersonId(1L);
        person.setFirstName("Julio");
        person.setLastName("Cesar");
        person.setEmail("julio_cesar_teste_mockt@gmail.com");
        person.setSex(EnumSex.MALE);
        person.setBirthDate(LocalDate.of(1997, 10, 18));
        person.setCpf("317.752.450-55");
        person.setPhones(getCreatedValidPhones());

        return person;
    }

    public static Person createValidPerson() {
        return new Person(null, "Júlio", "Cesar", "julio_cesar_teste_mockt@gmail.com",
                "317.752.450-55", EnumSex.MALE, LocalDate.of(1997, 10, 18), getCreateValidPhones());
    }

    public static Person updateValidPerson() {
        Person person = new Person();
        person.setPersonId(1L);
        person.setFirstName("Update");
        person.setLastName("Sucessful");
        person.setEmail("update.successful@gmail.com");
        person.setSex(EnumSex.MALE);
        person.setBirthDate(LocalDate.of(1997, 10, 18));
        person.setCpf("317.752.450-55");
        person.setPhones(getCreatedValidPhones());

        return person;
    }

    private static List<Phone> getCreatedValidPhones() {
        List<Phone> phones = new ArrayList<>();
        Phone fix = new Phone(1L, "3353-1011", EnumTypePhone.FIXED, false, null);
        Phone phone = new Phone(2L, "3353-1011", EnumTypePhone.FIXED, true, null);

        phones.add(fix);
        phones.add(phone);

        return phones;
    }

    private static List<Phone> getCreateValidPhones() {
        List<Phone> phones = new ArrayList<>();
        Phone fix = new Phone(null, "3353-1011", EnumTypePhone.FIXED, false, null);
        Phone phone = new Phone(null, "3353-1011", EnumTypePhone.FIXED, true, null);

        phones.add(fix);
        phones.add(phone);

        return phones;
    }
}
