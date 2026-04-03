package br.com.renatogsilva.my_car.utils.person;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.utils.phone.FactoryPhoneRequestDTO;

import java.time.LocalDate;
import java.util.List;

public class FactoryPersonRequestDTO {


    public static PersonRequestDTOBuilder personRequest() {
        return new PersonRequestDTOBuilder();
    }

    public static class PersonRequestDTOBuilder {

        private final PersonRequestDTO dto;

        public PersonRequestDTOBuilder() {
            this.dto = new PersonRequestDTO();

            // DEFAULT (válido para criação)
            dto.setPersonId(null);
            dto.setFirstName("Emanuel");
            dto.setLastName("Benjamin Benício Drumond");
            dto.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
            dto.setCpf("295.464.615-20");
            dto.setSex(EnumSex.MALE);
            dto.setBirthDate(LocalDate.of(1994, 10, 12));
            dto.setPhonesRequestDTOs(
                    FactoryPhoneRequestDTO.phoneList().build()
            );
        }

        public PersonRequestDTOBuilder persisted() {
            dto.setPersonId(1L);
            dto.setPhonesRequestDTOs(
                    FactoryPhoneRequestDTO.phoneList().persisted().build()
            );
            return this;
        }

        public PersonRequestDTOBuilder withId(Long id) {
            dto.setPersonId(id);
            return this;
        }

        public PersonRequestDTOBuilder withName(String firstName, String lastName) {
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            return this;
        }

        public PersonRequestDTOBuilder withEmail(String email) {
            dto.setEmail(email);
            return this;
        }

        public PersonRequestDTOBuilder withCpf(String cpf) {
            dto.setCpf(cpf);
            return this;
        }

        public PersonRequestDTOBuilder withBirthDate(LocalDate birthDate) {
            dto.setBirthDate(birthDate);
            return this;
        }

        public PersonRequestDTOBuilder withPhones(List<PhoneRequestDTO> phones) {
            dto.setPhonesRequestDTOs(phones);
            return this;
        }

        public PersonRequestDTO build() {
            return dto;
        }
    }
}
