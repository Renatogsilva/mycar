package br.com.renatogsilva.my_car.api.controller;

import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
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

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Gerenciamento de autenticação")
public class AuthenticationController {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Realiza autenticação",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao realizar autenticação")}, method = "POST")
    public LoginResponseDTO authentication(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        logger.info("m: authentication - receiving authentication request with loginRequestDTO object");

        return this.authenticationService.findUserByUsername(loginRequestDTO);
    }

    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Realiza logout",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logout com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "500", description = "Erro ao realizar autenticação")}, method = "POST")
    public void logout() {
        logger.info("m: logout - receiving request logout");

        this.authenticationService.logout();
    }
}
