package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.api.config.auth.TokenRevocationConfig;
import br.com.renatogsilva.my_car.api.controller.CarController;
import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumExchange;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import br.com.renatogsilva.my_car.service.car.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName(value = "Testing class Car Controller")
@WebMvcTest(controllers = CarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CarControllerTest {

    @MockBean
    private CarService carService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private TokenRevocationConfig tokenRevocationConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private CarRequestDTO carRequestDTO;
    private CarResponseDTO carResponseDTO;
    private CarRequestDTO carRequestDTOFailure;
    private Long carId;

    @BeforeEach
    public void setup() {
        this.carRequestDTO = new CarRequestDTO(null, "Fiat", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");

        this.carResponseDTO = new CarResponseDTO(1L, "Fiat", 2020, "Black",
                "Sedan", EnumExchange.AUTOMATIC.getDescription(), "1.0 Turbo", "Cronos",
                EnumStatus.ACTIVE, EnumStatus.ACTIVE.getDescription());

        this.carRequestDTOFailure = new CarRequestDTO(null, "", 2020, "",
                "Sedan", EnumExchange.AUTOMATIC, "1.0 Turbo", "Cronos");

        this.carId = 1L;
    }

    @Test
    @DisplayName(value = "Should register vehicle successfully")
    public void shouldRegisterVehicleSuccessfully() throws Exception {
        given(carService.create(carRequestDTO)).willReturn(this.carResponseDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(this.carRequestDTO)));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(1L))
                .andExpect(jsonPath("$.mark").value("Fiat"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"))
                .andExpect(jsonPath("$.version").value("Cronos"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.enumStatusDescription").value("Ativo"));

        verify(carService).create(carRequestDTO);
        verify(carService, times(1)).create(this.carRequestDTO);
    }

    @Test
    @DisplayName(value = "Should not register a vehicle with invalid data")
    public void shouldNotRegisterVehicleWithInvalidData() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTOFailure)));

        resultActions
                .andExpect(status().is4xxClientError());

        verify(carService, never()).create(carRequestDTO);
        verify(carService, times(0)).create(this.carRequestDTO);
    }

    @Test
    @DisplayName(value = "Should update a vehicle successfully")
    public void shouldUpdateVehicleSuccessfuly() throws Exception {
        Long carId = 1L;

        carResponseDTO.setMark("Wolkswagem");
        carResponseDTO.setColor("Black");
        carResponseDTO.setYearOfManufacture(2024);

        when(carService.update(eq(carRequestDTO), eq(carId))).thenReturn(carResponseDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/car/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTO)));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(carId))
                .andExpect(jsonPath("$.mark").value("Wolkswagem"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2024))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.version").value("Cronos"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"));

        verify(carService).update(carRequestDTO, carId);
        verify(carService, times(1)).update(this.carRequestDTO, carId);
    }

    @Test
    @DisplayName(value = "Should not update a vehicle with invalid data")
    public void shouldNotUpdateVehicleWithInvalidData() throws Exception {
        ResultActions resultActions = mockMvc.perform(put("/api/v1/car/{id}", this.carId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(carRequestDTOFailure)));

        resultActions
                .andExpect(status().is4xxClientError());

        verify(carService, never()).update(carRequestDTO, this.carId);
        verify(carService, times(0)).update(this.carRequestDTO, this.carId);
    }

    @Test
    @DisplayName(value = "Should successfully find the vehicle by id")
    public void shouldVehicleByIdSuccessfully() throws Exception {
        when(carService.findById(this.carId)).thenReturn(this.carResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/car/{id}", this.carId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.carId").value(this.carId))
                .andExpect(jsonPath("$.mark").value("Fiat"))
                .andExpect(jsonPath("$.yearOfManufacture").value(2020))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.version").value("Cronos"))
                .andExpect(jsonPath("$.engine").value("1.0 Turbo"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.enumStatusDescription").value("Ativo"));

        verify(carService).findById(this.carId);
        verify(carService, times(1)).findById(this.carId);
    }

    @Test
    @DisplayName("Should bring a page of vehicles")
    void shouldBringAPageOfVehicles() throws Exception {

        CarResponseListDTO carResponseDTOFiat = new CarResponseListDTO(
                1L, "Fiat", 2020,
                "1.0 Turbo", "Cronos",
                EnumStatus.ACTIVE,
                EnumStatus.ACTIVE.getDescription()
        );

        CarResponseListDTO carResponseDTOWolkswagem = new CarResponseListDTO(
                2L, "Wolkswagem", 2021,
                "1.6", "Polo MSI",
                EnumStatus.ACTIVE,
                EnumStatus.ACTIVE.getDescription()
        );

        CarResponseListDTO carResponseDTOChevrolet = new CarResponseListDTO(
                3L, "Chevrolet", 2023,
                "1.6", "Onix",
                EnumStatus.ACTIVE,
                EnumStatus.ACTIVE.getDescription()
        );

        List<CarResponseListDTO> list = List.of(
                carResponseDTOFiat,
                carResponseDTOWolkswagem,
                carResponseDTOChevrolet
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<CarResponseListDTO> page =
                new PageImpl<>(list, pageable, list.size());

        when(carService.findAllByFilters(
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/car")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(list.size()))
                .andExpect(jsonPath("$.totalElements").value(list.size()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        verify(carService, times(1))
                .findAllByFilters(
                        eq(null),
                        eq(null),
                        any(Pageable.class)
                );
    }

    @Test
    @DisplayName("Should bring an empty page of vehicles")
    void shouldBringAnEmptyPageOfVehicles() throws Exception {

        List<CarResponseListDTO> list = List.of();

        Pageable pageable = PageRequest.of(0, 10);

        Page<CarResponseListDTO> page =
                new PageImpl<>(list, pageable, 0);

        when(carService.findAllByFilters(
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/car")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.empty").value(true));

        verify(carService, times(1))
                .findAllByFilters(
                        eq(null),
                        eq(null),
                        any(Pageable.class)
                );
    }

    @Test
    @DisplayName(value = "Should deactivate vehicle successfully")
    public void shouldDeactivateVehicleSuccessfully() throws Exception {
        doNothing().when(carService).disable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/car/disable/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(carService).disable(anyLong());
        verify(carService, times(1)).disable(anyLong());
    }

    @Test
    @DisplayName(value = "Should successfully activate the vehicle")
    public void shouldSuccessfullyActivateVehicle() throws Exception {
        doNothing().when(carService).enable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/car/enable/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(carService).enable(anyLong());
        verify(carService, times(1)).enable(anyLong());
    }
}
