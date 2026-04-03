package br.com.renatogsilva.my_car.utils.user;

import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;

public class FactoryUserProfileRequesDTO {

    public static UserProfileRequestDTOBuilder userProfileRequest() {
        return new UserProfileRequestDTOBuilder();
    }

    public static class UserProfileRequestDTOBuilder {

        private final UserProfileRequestDTO dto;

        public UserProfileRequestDTOBuilder() {
            this.dto = new UserProfileRequestDTO();

            // DEFAULT (válido)
            dto.setUserId(null);
            dto.setUsername("username.login");
            dto.setEmail("email@gmail.com");
            dto.setCurrentPassword("current123");
            dto.setNewPassword("newPassword123");
        }

        public UserProfileRequestDTOBuilder persisted() {
            dto.setUserId(1L);
            return this;
        }

        public UserProfileRequestDTOBuilder withUserId(Long userId) {
            dto.setUserId(userId);
            return this;
        }

        public UserProfileRequestDTOBuilder withUsername(String username) {
            dto.setUsername(username);
            return this;
        }

        public UserProfileRequestDTOBuilder withEmail(String email) {
            dto.setEmail(email);
            return this;
        }

        public UserProfileRequestDTOBuilder withCurrentPassword(String currentPassword) {
            dto.setCurrentPassword(currentPassword);
            return this;
        }

        public UserProfileRequestDTOBuilder withNewPassword(String newPassword) {
            dto.setNewPassword(newPassword);
            return this;
        }

        public UserProfileRequestDTO build() {
            return dto;
        }
    }
}
