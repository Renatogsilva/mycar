package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.car.CarNotFoundException;
import br.com.renatogsilva.my_car.model.validations.CarBusinessRules;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
import br.com.renatogsilva.my_car.service.car.CarServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryCar;
import br.com.renatogsilva.my_car.utils.user.FactoryUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@DisplayName(value = "Testing class Car Service")
@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarBusinessRules carBusinessRules;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    private User user;

    @BeforeEach
    public void setup() {
        user = FactoryUser.user().build();
    }

    @Test
    @DisplayName("Should return a car create with successful")
    public void shouldReturnACarCreateWithSuccessful() {
        Car carEntity = FactoryCar.createValidCarObjectWithoutCreationDateAndstatusAndUserId();
        CarRequestDTO carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();

        doNothing().when(this.carBusinessRules).validateInclusionRules(any(CarRequestDTO.class));

        when(this.authenticationService.getAuthenticatedUser()).thenReturn(this.user);
        when(this.carRepository.save(any(Car.class))).thenReturn(carEntity);

        CarResponseDTO carResponseDTO = this.carServiceImpl.create(carRequestDTO);

        assertNotNull(carResponseDTO);
        assertEquals(carRequestDTO.getMark(), carResponseDTO.getMark());
        assertEquals(carRequestDTO.getVersion(), carResponseDTO.getVersion());
        assertEquals(carRequestDTO.getYearOfManufacture(), carResponseDTO.getYearOfManufacture());

        var argumentCaptor = ArgumentCaptor.forClass(Car.class);

        verify(this.carRepository).save(argumentCaptor.capture());

        Car carArgumentCapture = argumentCaptor.getValue();

        assertNotNull(carArgumentCapture.getCreationDate());
        assertNotNull(carArgumentCapture.getUserCreation());
        assertNotNull(carArgumentCapture.getStatus());

        assertEquals(carRequestDTO.getMark(), carArgumentCapture.getMark());
        assertEquals(carRequestDTO.getVersion(), carArgumentCapture.getVersion());
        assertEquals(carRequestDTO.getEngine(), carArgumentCapture.getEngine());
        assertEquals(carRequestDTO.getBodyStyle(), carArgumentCapture.getBodyStyle());
        assertEquals(carRequestDTO.getColor(), carArgumentCapture.getColor());

        assertEquals(EnumStatus.ACTIVE, carArgumentCapture.getStatus());

        assertEquals(this.user, carArgumentCapture.getUserCreation());

        verify(this.carBusinessRules, times(1)).validateInclusionRules(carRequestDTO);
        verify(this.carRepository, times(1)).save(any(Car.class));
        verify(this.authenticationService, times(1)).getAuthenticatedUser();

        verifyNoMoreInteractions(this.carBusinessRules);
        verifyNoMoreInteractions(this.carRepository);
        verifyNoMoreInteractions(this.authenticationService);
    }

    @Test
    @DisplayName("Should must not register a new vehicle with duplicate data")
    public void shouldThrowExceptionWhenCarIsDuplicated() {
        CarRequestDTO carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();

        CarDuplicationException exception = new CarDuplicationException(
                EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(),
                EnumMessageCarExceptions.CAR_DUPLICATE.getCode()
        );

        willThrow(exception)
                .given(carBusinessRules).validateInclusionRules(carRequestDTO);

        CarDuplicationException thrown = assertThrows(
                CarDuplicationException.class,
                () -> carServiceImpl.create(carRequestDTO)
        );

        assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_DUPLICATE.getCode(), thrown.getCode());

        verify(carBusinessRules, times(1)).validateInclusionRules(carRequestDTO);
        verify(authenticationService, never()).getAuthenticatedUser();
        verify(carRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return a successfully updated car")
    public void shouldReturnSuccessfullyUpdatedCar() {
        CarRequestDTO carRequestDTO = FactoryCar.updateCarRequestDTOObjectValid();
        Car carEntity = FactoryCar.createValidCarObject();

        doNothing().when(this.carBusinessRules).validateUpdateRules(carRequestDTO);

        given(this.carRepository.findById(1L)).willReturn(Optional.of(carEntity));
        given(this.carRepository.save(carEntity)).willReturn(carEntity);

        CarResponseDTO carResponseDTO = this.carServiceImpl.update(carRequestDTO, carRequestDTO.getCarId());

        ArgumentCaptor<Car> argumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(this.carRepository).save(argumentCaptor.capture());
        Car carArgumentCapture = argumentCaptor.getValue();

        assertNotNull(carArgumentCapture);
        assertNotNull(carResponseDTO);

        assertEquals(carArgumentCapture.getMark(), carResponseDTO.getMark());
        assertEquals(carArgumentCapture.getVersion(), carResponseDTO.getVersion());
        assertEquals(carArgumentCapture.getYearOfManufacture(), carResponseDTO.getYearOfManufacture());
        assertEquals(carArgumentCapture.getEngine(), carResponseDTO.getEngine());
        assertEquals(carArgumentCapture.getBodyStyle(), carResponseDTO.getBodyStyle());
        assertEquals(carArgumentCapture.getColor(), carResponseDTO.getColor());
        assertEquals(EnumStatus.ACTIVE, carArgumentCapture.getStatus());

        verify(this.carBusinessRules).validateUpdateRules(any(CarRequestDTO.class));
        verify(this.carBusinessRules, times(1)).validateUpdateRules(carRequestDTO);
        verify(this.carRepository, times(1)).save(carEntity);
        verify(this.carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw CarNotFoundException when car does not exist")
    void shouldThrowExceptionWhenCarNotFound() {
        CarRequestDTO carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();

        given(carRepository.findById(1L)).willReturn(Optional.empty());

        CarNotFoundException thrown = assertThrows(CarNotFoundException.class, () -> {
            carServiceImpl.update(carRequestDTO, 1L);
        });

        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getCode(), thrown.getCode());

        verify(carRepository, times(1)).findById(1L);
        verify(carBusinessRules, times(1)).validateUpdateRules(carRequestDTO);
        verify(carRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should disable registration successfully")
    void shouldDisableRegistrationSuccessfully() {
        Car entity = FactoryCar.createValidCarObject();

        given(carRepository.findById(1L)).willReturn(Optional.of(entity));
        given(this.authenticationService.getAuthenticatedUser()).willReturn(this.user);

        this.carServiceImpl.disable(1L);
        var argumentCaptor = ArgumentCaptor.forClass(Car.class);

        then(this.carRepository).should().save(argumentCaptor.capture());
        then(this.carRepository).should(times(1)).findById(1L);
        then(this.authenticationService).should(times(1)).getAuthenticatedUser();

        Car carArgumentCaptor = argumentCaptor.getValue();

        assertNotNull(carArgumentCaptor.getCreationDate());
        assertNotNull(carArgumentCaptor.getExclusionDate());
        assertNotNull(carArgumentCaptor.getUserCreation());
        assertNotNull(carArgumentCaptor.getUserExclusion());
        assertNotNull(carArgumentCaptor.getStatus());
        assertEquals(EnumStatus.INACTIVE, carArgumentCaptor.getStatus());
    }

    @Test
    @DisplayName("Should throw an exception when querying registry to disable")
    void shouldThrowAnExceptionWhenQueryingRegistryToDisable() {
        given(this.carRepository.findById(1L)).willReturn(Optional.empty());

        CarNotFoundException thrown = assertThrows(CarNotFoundException.class, () -> {
            this.carServiceImpl.disable(1L);
        });

        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getCode(), thrown.getCode());

        verify(this.carRepository, times(1)).findById(1L);
        verify(this.carRepository, never()).save(any());
        verify(this.authenticationService, never()).getAuthenticatedUser();
    }

    @Test
    @DisplayName("Should not deactivate an already inactive record")
    void shouldNotDeactivateAnAlreadyInactiveRecord() {
        Car entity = FactoryCar.createValidCarObjectAndInactive();

        given(carRepository.findById(1L)).willReturn(Optional.of(entity));

        this.carServiceImpl.disable(1L);

        assertEquals(EnumStatus.INACTIVE, entity.getStatus());
        assertNotNull(entity.getExclusionDate());

        verify(this.carRepository, times(1)).findById(1L);
        verify(this.authenticationService, never()).getAuthenticatedUser();
        verify(this.carRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should enable registration successfully")
    void shouldEnableRegistrationSuccessfully() {
        Car entity = FactoryCar.createValidCarObjectAndInactive();

        given(this.carRepository.findById(1L)).willReturn(Optional.of(entity));
        given(this.carRepository.save(any(Car.class))).willReturn(entity);

        this.carServiceImpl.enable(1L);
        var argumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(this.carRepository).save(argumentCaptor.capture());
        Car carArgumentCaptor = argumentCaptor.getValue();

        assertNull(carArgumentCaptor.getExclusionDate());
        assertNull(carArgumentCaptor.getUserExclusion());
        assertNotNull(carArgumentCaptor.getStatus());

        assertEquals(EnumStatus.ACTIVE, carArgumentCaptor.getStatus());

        verify(this.carRepository, times(1)).findById(1L);
        verify(this.carRepository, times(1)).save(any(Car.class));
    }

    @Test
    @DisplayName("Should throw an exception when querying registry to enable")
    void shouldThrowAnExceptionWhenQueryingRegistryToEnable() {
        given(this.carRepository.findById(1L)).willReturn(Optional.empty());

        CarNotFoundException thrown = assertThrows(CarNotFoundException.class, () -> {
            this.carServiceImpl.enable(1L);
        });

        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getCode(), thrown.getCode());

        verify(this.carRepository, times(1)).findById(1L);
        verify(this.carRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should not enable a record that is already enabled")
    void shouldNotEnableARecordThatIsAlreadyEnabled() {
        Car entity = FactoryCar.createValidCarObject();

        given(carRepository.findById(1L)).willReturn(Optional.of(entity));

        this.carServiceImpl.enable(1L);

        assertEquals(EnumStatus.ACTIVE, entity.getStatus());

        verify(this.carRepository, times(1)).findById(1L);
        verify(this.carRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should search for a record by id")
    void shouldSearchForARecordById() {
        Car entity = FactoryCar.createValidCarObject();
        given(carRepository.findById(1L)).willReturn(Optional.of(entity));

        CarResponseDTO carResponse = this.carServiceImpl.findById(1L);

        assertNotNull(carResponse);

        verify(this.carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when not finding record by id")
    void shouldThrowExceptionWhenNotFindingRecordById() {
        given(carRepository.findById(1L)).willReturn(Optional.empty());

        CarNotFoundException thrown = assertThrows(CarNotFoundException.class, () -> {
            carServiceImpl.findById(1L);
        });

        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getCode(), thrown.getCode());

        verify(this.carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return a list of records")
    void shouldReturnAListOfRecords() {
        List<Car> cars = FactoryCar.createListValidCarObject();
        List<CarResponseListDTO> carsResponseDto = FactoryCar.carResponseListDTOList();

        given(this.carRepository.findAll()).willReturn(cars);

        List<CarResponseListDTO> carsResponseDtoList = this.carServiceImpl.findAll();

        assertNotNull(carsResponseDtoList);
        assertEquals(carsResponseDto, carsResponseDtoList);
        assertEquals(1, carsResponseDtoList.size());

        verify(this.carRepository, times(1)).findAll();
    }
}