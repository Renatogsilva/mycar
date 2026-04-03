package br.com.renatogsilva.my_car.utils.user;

import br.com.renatogsilva.my_car.model.dto.person.PersonResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.utils.person.FactoryPersonResponseDTO;

public class FactoryUserResponseDTO {

    public static UserResponseDTOBuilder userResponse() {
        return new UserResponseDTOBuilder();
    }

    // =========================
    // BUILDER
    // =========================
    public static class UserResponseDTOBuilder {

        private final UserResponseDTO dto;

        public UserResponseDTOBuilder() {
            this.dto = new UserResponseDTO();

            // DEFAULT (objeto válido retornado pela API)
            dto.setUserId(1L);
            dto.setUsername("username.teste.unitario");
            dto.setPersonResponseDTO(
                    FactoryPersonResponseDTO.personResponse().build()
            );
        }

        public UserResponseDTOBuilder persisted() {
            dto.setUserId(1L);
            dto.setPersonResponseDTO(
                    FactoryPersonResponseDTO.personResponse().persisted().build()
            );
            return this;
        }

        public UserResponseDTOBuilder withId(Long id) {
            dto.setUserId(id);
            return this;
        }

        public UserResponseDTOBuilder withUsername(String username) {
            dto.setUsername(username);
            return this;
        }

        public UserResponseDTOBuilder withPerson(PersonResponseDTO person) {
            dto.setPersonResponseDTO(person);
            return this;
        }

        public UserResponseDTO build() {
            return dto;
        }
    }
}
