package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.converters.CarMapper;
import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.car.CarNotFoundException;
import br.com.renatogsilva.my_car.model.validations.CarBusinessRules;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
import br.com.renatogsilva.my_car.service.car.CarServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryCar;
import br.com.renatogsilva.my_car.utils.FactoryUser;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@DisplayName(value = "Testing class Car Service")
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarBusinessRules carBusinessRules;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    private MockMvc mockMvc;
    private CarRequestDTO carRequestDTO;
    private CarResponseDTO carResponseDTO;
    private Car car;
    private User user;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(carServiceImpl).build();

        carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();
        carResponseDTO = FactoryCar.createCarResponseDTOObjectValid();
        car = FactoryCar.createValidCarObjectWithoutCreationDateAndstatusAndUserId();
        user = FactoryUser.createUserObjectValid();
    }

    @Test
    @DisplayName("Should return a car create with successful")
    public void shouldReturnACarCreateWithSuccessful() {
        doNothing().when(this.carBusinessRules).validateInclusionRules(any(CarRequestDTO.class));

        given(this.authenticationService.getAuthenticatedUser()).willReturn(this.user);
        given(this.carMapper.toCar(this.carRequestDTO)).willReturn(this.car);
        given(this.carRepository.save(any(Car.class))).willReturn(this.car);
        given(this.carMapper.toCarResponseDto(this.car)).willReturn(this.carResponseDTO);

        this.carResponseDTO = this.carServiceImpl.create(this.carRequestDTO);

        assertNotNull(this.carResponseDTO);
        assertEquals(this.carRequestDTO.getMark(), this.carResponseDTO.getMark());
        assertEquals(this.carRequestDTO.getVersion(), this.carResponseDTO.getVersion());
        assertEquals(this.carRequestDTO.getYearOfManufacture(), this.carResponseDTO.getYearOfManufacture());

        verify(this.carBusinessRules).validateInclusionRules(any(CarRequestDTO.class));
        var argumentCaptor = ArgumentCaptor.forClass(Car.class); //validar o que estou passando para frente no método
        verify(this.carRepository).save(argumentCaptor.capture());
        verify(this.authenticationService).getAuthenticatedUser();
        val carTest = argumentCaptor.getValue();

        assertNotNull(carTest.getCreationDate());
        assertNotNull(carTest.getUserCreation());
        assertNotNull(carTest.getStatus());

        verify(this.carBusinessRules, times(1)).validateInclusionRules(this.carRequestDTO);
        verify(this.carRepository, times(1)).save(this.car);
        verify(this.authenticationService, times(1)).getAuthenticatedUser();
    }

    @Test
    @DisplayName("Should must not register a new vehicle with duplicate data")
    public void ShouldMustNotRegisterANewVehicleWithDuplicateData() {

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
    }

    @Test
    @DisplayName("Should return a successfully updated car")
    public void shouldReturnASuccessfullyUpdateCar() {
        Car carExistent = FactoryCar.createValidCarObjectWithoutCreationDateAndstatusAndUserId();

        doNothing().when(this.carBusinessRules).validateUpdateRules(any(CarRequestDTO.class));

        given(this.authenticationService.getAuthenticatedUser()).willReturn(this.user);
        given(this.carRepository.findById(1L)).willReturn(Optional.of(carExistent));
        given(this.carMapper.toCar(carExistent, this.carRequestDTO)).willReturn(this.car);
        given(this.carRepository.save(any(Car.class))).willReturn(this.car);
        given(this.carMapper.toCarResponseDto(this.car)).willReturn(this.carResponseDTO);

        this.carResponseDTO = this.carServiceImpl.update(this.carRequestDTO, 1L);

        assertNotNull(this.carResponseDTO);
        assertEquals(this.carRequestDTO.getMark(), this.carResponseDTO.getMark());
        assertEquals(this.carRequestDTO.getVersion(), this.carResponseDTO.getVersion());
        assertEquals(this.carRequestDTO.getYearOfManufacture(), this.carResponseDTO.getYearOfManufacture());

        verify(this.carBusinessRules).validateUpdateRules(any(CarRequestDTO.class));
        verify(this.carBusinessRules, times(1)).validateUpdateRules(this.carRequestDTO);
        verify(this.carRepository, times(1)).save(this.car);
        verify(carRepository, times(1)).findById(1L);
        verify(carMapper, times(1)).toCarResponseDto(this.car);
    }

    @Test
    @DisplayName("Should throw CarNotFoundException when car does not exist")
    void shouldThrowExceptionWhenCarNotFound(){
        given(carRepository.findById(1L)).willReturn(Optional.empty());

        CarNotFoundException thrown = assertThrows(CarNotFoundException.class, () -> {
            carServiceImpl.update(this.carRequestDTO, 1L);
        });

        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageCarExceptions.CAR_NOT_FOUND.getCode(), thrown.getCode());

        verify(carRepository, times(1)).findById(1L);
        verify(carBusinessRules, times(1)).validateUpdateRules(this.carRequestDTO);
        verify(carRepository, never()).save(any());
    }
}
