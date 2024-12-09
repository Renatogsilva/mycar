package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.api.controller.UserController;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.service.user.UserService;
import br.com.renatogsilva.my_car.utils.Factory;
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


import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName(value = "Testing class User Controller")
public class UserControllerTest {

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private UserProfileRequestDTO userProfileRequestDTO;

    private Long userId;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        objectMapper = new ObjectMapper();

        userRequestDTO = Factory.createUserRequestDTOObjectValid();
        userResponseDTO = Factory.createUserResponseDTOObjectValid();
        userProfileRequestDTO = Factory.createUserProfileRequestDTOObjectValid();
        userId = 1L;
    }

    @Test
    @DisplayName(value = "Should register user successfully")
    public void shouldRegisterUserSuccessfully() throws Exception {
        given(userService.create(this.userRequestDTO)).willReturn(this.userResponseDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Factory.createUserRequestDTOObjectValidString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userResponseDTO.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId")
                        .value(userResponseDTO.getPersonResponseDTO().getPersonId()));

        verify(userService).create(this.userRequestDTO);
        verify(userService, times(1)).create(this.userRequestDTO);
    }

    @Test
    @DisplayName(value = "Should not register the user with invalid data")
    public void shouldNotRegisterUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Factory.createUserRequestDTOObjectInvalidString()));

        resultActions
                .andExpect(status().is4xxClientError());

        verify(userService, never()).create(this.userRequestDTO);
        verify(userService, times(0)).create(this.userRequestDTO);
    }

    @Test
    @DisplayName(value = "Should update user successfully")
    public void shouldUpdateUserSuccessfully() throws Exception {
        userResponseDTO.setUsername("username.alterado");

        when(userService.update(eq(this.userId), eq(this.userRequestDTO))).thenReturn(this.userResponseDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Factory.createUserRequestDTOObjectValidString()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(userResponseDTO.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId").value(userResponseDTO.getPersonResponseDTO().getPersonId()))
                .andExpect(jsonPath("$.username").value(userResponseDTO.getUsername()));

        verify(userService).update(this.userId, this.userRequestDTO);
        verify(userService, times(1)).update(this.userId, this.userRequestDTO);
    }

    @Test
    @DisplayName(value = "Should not update the user with invalid data")
    public void shouldNotUpdateUserWithInvalidData() throws Exception {
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Factory.createUserRequestDTOObjectInvalidString()));

        resultActions
                .andExpect(status().is4xxClientError());

        verify(userService, never()).update(this.userId, this.userRequestDTO);
        verify(userService, times(0)).update(this.userId, this.userRequestDTO);
    }

    @Test
    @DisplayName(value = "Should successfully find the user by id")
    public void shouldSuccessfullyFindUserById() throws Exception {
        when(userService.findById(eq(this.userId))).thenReturn(this.userResponseDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(userResponseDTO.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId").value(userResponseDTO.getPersonResponseDTO().getPersonId()))
                .andExpect(jsonPath("$.username").value(userResponseDTO.getUsername()));

        verify(userService).findById(this.userId);
        verify(userService, times(1)).findById(this.userId);
    }

    @Test
    @DisplayName(value = "Should bring a list of users")
    public void shouldBringAListOfUsers() throws Exception {
        UserResponseListDTO userResponseListDTOUm = new UserResponseListDTO(1L, "Full last name", "teste.com@email.com",
                "085.549.795-80", "Masculino", "Ativo");

        UserResponseListDTO userResponseListDTODois = new UserResponseListDTO(1L, "Full last name", "teste.com@email.com",
                "085.549.795-80", "Feminino", "Ativo");

        List<UserResponseListDTO> list = List.of(userResponseListDTOUm, userResponseListDTODois);

        when(userService.findAll()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(list.size()));

        verify(userService).findAll();
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName(value = "Should bring an empty list of users")
    public void shouldBringAnEmptyListOfUsers() throws Exception {
        List<UserResponseListDTO> list = List.of();

        when(userService.findAll()).thenReturn(list);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$.size()").value(list.size()));

        verify(userService).findAll();
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName(value = "Should deactivate user successfully")
    public void shouldDeactivateUserSuccessfully() throws Exception {
        doNothing().when(userService).disable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/user/desactive/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(userService).disable(anyLong());
        verify(userService, times(1)).disable(anyLong());
    }

    @Test
    @DisplayName(value = "Should successfully activate the user")
    public void shouldSuccessfullyActivateUser() throws Exception {
        doNothing().when(userService).enable(anyLong());

        ResultActions resultActions = mockMvc.perform(patch("/api/v1/user/active/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());

        verify(userService).enable(anyLong());
        verify(userService, times(1)).enable(anyLong());
    }

    @Test
    @DisplayName(value = "Should update password successfully")
    public void shouldUpdatePasswordSuccessfully() throws Exception {
        doNothing().when(userService).update(userId, userProfileRequestDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{userId}/change-password", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileRequestDTO)));

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());

        resultActions.andExpect(status().isNoContent());

        verify(userService).update(userId, userProfileRequestDTO);
        verify(userService, times(1)).update(userId, userProfileRequestDTO);
    }
}
