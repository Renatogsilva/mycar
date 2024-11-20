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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/car")
@RequiredArgsConstructor
@Tag(name = "Car", description = "API de gerenciamento de carros")
public class CarController {

    private final CarService carService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cadastra veículos",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao cadastrar veículo")}, method = "POST")
    public CarResponseDTO create(@RequestBody @Valid CarRequestDTO carRequestDTO) {
        return this.carService.create(carRequestDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
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
        return this.carService.findAll();
    }

    @DeleteMapping(value = "/active/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ativa veículo",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo ativado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Veículo informado não existe"),
                    @ApiResponse(responseCode = "500", description = "Erro ao ativar veículo")}, method = "DELETE")
    public void enable(@PathVariable Long id) {
        this.carService.enable(id);
    }

    @DeleteMapping(value = "/desactive/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa veículo",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo destivado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Veículo informado não existe"),
                    @ApiResponse(responseCode = "500", description = "Erro ao desativar veículo")}, method = "DELETE")
    public void disable(@PathVariable Long id) {
        this.carService.disable(id);
    }
}
