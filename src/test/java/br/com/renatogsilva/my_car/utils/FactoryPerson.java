package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.person.PersonResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;

import java.time.LocalDate;

public class FactoryPerson {

    public static PersonRequestDTO createPersonRequestDTOObjectValid() {
        PersonRequestDTO personRequestDTO = new PersonRequestDTO();

        personRequestDTO.setPersonId(null);
        personRequestDTO.setCpf("295.464.615-20");
        personRequestDTO.setSex(EnumSex.MALE);
        personRequestDTO.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
        personRequestDTO.setFirstName("Emanuel");
        personRequestDTO.setLastName("Benjamin Benício Drumond");
        personRequestDTO.setBirthDate(LocalDate.of(1994, 10, 12));
        personRequestDTO.setPhonesRequestDTOs(FactoryPhone.createListPhoneRequestDTOObjectValid());

        return personRequestDTO;
    }

    public static PersonResponseDTO createPersonResponseDTOObjectValid() {
        PersonResponseDTO personResponseDTO = new PersonResponseDTO();

        personResponseDTO.setPersonId(1L);
        personResponseDTO.setCpf("355.137.120-24");
        personResponseDTO.setSex(EnumSex.MALE);
        personResponseDTO.setEmail("email@gmail.com");
        personResponseDTO.setFullName("FirstName LastName");
        personResponseDTO.setBirthDate(LocalDate.of(1994, 10, 12));
        personResponseDTO.setPhonesResponseDTOs(FactoryPhone.createListPhoneResponseDTOObjectValid());

        return personResponseDTO;
    }

    public static Person createPersonEntityObjectValid(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setFirstName("Emanuel");
        person.setLastName("Benjamin Benício Drumond");
        person.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
        person.setSex(EnumSex.MALE);
        person.setBirthDate(LocalDate.of(1994, 10, 12));
        person.setCpf("295.464.615-20");
        person.setPhones(FactoryPhone.createdListPhoneEntityObjectValid());

        return person;
    }

    public static Person updatePersonEntityObjectValid(){
        Person person = new Person();
        person.setPersonId(1L);
        person.setFirstName("Emanuel update");
        person.setLastName("Benjamin Benício Drumond update");
        person.setEmail("update.successful@gmail.com");
        person.setSex(EnumSex.MALE);
        person.setBirthDate(LocalDate.of(1997, 10, 18));
        person.setCpf("317.752.450-55");
        person.setPhones(FactoryPhone.createdListPhoneEntityObjectValid());

        return person;
    }
}
