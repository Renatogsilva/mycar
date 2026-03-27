package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;

public class FactoryUser {

    public static UserRequestDTO createUserRequestDTOObjectValid() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();

        userRequestDTO.setUserId(null);
        userRequestDTO.setUsername("username.login");
        userRequestDTO.setPersonRequestDTO(FactoryPerson.createPersonRequestDTOObjectValid());

        return userRequestDTO;
    }

    public static UserResponseDTO createUserResponseDTOObjectValid() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setUserId(1L);
        userResponseDTO.setUsername("username.teste.unitario");

        userResponseDTO.setPersonResponseDTO(FactoryPerson.createPersonResponseDTOObjectValid());

        return userResponseDTO;
    }

    public static String createUserRequestDTOObjectInvalidString() {
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

    public static String createUserRequestDTOObjectValidString() {
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

    public static UserProfileRequestDTO createUserProfileRequestDTOObjectValid() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();

        userProfileRequestDTO.setUserId(1L);
        userProfileRequestDTO.setUsername("username.login");
        userProfileRequestDTO.setEmail("email@gmail.com");
        userProfileRequestDTO.setCurrentPassword("abcd");
        userProfileRequestDTO.setNewPassword("abcd123456789");

        return userProfileRequestDTO;
    }

    public static User createUserEntityObjectValid() {
        User user = new User();

        user.setUsername("username.login");
        user.setPassword("abcd");
        user.setStatus(EnumStatus.ACTIVE);
        user.setPerson(FactoryPerson.createPersonEntityObjectValid());

        return user;
    }

    public static User createUserEntityObjectValidAndInactive() {
        User user = new User();

        user.setUsername("username.login");
        user.setPassword("abcd");
        user.setStatus(EnumStatus.INACTIVE);
        user.setPerson(FactoryPerson.createPersonEntityObjectValid());

        return user;
    }
}
