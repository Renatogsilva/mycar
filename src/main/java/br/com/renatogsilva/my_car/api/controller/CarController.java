package br.com.renatogsilva.my_car.api.controller;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.service.car.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/car")
@RequiredArgsConstructor
@Tag(name = "Car", description = "Gerenciamento de carros")
public class CarController {

    private static Logger logger = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cadastra veículos",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "409", description = "Veículo já cadastrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao cadastrar veículo")}, method = "POST")
    public CarResponseDTO create(@RequestBody @Valid CarRequestDTO carRequestDTO) {
        logger.info("m: create - receiving request to create car object {}", carRequestDTO);

        return this.carService.create(carRequestDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza veículos",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao atualizar veículo")}, method = "PUT")
    public CarResponseDTO update(@Valid @PathVariable Long id, @RequestBody @Valid CarRequestDTO carRequestDTO) {
        logger.info("m: update - receiving request to update car object {} and id {}", carRequestDTO, id);

        return this.carService.update(carRequestDTO, id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta veículo por id",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Dados de requisição inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro ao consultar veículo")}, method = "GET")
    public CarResponseDTO findById(@PathVariable Long id) {
        logger.info("m: findById - receiving request to find car object by id {}", id);

        return this.carService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta todos os veículos cadastrados",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao consultar veículo")}, method = "GET")
    public List<CarResponseListDTO> findAll() {
        logger.info("m: findAll - receiving request to find all cars objects");

        return this.carService.findAll();
    }

    @PatchMapping(value = "/active/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ativa veículo",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo ativado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Veículo informado não existe"),
                    @ApiResponse(responseCode = "500", description = "Erro ao ativar veículo")}, method = "PATCH")
    public void enable(@PathVariable Long id) {
        logger.info("m: enable - receiving request to enable car object by id {}", id);

        this.carService.enable(id);
    }

    @PatchMapping(value = "/desactive/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa veículo",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo destivado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Veículo informado não existe"),
                    @ApiResponse(responseCode = "500", description = "Erro ao desativar veículo")}, method = "PATCH")
    public void disable(@PathVariable Long id) {
        logger.info("m: disable - receiving request to disable car object by id {}", id);

        this.carService.disable(id);
    }
}
