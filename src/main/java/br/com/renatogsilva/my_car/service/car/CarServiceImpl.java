package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.exceptions.car.CarNotFoundException;
import br.com.renatogsilva.my_car.model.validations.CarValidations;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarValidations carValidations;

    @Transactional
    @Override
    public CarResponseDTO create(CarRequestDTO carRequestDTO) {
        this.carValidations.checkRegistration(carRequestDTO);

        Car car = new Car(carRequestDTO);
        car.setCreationDate(LocalDate.now());
        car.setStatus(EnumStatus.ACTIVE.getCode());

        this.carRepository.save(car);

        return new CarResponseDTO(car);
    }

    @Transactional
    @Override
    public CarResponseDTO update(CarRequestDTO carRequestDTO, Long id) {
        carValidations.checkUpdate(carRequestDTO);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car.setEngine(carRequestDTO.getEngine());
        car.setColor(carRequestDTO.getColor());
        car.setExchange(carRequestDTO.getExchange().getCode());
        car.setMark(carRequestDTO.getMark());
        car.setVersion(carRequestDTO.getVersion());
        car.setBodyStyle(carRequestDTO.getBodyStyle());
        car.setYearOfManufacture(carRequestDTO.getYearOfManufacture());

        this.carRepository.save(car);
        return new CarResponseDTO(car);
    }

    @Transactional
    @Override
    public void disable(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car.setStatus(EnumStatus.INACTIVE.getCode());
        car.setExclusionDate(LocalDate.now());
        this.carRepository.save(car);
    }

    @Transactional
    @Override
    public void enable(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car.setStatus(EnumStatus.ACTIVE.getCode());
        car.setExclusionDate(null);
        this.carRepository.save(car);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseListDTO> findAll() {
        List<Car> cars = this.carRepository.findAll();
        List<CarResponseListDTO> carResponseListDTOS = new ArrayList<>();
        for (Car car : cars) {
            carResponseListDTOS.add(new CarResponseListDTO(car));
        }
        return carResponseListDTOS;
    }

    @Transactional(readOnly = true)
    @Override
    public CarResponseDTO findById(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        return new CarResponseDTO(car);
    }
}