package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.api.config.auth.SecurityConfig;
import br.com.renatogsilva.my_car.api.controller.CarController;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumExchange;
import br.com.renatogsilva.my_car.service.car.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName(value = "Testing class Car Controller")
@Import(SecurityConfig.class)
public class CarControllerTest {

    @Mock
    private CarService carService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName(value = "Should register vehicle successfully")
    public void shouldRegisterVehicleSuccessfully() throws Exception {
        CarRequestDTO carRequestDTO = new CarRequestDTO(null, "Fiat", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");
        CarResponseDTO carResponseDTO = new CarResponseDTO(1L, "Fiat", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC.getDescription(), "1.0 Turbo", "Cronos");

        given(carService.create(carRequestDTO)).willReturn(carResponseDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTO)));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(1L))
                .andExpect(jsonPath("$.mark").value("Fiat"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"))
                .andExpect(jsonPath("$.version").value("Cronos"));

    }

    @Test
    @DisplayName(value = "Should not register a vehicle with invalid data")
    public void shouldNotRegisterVehicleWithInvalidData() throws Exception {
        CarRequestDTO carRequestDTO = new CarRequestDTO(null, "", 2020, "",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");

        ResultActions resultActions = mockMvc.perform(post("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTO)));

        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName(value = "Should update a vehicle successfully")
    public void shouldUpdateVehicleSuccessfuly() throws Exception {
        Long carId = 1L;

        CarRequestDTO carRequestDTO = new CarRequestDTO(null, "Fiat", 2020, "Red",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");

        CarResponseDTO carResponseDTO = new CarResponseDTO(carId, "Wolkswagem", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC.getDescription(), "1.0 Turbo", "Polo MSI");

        when(carService.update(eq(carRequestDTO), eq(carId))).thenReturn(carResponseDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/car/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTO)));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(carId))
                .andExpect(jsonPath("$.mark").value("Wolkswagem"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.version").value("Polo MSI"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"));

    }

    @Test
    @DisplayName(value = "Should not update a vehicle with invalid data")
    public void shouldNotUpdateVehicleWithInvalidData() throws Exception {
        Long carId = 1L;

        CarRequestDTO carRequestDTO = new CarRequestDTO(null, "", 2020, "Green",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");

        ResultActions resultActions = mockMvc.perform(put("/api/v1/car/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTO)));

        resultActions
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName(value = "Should successfully find the vehicle by id")
    public void shouldVehicleByIdSuccessfully() throws Exception {
        Long carId = 1L;

        CarResponseDTO carResponseDTO = new CarResponseDTO(carId, "Wolkswagem", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC.getDescription(), "1.0 Turbo", "Polo MSI");

        when(carService.findById(carId)).thenReturn(carResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/car/{id}", carId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(carId))
                .andExpect(jsonPath("$.mark").value("Wolkswagem"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.version").value("Polo MSI"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"));
    }

    @Test
    @DisplayName(value = "Should bring a list of vehicles")
    public void shouldBringAListOfVehicles() throws Exception {
        CarResponseListDTO carResponseDTOFiat = new CarResponseListDTO(1L, "Fiat", 2020,
                "1.0 Turbo", "Cronos");

        CarResponseListDTO carResponseDTOWolkswagem = new CarResponseListDTO(2L, "Wolkswagem", 2021,
                "1.6", "Polo MSI");

        CarResponseListDTO carResponseDTOChevrolet = new CarResponseListDTO(3L, "Chevrolet", 2023,
                "1.6", "Onix");

        List<CarResponseListDTO> list = List.of(carResponseDTOFiat, carResponseDTOWolkswagem, carResponseDTOChevrolet);

        when(carService.findAll()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/car/")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(list.size()));

        verify(carService).findAll();
        verify(carService, times(1)).findAll();
    }

    @Test
    @DisplayName(value = "Should bring an empty list of vehicles")
    public void shouldBringAnEmptyListOfVehicles() throws Exception {
        List<CarResponseListDTO> list = List.of();
        when(carService.findAll()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/car/")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$.size()").value(list.size()));

        verify(carService).findAll();
        verify(carService, times(1)).findAll();
    }

    @Test
    @DisplayName(value = "Should deactivate vehicle successfully")
    public void shouldDeactivateVehicleSuccessfully() throws Exception {
        doNothing().when(carService).disable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/car/desactive/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(carService).disable(anyLong());
        verify(carService, times(1)).disable(anyLong());
    }

    @Test
    @DisplayName(value = "Should successfully activate the vehicle")
    public void shouldSuccessfullyActivateVehicle() throws Exception {
        doNothing().when(carService).enable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/car/active/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(carService).enable(anyLong());
        verify(carService, times(1)).enable(anyLong());
    }
}
