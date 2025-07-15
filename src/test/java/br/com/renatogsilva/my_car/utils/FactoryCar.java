package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public static Car createValidCarObjectWithoutCreationDateAndstatusAndUserId(){
        Car car = new Car();

        car.setCarId(1L);
        car.setEngine("1.6");
        car.setExchange(EnumExchange.MANUAL);
        car.setColor("Branco");
        car.setExclusionDate(null);
        car.setMark("Wolkswagem");
        car.setBodyStyle("Hatch");
        car.setVersion("Polo MSI");
        car.setYearOfManufacture(2020);

        return car;
    }

    public static Car createValidCarObject(){
        Car car = new Car();

        car.setCarId(1L);
        car.setEngine("1.6");
        car.setExchange(EnumExchange.MANUAL);
        car.setColor("Branco");
        car.setExclusionDate(null);
        car.setMark("Wolkswagem");
        car.setBodyStyle("Hatch");
        car.setVersion("Polo MSI");
        car.setYearOfManufacture(2020);
        car.setStatus(EnumStatus.ACTIVE);
        car.setCreationDate(LocalDate.now());
        car.setUserCreation(new User(1L, null, null, null, false, null, null, null));

        return car;
    }

    public static Car createValidCarObjectAndInactive(){
        Car car = new Car();

        car.setCarId(1L);
        car.setEngine("1.6");
        car.setExchange(EnumExchange.MANUAL);
        car.setColor("Branco");
        car.setExclusionDate(null);
        car.setMark("Wolkswagem");
        car.setBodyStyle("Hatch");
        car.setVersion("Polo MSI");
        car.setYearOfManufacture(2020);
        car.setStatus(EnumStatus.INACTIVE);
        car.setCreationDate(LocalDate.now());
        car.setUserCreation(new User(1L, null, null, null, false, null, null, null));
        car.setExclusionDate(LocalDate.now());
        car.setUserExclusion(new User(2L, null, null, null, false, null, null, null));
        return car;
    }

    public static List<Car> createListValidCarObject(){
        List<Car> cars = new ArrayList<>();
        Car car = new Car();

        car.setCarId(1L);
        car.setEngine("1.6");
        car.setExchange(EnumExchange.MANUAL);
        car.setColor("Branco");
        car.setExclusionDate(null);
        car.setMark("Wolkswagem");
        car.setBodyStyle("Hatch");
        car.setVersion("Polo MSI");
        car.setYearOfManufacture(2020);
        car.setStatus(EnumStatus.ACTIVE);
        car.setCreationDate(LocalDate.now());
        car.setUserCreation(new User(1L, null, null, null, false, null, null, null));

        cars.add(car);

        return cars;
    }

    public static List<CarResponseListDTO> carResponseListDTOList(){
        List<CarResponseListDTO> carsResponseDto = new ArrayList<>();
        CarResponseListDTO carResponseDTO = new CarResponseListDTO();

        carResponseDTO.setMark("Wolkswagem");
        carResponseDTO.setCarId(1L);
        carResponseDTO.setEngine("1.6");
        carResponseDTO.setVersion("Polo MSI");
        carResponseDTO.setYearOfManufacture(2020);

        carsResponseDto.add(carResponseDTO);

        return carsResponseDto;
    }
}