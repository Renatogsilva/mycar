package br.com.renatogsilva.my_car.utils.person;

import br.com.renatogsilva.my_car.model.dto.person.PersonResponseDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import br.com.renatogsilva.my_car.model.enums.EnumSex;
import br.com.renatogsilva.my_car.utils.phone.FactoryPhoneResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class FactoryPersonResponseDTO {

    public static PersonResponseDTOBuilder personResponse() {
        return new PersonResponseDTOBuilder();
    }

    public static class PersonResponseDTOBuilder {

        private final PersonResponseDTO dto;

        public PersonResponseDTOBuilder() {
            this.dto = new PersonResponseDTO();

            // DEFAULT (resposta válida da API)
            dto.setPersonId(1L);
            dto.setFullName("Emanuel Benjamin Benício Drumond");
            dto.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
            dto.setCpf("295.464.615-20");
            dto.setSex(EnumSex.MALE);
            dto.setBirthDate(LocalDate.of(1994, 10, 12));
            dto.setPhonesResponseDTOs(
                    FactoryPhoneResponseDTO.phoneList().build()
            );
        }

        /**
         * Representa objeto vindo do banco (persistido)
         */
        public PersonResponseDTOBuilder persisted() {
            dto.setPersonId(1L);
            dto.setPhonesResponseDTOs(
                    FactoryPhoneResponseDTO.phoneList()
                            .persisted()
                            .build()
            );
            return this;
        }

        public PersonResponseDTOBuilder withId(Long id) {
            dto.setPersonId(id);
            return this;
        }

        public PersonResponseDTOBuilder withFullName(String fullName) {
            dto.setFullName(fullName);
            return this;
        }

        public PersonResponseDTOBuilder withEmail(String email) {
            dto.setEmail(email);
            return this;
        }

        public PersonResponseDTOBuilder withCpf(String cpf) {
            dto.setCpf(cpf);
            return this;
        }

        public PersonResponseDTOBuilder withSex(EnumSex sex) {
            dto.setSex(sex);
            return this;
        }

        public PersonResponseDTOBuilder withBirthDate(LocalDate birthDate) {
            dto.setBirthDate(birthDate);
            return this;
        }

        public PersonResponseDTOBuilder withPhones(List<PhoneResponseDTO> phones) {
            dto.setPhonesResponseDTOs(phones);
            return this;
        }

        public PersonResponseDTO build() {
            return dto;
        }
    }
}
