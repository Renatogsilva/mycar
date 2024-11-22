package br.com.renatogsilva.my_car.model.converters;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.dto.person.PersonResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {PhoneMapper.class})
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "phonesRequestDTOs", target = "phones")
    @Mapping(source = "sex", target = "sex")
    Person toPerson(PersonRequestDTO personRequestDTO);

    @Mapping(target = "phones", source = "phonesRequestDTOs")
    @Mapping(source = "sex", target = "sex")
    Person toPerson(@MappingTarget Person person, PersonRequestDTO personRequestDTO);

    @Mapping(target = "phonesResponseDTOs", source = "phones")
    @Mapping(target = "fullName", expression = "java(concatFullName(person.getFirstName(), person.getLastName()))")
    PersonResponseDTO toPersonResponseDTO(Person person);

    default String concatFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}