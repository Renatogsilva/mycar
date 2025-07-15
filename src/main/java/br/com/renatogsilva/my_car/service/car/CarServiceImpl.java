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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private static Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;
    private final CarBusinessRules carBusinessRules;
    private final AuthenticationService authenticationService;

    private final CarMapper carMapper;

    @Transactional
    @Override
    public CarResponseDTO create(CarRequestDTO carRequestDTO) {
        logger.info("m: create - Creating new car {}", carRequestDTO);

        this.carBusinessRules.validateInclusionRules(carRequestDTO);

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
        logger.info("m: update - Updating existing car {}", carRequestDTO);

        carBusinessRules.validateUpdateRules(carRequestDTO);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        car = carMapper.toCar(car, carRequestDTO);

        this.carRepository.save(car);

        logger.info("m: update - Car with ID {} updated successfully", id);

        return carMapper.toCarResponseDto(car);
    }

    @Transactional
    @Override
    public void disable(Long id) {
        logger.info("m: disable - Disabling existing car by id {}", id);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        User user = this.authenticationService.getAuthenticatedUser();

        if (user.getStatus().equals(EnumStatus.INACTIVE)) {
            logger.warn("m: disable - Car with id {} is already inactive", id);
            return;
        }

        car.setStatus(EnumStatus.INACTIVE);
        car.setExclusionDate(LocalDate.now());
        car.setUserExclusion(user);

        this.carRepository.save(car);

        logger.info("m: disable - Car with id {} disabled successfully", id);
    }

    @Transactional
    @Override
    public void enable(Long id) {
        logger.info("m: enable - Enabling existing car by id {}", id);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        if (car.getStatus().equals(EnumStatus.ACTIVE)) {
            logger.warn("m: enable - Car with id {} is already active", id);
            return;
        }

        car.setStatus(EnumStatus.ACTIVE);
        car.setExclusionDate(null);
        car.setUserExclusion(null);

        this.carRepository.save(car);

        logger.info("m: enable - Car with id {} enabled successfully", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarResponseListDTO> findAll() {
        logger.info("m: findAll - Finding all cars");

        List<Car> cars = this.carRepository.findAll();

        logger.info("m: findAll - Cars found successfully");

        return carMapper.toCarResponseListDto(cars);
    }

    @Transactional(readOnly = true)
    @Override
    public CarResponseDTO findById(Long id) {
        logger.info("m: findById - Attempting to find car by ID {}", id);

        Car car = this.carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(),
                        EnumMessageCarExceptions.CAR_NOT_FOUND.getCode()));

        logger.info("m: findById - Car with id {} found successfully", id);

        return carMapper.toCarResponseDto(car);
    }
}