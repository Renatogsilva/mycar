package br.com.renatogsilva.my_car.model.converters;


import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {PersonMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "typeUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "primaryAccess", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "person", source = "personRequestDTO")
    User toUser(UserRequestDTO userRequestDTO);

    @Mapping(target = "typeUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "primaryAccess", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "person", source = "personRequestDTO")
    User toUser(@MappingTarget User user, UserRequestDTO userRequestDTO);

    @Mapping(target = "personResponseDTO", source = "person")
    UserResponseDTO toUserResponseDTO(User user);

    List<UserResponseListDTO> toUserResponseListDTO(List<User> users);

    @Mapping(target = "fullName",
            expression = "java(concatFullName(user.getPerson().getFirstName(), user.getPerson().getLastName()))")
    @Mapping(target = "email", source = "person.email")
    @Mapping(target = "cpf", source = "person.cpf")
    @Mapping(target = "enumSexDescription", expression = "java(getSexDescription(user.getPerson().getSex()))")
    @Mapping(target = "enumStatusDescription", expression = "java(getStatusDescription(user.getStatus()))")
    UserResponseListDTO toUserResponseListDTO(User user);

    default String concatFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    default String getStatusDescription(EnumStatus status) {
        return status.getDescription();
    }

    default String getSexDescription(EnumSex sex) {
        return sex.getDescription();
    }
}
