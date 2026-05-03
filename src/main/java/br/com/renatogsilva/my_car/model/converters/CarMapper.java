package br.com.renatogsilva.my_car.model.converters;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumExchange;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "userExclusion", ignore = true)
    @Mapping(target = "userCreation", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(source = "exchange", target = "exchange")
    Car toCar(CarRequestDTO carRequestDTO);

    @Mapping(target = "userExclusion", ignore = true)
    @Mapping(target = "userCreation", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(source = "exchange", target = "exchange")
    Car toCar(@MappingTarget Car car, CarRequestDTO carRequestDTO);

    @Mapping(target = "enumExchangeDescription", expression = "java(getExchangeDescription(car.getExchange()))")
    @Mapping(target = "enumStatusDescription", expression = "java(getStatusDescription(car.getStatus()))")
    CarResponseDTO toCarResponseDto(Car car);

    @Mapping(target = "enumStatusDescription", expression = "java(getStatusDescription(cars.getStatus()))")
    CarResponseListDTO toCarResponseListDto(Car cars);

    List<CarResponseListDTO> toCarResponseListDto(List<Car> cars);

    default String getExchangeDescription(EnumExchange exchange) {
        return exchange.getDescription();
    }

    default String getStatusDescription(EnumStatus status) {
        return status.getDescription();
    }
}
