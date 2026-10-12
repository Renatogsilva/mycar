package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.api.config.auth.TokenRevocationConfig;
import br.com.renatogsilva.my_car.api.controller.UserController;
import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import br.com.renatogsilva.my_car.model.enums.EnumTypeUser;
import br.com.renatogsilva.my_car.service.user.UserService;
import br.com.renatogsilva.my_car.utils.user.FactoryUser;
import br.com.renatogsilva.my_car.utils.user.FactoryUserProfileRequesDTO;
import br.com.renatogsilva.my_car.utils.user.FactoryUserResponseDTO;
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

@DisplayName(value = "Testing class User Controller")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private TokenRevocationConfig tokenRevocationConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private Long userId;

    @BeforeEach
    public void setUp() {
        this.userId = 1L;
    }

    @Test
    @DisplayName(value = "Should register user successfully")
    public void shouldRegisterUserSuccessfully() throws Exception {
        //GIVEN ARRANGE
        UserResponseDTO createResponse = FactoryUserResponseDTO.userResponse().persisted().build();

        given(this.userService.create(any(UserRequestDTO.class))).willReturn(createResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FactoryUser.getUserRequestDTOObjectValidString()));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(createResponse.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId")
                        .value(createResponse.getPersonResponseDTO().getPersonId()));

        then(this.userService).should().create(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName(value = "Should not register the user with invalid data")
    public void shouldNotRegisterUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FactoryUser.getUserRequestDTOObjectInvalidString()));

        resultActions
                .andExpect(status().is4xxClientError());

        then(this.userService).should(never()).create(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName(value = "Should update user successfully")
    public void shouldUpdateUserSuccessfully() throws Exception {
        //GIVEN ARRANGE
        UserResponseDTO createResponse = FactoryUserResponseDTO.userResponse().persisted()
                .withUsername("username.alterado").build();

        given(this.userService.update(eq(this.userId), any(UserRequestDTO.class))).willReturn(createResponse);

        //WHEN ACT
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{id}", this.userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FactoryUser.getUserRequestDTOObjectValidString()));

        //THEN ASSERT
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(createResponse.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId").value(createResponse.getPersonResponseDTO().getPersonId()))
                .andExpect(jsonPath("$.username").value(createResponse.getUsername()));

        then(this.userService).should().update(eq(this.userId), any(UserRequestDTO.class));
    }

    @Test
    @DisplayName(value = "Should not update the user with invalid data")
    public void shouldNotUpdateUserWithInvalidData() throws Exception {
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{id}", this.userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FactoryUser.getUserRequestDTOObjectInvalidString()));

        resultActions
                .andExpect(status().is4xxClientError());

        then(this.userService).should(never()).create(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName(value = "Should successfully find the user by id")
    public void shouldSuccessfullyFindUserById() throws Exception {
        //GIVEN ARRANGE
        UserResponseDTO createResponse = FactoryUserResponseDTO.userResponse().persisted().build();

        given(this.userService.findById(this.userId)).willReturn(createResponse);

        //WHE ACT
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/{id}", this.userId)
                .contentType(MediaType.APPLICATION_JSON));

        //THEN ASSERT
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(createResponse.getUserId()))
                .andExpect(jsonPath("$.personResponseDTO.personId").value(createResponse.getPersonResponseDTO().getPersonId()))
                .andExpect(jsonPath("$.username").value(createResponse.getUsername()));

        then(this.userService).should().findById(this.userId);
    }

    @Test
    @DisplayName("Should bring a list of users")
    void shouldBringAListOfUsers() throws Exception {
        // GIVEN
        UserResponseListDTO userResponseListDTOUm =
                new UserResponseListDTO(
                        1L,
                        "Full last name",
                        "teste.com@email.com",
                        "085.549.795-80",
                        "Masculino",
                        EnumStatus.ACTIVE,
                        EnumStatus.ACTIVE.getDescription()
                );

        UserResponseListDTO userResponseListDTODois =
                new UserResponseListDTO(
                        2L,
                        "Full last name",
                        "teste2@email.com",
                        "085.549.795-81",
                        "Feminino",
                        EnumStatus.ACTIVE,
                        EnumStatus.ACTIVE.getDescription()
                );

        List<UserResponseListDTO> users = List.of(
                userResponseListDTOUm,
                userResponseListDTODois
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserResponseListDTO> page =
                new PageImpl<>(users, pageable, users.size());

        when(this.userService.findAllByFilters(
                EnumStatus.ACTIVE,
                EnumTypeUser.ADMIN,
                pageable
        )).thenReturn(page);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/user")
                        .param("status", "ACTIVE")
                        .param("typeUser", "ADMIN")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.size()").value(users.size()))
                .andExpect(jsonPath("$.totalElements").value(users.size()))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        verify(this.userService).findAllByFilters(
                EnumStatus.ACTIVE,
                EnumTypeUser.ADMIN,
                pageable
        );

        verifyNoMoreInteractions(this.userService);
    }

    @Test
    @DisplayName("Should bring an empty list of users")
    void shouldBringAnEmptyListOfUsers() throws Exception {

        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserResponseListDTO> page = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(this.userService.findAllByFilters(
                null,
                null,
                pageable
        )).thenReturn(page);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/user")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.content.size()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        verify(this.userService).findAllByFilters(
                null,
                null,
                pageable
        );

        verifyNoMoreInteractions(this.userService);
    }

    @Test
    @DisplayName(value = "Should deactivate user successfully")
    public void shouldDeactivateUserSuccessfully() throws Exception {
        //GIVEN ARRANGE
        doNothing().when(this.userService).disable(anyLong());

        //WHEN ACT
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/user/disable/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        //THEN ASSERT
        resultActions.andExpect(status().isNoContent());

        verify(this.userService).disable(anyLong());
        verify(this.userService, times(1)).disable(anyLong());
    }

    @Test
    @DisplayName(value = "Should successfully activate the user")
    public void shouldSuccessfullyActivateUser() throws Exception {
        //GIVEN ARRANGE
        doNothing().when(this.userService).enable(anyLong());

        //WHEN ACT
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/user/enable/{id}", anyLong())
                .accept(MediaType.APPLICATION_JSON));

        //THEN ASSERT
        resultActions.andExpect(status().isNoContent());

        verify(this.userService).enable(anyLong());
        verify(this.userService, times(1)).enable(anyLong());
    }

    @Test
    @DisplayName(value = "Should update password successfully")
    public void shouldUpdatePasswordSuccessfully() throws Exception {
        //GIVEN ARRANGE
        UserProfileRequestDTO userProfileRequestDTO = FactoryUserProfileRequesDTO.userProfileRequest().persisted().build();
        doNothing().when(this.userService).update(this.userId, userProfileRequestDTO);

        //WHEN ACT
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/{userId}/change-password", this.userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userProfileRequestDTO)));

        //THEN ASSERT
        resultActions.andExpect(status().isNoContent());

        verify(this.userService).update(this.userId, userProfileRequestDTO);
        verify(this.userService, times(1)).update(this.userId, userProfileRequestDTO);
    }
}
