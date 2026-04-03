package br.com.renatogsilva.my_car.utils.user;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;

public class FactoryUserRequestDTO {
    public static UserRequestDTOBuilder userRequest() {
        return new UserRequestDTOBuilder();
    }

    public static class UserRequestDTOBuilder {

        private final UserRequestDTO dto;

        public UserRequestDTOBuilder() {
            this.dto = new UserRequestDTO();

            dto.setUserId(null);
            dto.setUsername("username.login");
            dto.setPersonRequestDTO(
                    FactoryPersonRequestDTO.personRequest().build()
            );
        }

        public UserRequestDTOBuilder persisted() {
            dto.setUserId(1L);
            dto.setPersonRequestDTO(
                    FactoryPersonRequestDTO.personRequest().persisted().build()
            );
            return this;
        }

        public UserRequestDTOBuilder withId(Long id) {
            dto.setUserId(id);
            return this;
        }

        public UserRequestDTOBuilder withUsername(String username) {
            dto.setUsername(username);
            return this;
        }

        public UserRequestDTOBuilder withPerson(PersonRequestDTO person) {
            dto.setPersonRequestDTO(person);
            return this;
        }

        public UserRequestDTOBuilder withoutPerson() {
            dto.setPersonRequestDTO(null);
            return this;
        }

        public UserRequestDTOBuilder withInvalidUsername() {
            dto.setUsername("");
            return this;
        }

        public UserRequestDTO build() {
            return dto;
        }
    }
}
