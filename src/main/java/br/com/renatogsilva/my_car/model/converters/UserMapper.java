package br.com.renatogsilva.my_car.model.converters;


import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {PersonMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "primaryAccess", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "person", source = "personRequestDTO")
    User toUser(UserRequestDTO userRequestDTO);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "primaryAccess", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "person", source = "personRequestDTO")
    User toUser(@MappingTarget User user, UserRequestDTO userRequestDTO);

//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "primaryAccess", ignore = true)
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "personResponseDTO", source = "person")
    UserResponseDTO toUserResponseDTO(User user);
}
