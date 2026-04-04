package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.enums.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import br.com.renatogsilva.my_car.utils.FactoryCar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@DisplayName(value = "Testing class Car Business Rules")
@ExtendWith(MockitoExtension.class)
public class CarBusinessRulesTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarBusinessRules carBusinessRules;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Should throw car duplication exception when creating a car that already exists")
    public void shouldThrowCarDuplicationException_whenCreatingCarThatAlreadyExists() {
        //GIVEN ARRANGE
        Car carEntityFromDB = FactoryCar.createValidCarObject();
        CarRequestDTO carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();

        when(this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine())).thenReturn(carEntityFromDB);

        //WHEN ACT
        CarDuplicationException carDuplicationException = Assertions.assertThrows(CarDuplicationException.class, () -> {
            this.carBusinessRules.validateInclusionRules(carRequestDTO);
        });

        //THEN ASSERT
        verify(this.carRepository).findCarDuplicatorByIdAndMarkAndVersionAndEngine(
                carRequestDTO.getCarId(), carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine()
        );
        verifyNoMoreInteractions(this.carRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(carDuplicationException),
                () -> Assertions.assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(), carDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getCode(), carDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should throw car duplication exception when updating a car that already exists")
    public void shouldThrowCarDuplicationException_whenUpdatingCarThatAlreadyExists() {
        //GIVEN ARRANGE
        Car carEntityFromDB = FactoryCar.createValidCarObject();
        CarRequestDTO carRequestDTO = FactoryCar.updateCarRequestDTOObjectValid();

        when(this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine())).thenReturn(carEntityFromDB);

        //WHEN ACT
        CarDuplicationException carDuplicationException = Assertions.assertThrows(CarDuplicationException.class, () -> {
            this.carBusinessRules.validateUpdateRules(carRequestDTO);
        });

        //THEN ASSERT
        verify(this.carRepository).findCarDuplicatorByIdAndMarkAndVersionAndEngine(
                carRequestDTO.getCarId(), carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine()
        );
        verifyNoMoreInteractions(this.carRepository);

        Assertions.assertAll(
                "Assert Group",
                () -> Assertions.assertNotNull(carDuplicationException),
                () -> Assertions.assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(), carDuplicationException.getMessage()),
                () -> Assertions.assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getCode(), carDuplicationException.getCode())
        );
    }

    @Test
    @DisplayName("Should return true when validate inclusion rules car is not exist")
    public void shouldReturnTrue_whenValidateInclusionRulesCarIsNotExist() {
        //GIVEN ARRANGE
        CarRequestDTO carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();

        when(this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(),
                carRequestDTO.getEngine())).thenReturn(null);

        //WHEN ACT
        this.carBusinessRules.validateInclusionRules(carRequestDTO);

        //THEN ASSERT
        verify(this.carRepository).findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine());
        verifyNoMoreInteractions(this.carRepository);
    }

    @Test
    @DisplayName("Should return true when validate update rules car is not exist")
    public void shouldReturnTrue_whenValidateUpdateRulesCarIsNotExist() {
        //GIVEN ARRANGE
        CarRequestDTO carRequestDTO = FactoryCar.updateCarRequestDTOObjectValid();

        when(this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(),
                carRequestDTO.getEngine())).thenReturn(null);

        //WHEN ACT
        this.carBusinessRules.validateUpdateRules(carRequestDTO);

        //THEN ASSERT
        verify(this.carRepository).findCarDuplicatorByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(),
                carRequestDTO.getMark(), carRequestDTO.getVersion(), carRequestDTO.getEngine());
        verifyNoMoreInteractions(this.carRepository);
    }
}
