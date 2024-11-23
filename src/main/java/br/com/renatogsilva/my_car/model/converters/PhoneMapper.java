package br.com.renatogsilva.my_car.model.converters;

import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {PersonMapper.class})
public interface PhoneMapper {
    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    @Mapping(target = "isMain", source = "isMain")
    @Mapping(target = "person", ignore = true)
    Phone toPhone(PhoneRequestDTO requestDTO);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "isMain", source = "isMain")
    Phone toPhone(@MappingTarget Phone phone, PhoneRequestDTO requestDTO);

    @Mapping(target = "main", source = "isMain")
    PhoneResponseDTO toPhoneResponseDTO(Phone phone);
}
