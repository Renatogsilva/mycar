package br.com.renatogsilva.my_car.utils.user;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.utils.person.FactoryPerson;

import java.time.LocalDate;

public class FactoryUser {
    public static UserBuilder user() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private User user;

        public UserBuilder() {
            user = new User();

            // DEFAULT (estado padrão)
            user.setUsername("username.login");
            user.setPassword("abcd");
            user.setPerson(FactoryPerson.person().persisted().build());
        }

        public UserBuilder persisted() {
            user.setUserId(1L);
            user.setCreationDate(LocalDate.now());
            user.setTypeUser(EnumTypeUser.ADMIN);
            user.setStatus(EnumStatus.ACTIVE);
            user.setPrimaryAccess(false);
            return this;
        }

        public UserBuilder withUsername(String username) {
            user.setUsername(username);
            return this;
        }

        public UserBuilder withStatus(EnumStatus status) {
            user.setStatus(status);
            return this;
        }

        public UserBuilder withType(EnumTypeUser type) {
            user.setTypeUser(type);
            return this;
        }

        public UserBuilder withPerson(Person person) {
            user.setPerson(person);
            return this;
        }

        public User build() {
            return user;
        }
    }

    public static String getUserRequestDTOObjectInvalidString() {
        return "{\n" +
                "   \"username\":\"\",\n" +
                "   \"personRequestDTO\":{\n" +
                "      \"firstName\":\"Emanuel\",\n" +
                "      \"lastName\":\"Benjamin Benício Drumond\",\n" +
                "      \"email\":\"emanuelbenjamindrumond@agreonoma.eng.br\",\n" +
                "      \"cpf\":\"\",\n" +
                "      \"sex\": 1,\n" +
                "      \"birthDate\":\"1994-10-12\",\n" +
                "      \"phonesRequestDTOs\":[\n" +
                "         {\n" +
                "            \"number\":\"(62)98420-9870\",\n" +
                "            \"typePhone\": 1,\n" +
                "            \"isMain\":true\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";
    }

    public static String getUserRequestDTOObjectValidString() {
        return "{\n" +
                "   \"username\":\"username.login\",\n" +
                "   \"personRequestDTO\":{\n" +
                "      \"firstName\":\"Emanuel\",\n" +
                "      \"lastName\":\"Benjamin Benício Drumond\",\n" +
                "      \"email\":\"emanuelbenjamindrumond@agreonoma.eng.br\",\n" +
                "      \"cpf\":\"295.464.615-20\",\n" +
                "      \"sex\": 1,\n" +
                "      \"birthDate\":\"1994-10-12\",\n" +
                "      \"phonesRequestDTOs\":[\n" +
                "         {\n" +
                "            \"number\":\"(62)98420-9870\",\n" +
                "            \"typePhone\": 1,\n" +
                "            \"isMain\":true\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";
    }
}
