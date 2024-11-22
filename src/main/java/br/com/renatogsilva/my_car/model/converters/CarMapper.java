package br.com.renatogsilva.my_car.model.converters;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(source = "exchange", target = "exchange")
    Car toCar(CarRequestDTO carRequestDTO);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(source = "exchange", target = "exchange")
    Car toCar(@MappingTarget Car car, CarRequestDTO carRequestDTO);

    @Mapping(target = "enumExchangeDescription", ignore = true)
    CarResponseDTO toCarResponseDto(Car car);

    List<CarResponseListDTO> toCarResponseListDto(List<Car> cars);
}
