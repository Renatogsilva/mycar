package br.com.renatogsilva.my_car.api.controller;

import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.service.user.UserService;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Gerenciamento de usuários")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra usuários",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao cadastrar usuário")}, method = "POST")
    public UserResponseDTO create(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        logger.info("m: create - receiving request to create user object {}", userRequestDTO);

        return this.userService.create(userRequestDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza usuários",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário")}, method = "PUT")
    public UserResponseDTO update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        logger.info("m: update - receiving request to update user object {}", userRequestDTO);

        return this.userService.update(id, userRequestDTO);
    }

    @PutMapping("/{userId}/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Alterar senha",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário")}, method = "PUT")
    public void changePassword(@PathVariable Long userId, @RequestBody @Valid UserProfileRequestDTO userProfileRequestDTO) {
        logger.info("m: changePassword - receiving request to update user password");

        this.userService.update(userId, userProfileRequestDTO);
    }

    @PatchMapping(value = "/active/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ativar usuário",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao ativar usuário")}, method = "PATCH")
    public void enable(@PathVariable Long id) {
        logger.info("m: enable - receiving request to enable user object by id {}", id);

        this.userService.enable(id);
    }

    @PatchMapping(value = "/desactive/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Inativar usuário",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao inativar usuário")}, method = "PATCH")
    public void disable(@PathVariable Long id) {
        logger.info("m: disable - receiving request to disable user object by id {}", id);

        this.userService.disable(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta usuário por id",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Usuário informado não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao consultar usuário")}, method = "GET")
    public UserResponseDTO findById(@PathVariable long id) {
        logger.info("m: findById - receiving request to find user object by id {}", id);

        return this.userService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta lista de usuários",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao consultar lista de usuários")}, method = "GET")
    public List<UserResponseListDTO> findAll() {
        logger.info("m: findAll - receiving request to find all user objects");

        return this.userService.findAll();
    }
}