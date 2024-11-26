package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.converters.CarMapper;
import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.exceptions.car.CarNotFoundException;
import br.com.renatogsilva.my_car.model.validations.CarBusinessRules;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarBusinessRules carBusinessRules;
    private final AuthenticationService authenticationService;

    private final CarMapper carMapper;

    @Transactional
    @Override
    public CarResponseDTO create(CarRequestDTO carRequestDTO) {
        this.carBusinessRules.checkRegistration(carRequestDTO);

        User user = this.authenticationService.getAuthenticatedUser();

        Car car = carMapper.toCar(carRequestDTO);
        car.setCreationDate(LocalDate.now());
        car.setStatus(EnumStatus.ACTIVE);
        car.setUserCreation(user);

        this.carRepository.save(car);

        return carMapper.toCarResponseDto(car);
    }

    @Transactional
    @Override
    public CarResponseDTO update(CarRequestDTO carRequestDTO, Long id) {
        carBusinessRules.checkUpdate(carRequestDTO);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car = carMapper.toCar(car, carRequestDTO);

        this.carRepository.save(car);
        return carMapper.toCarResponseDto(car);
    }

    @Transactional
    @Override
    public void disable(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car.setStatus(EnumStatus.INACTIVE);
        car.setExclusionDate(LocalDate.now());
        this.carRepository.save(car);
    }

    @Transactional
    @Override
    public void enable(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car.setStatus(EnumStatus.ACTIVE);
        car.setExclusionDate(null);
        this.carRepository.save(car);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseListDTO> findAll() {
        List<Car> cars = this.carRepository.findAll();

        return carMapper.toCarResponseListDto(cars);
    }

    @Transactional(readOnly = true)
    @Override
    public CarResponseDTO findById(Long id) {
        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        return carMapper.toCarResponseDto(car);
    }
}