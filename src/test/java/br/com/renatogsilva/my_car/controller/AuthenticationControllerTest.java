package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.api.controller.AuthenticationController;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.service.auth.AuthenticationService;
import br.com.renatogsilva.my_car.utils.FactoryAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName(value = "Testing class Authentication Controller")
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationController authenticationController;

    MockMvc mockMvc;

    private LoginRequestDTO loginRequestDTO;
    private LoginRequestDTO loginRequestDTOInvalid;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();

        this.loginRequestDTO = FactoryAuthentication.createLoginRequestDTOObjectValid();
        this.loginResponseDTO = FactoryAuthentication.createLoginResponseDTOObjectValid();
        this.loginRequestDTOInvalid = FactoryAuthentication.createLoginRequestDTOObjectInvalid();
    }

    @Test
    @DisplayName(value = "Should successfully authenticate")
    public void shouldSuccessfullyAuthenticate() throws Exception {
        given(authenticationService.findUserByUsername(loginRequestDTO)).willReturn(loginResponseDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.loginRequestDTO)));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.token").value(this.loginResponseDTO.getToken()))
                .andExpect(jsonPath("$.expiresIn").value(this.loginResponseDTO.getExpiresIn()));

        verify(authenticationService).findUserByUsername(loginRequestDTO);
        verify(authenticationService, times(1)).findUserByUsername(loginRequestDTO);
    }

    @Test
    @DisplayName(value = "Should log out successfully")
    public void shouldLogOutSuccessfully() throws Exception {
        doNothing().when(authenticationService).logout();

        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        verify(authenticationService).logout();
        verify(authenticationService, times(1)).logout();
    }
}
