package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;

import java.time.LocalDate;

public class FactoryCar {

    public static CarRequestDTO createCarRequestDTOObjectValid(){
        CarRequestDTO carRequestDTO = new CarRequestDTO();

        carRequestDTO.setMark("Wolkswagem");
        carRequestDTO.setYearOfManufacture(2020);
        carRequestDTO.setColor("Branco");
        carRequestDTO.setExchange(EnumExchange.MANUAL);
        carRequestDTO.setBodyStyle("hatch");
        carRequestDTO.setEngine("1.6");
        carRequestDTO.setVersion("Polo MSI");

        return carRequestDTO;
    }

    public static CarResponseDTO createCarResponseDTOObjectValid(){
        CarResponseDTO carResponseDTO = new CarResponseDTO();

        carResponseDTO.setColor("Branco");
        carResponseDTO.setMark("Wolkswagem");
        carResponseDTO.setCarId(1L);
        carResponseDTO.setEngine("1.6");
        carResponseDTO.setVersion("Polo MSI");
        carResponseDTO.setBodyStyle("Hatch");
        carResponseDTO.setYearOfManufacture(2020);
        carResponseDTO.setEnumExchangeDescription(EnumExchange.MANUAL.getDescription());

        return carResponseDTO;
    }

    public static Car createCarObjectValid(){
        Car car = new Car();

        car.setStatus(EnumStatus.ACTIVE);
        car.setCarId(1L);
        car.setEngine("1.6");
        car.setExchange(EnumExchange.MANUAL);
        car.setColor("Branco");
        car.setExclusionDate(null);
        car.setCreationDate(LocalDate.now());
        car.setUserCreation(new User(1L, null,null,null, false, null, null, null));
        car.setMark("Wolkswagem");
        car.setBodyStyle("Hatch");
        car.setVersion("Polo MSI");
        car.setYearOfManufacture(2020);

        return car;
    }
}