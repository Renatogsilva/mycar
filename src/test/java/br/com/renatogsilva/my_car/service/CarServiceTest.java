package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.model.converters.CarMapper;
import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.validations.CarBusinessRules;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
import br.com.renatogsilva.my_car.service.car.CarServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryCar;
import br.com.renatogsilva.my_car.utils.FactoryUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    public void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(carServiceImpl).build();

        carRequestDTO = FactoryCar.createCarRequestDTOObjectValid();
        carResponseDTO = FactoryCar.createCarResponseDTOObjectValid();
        car = FactoryCar.createCarObjectValid();
        user = FactoryUser.createUserObjectValid();
    }

    @Test
    @DisplayName("Should return a car create with successful")
    public void shouldReturnACarCreateWithSuccessful(){
        doNothing().when(this.carBusinessRules).validateInclusionRules(any(CarRequestDTO.class));

        given(this.authenticationService.getAuthenticatedUser()).willReturn(this.user);
        given(this.carMapper.toCar(this.carRequestDTO)).willReturn(this.car);
        given(this.carRepository.save(any(Car.class))).willReturn(this.car);
        given(this.carMapper.toCarResponseDto(this.car)).willReturn(this.carResponseDTO);

        this.carResponseDTO = this.carServiceImpl.create(this.carRequestDTO);

        assertNotNull(this.carResponseDTO);
        assertEquals(this.carRequestDTO.getMark(),this.carResponseDTO.getMark());
        assertEquals(this.carRequestDTO.getVersion(), this.carResponseDTO.getVersion());
        assertEquals(this.carRequestDTO.getYearOfManufacture(), this.carResponseDTO.getYearOfManufacture());

        verify(this.carBusinessRules).validateInclusionRules(any(CarRequestDTO.class));
        verify(this.carRepository).save(any(Car.class));
        verify(this.authenticationService).getAuthenticatedUser();

        verify(this.carBusinessRules, times(1)).validateInclusionRules(this.carRequestDTO);
        verify(this.carRepository, times(1)).save(this.car);
        verify(this.authenticationService, times(1)).getAuthenticatedUser();
    }
}
