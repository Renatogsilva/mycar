package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.person.PersonResponseDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactoryUser {

    public static UserRequestDTO createUserRequestDTOObjectValid() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        PersonRequestDTO personRequestDTO = new PersonRequestDTO();
        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO();
        List<PhoneRequestDTO> list = new ArrayList<>();

        userRequestDTO.setUserId(null);
        userRequestDTO.setUsername("username.login");

        personRequestDTO.setPersonId(null);
        personRequestDTO.setCpf("295.464.615-20");
        personRequestDTO.setSex(EnumSex.MALE);
        personRequestDTO.setEmail("emanuelbenjamindrumond@agreonoma.eng.br");
        personRequestDTO.setFirstName("Emanuel");
        personRequestDTO.setLastName("Benjamin Benício Drumond");
        personRequestDTO.setBirthDate(LocalDate.of(1994, 10, 12));

        phoneRequestDTO.setPhoneId(null);
        phoneRequestDTO.setTypePhone(EnumTypePhone.FIXED);
        phoneRequestDTO.setNumber("(62)98420-9870");
        phoneRequestDTO.setIsMain(true);

        list.add(phoneRequestDTO);

        personRequestDTO.setPhonesRequestDTOs(list);

        userRequestDTO.setPersonRequestDTO(personRequestDTO);
        return userRequestDTO;
    }

    public static UserResponseDTO createUserResponseDTOObjectValid() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        PersonResponseDTO personResponseDTO = new PersonResponseDTO();
        PhoneResponseDTO phoneResponseDTO = new PhoneResponseDTO();
        List<PhoneResponseDTO> list = new ArrayList<>();

        userResponseDTO.setUserId(1L);
        userResponseDTO.setUsername("username.teste.unitario");

        personResponseDTO.setPersonId(1L);
        personResponseDTO.setCpf("355.137.120-24");
        personResponseDTO.setSex(EnumSex.MALE);
        personResponseDTO.setEmail("email@gmail.com");
        personResponseDTO.setFullName("FirstName LastName");
        personResponseDTO.setBirthDate(LocalDate.of(1994, 10, 12));

        phoneResponseDTO.setPhoneId(1L);
        phoneResponseDTO.setTypePhone(EnumTypePhone.CELL_PHONE);
        phoneResponseDTO.setNumber("(62)98420-9870");
        phoneResponseDTO.setMain(true);

        list.add(phoneResponseDTO);

        personResponseDTO.setPhonesResponseDTOs(list);

        userResponseDTO.setPersonResponseDTO(personResponseDTO);

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
}
