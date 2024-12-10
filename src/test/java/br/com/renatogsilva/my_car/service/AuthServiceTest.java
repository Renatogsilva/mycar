package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@DisplayName(value = "Testing class User Service")
@TestPropertySource(properties = "jwt.expiration.time=1200000")
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Value("${jwt.expiration.time}")
    private Long EXPIRATION_TIME;

    private MockMvc mockMvc;
    private LoginRequestDTO loginRequestDTOValid;
    private LoginRequestDTO loginRequestDTOPasswordInvalid;
    private LoginRequestDTO loginRequestDTOUsernameInvalid;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationServiceImpl).build();

        this.loginRequestDTOValid = FactoryAuthentication.createLoginRequestDTOObjectValid();
        this.loginRequestDTOPasswordInvalid = FactoryAuthentication.createLoginRequestDTOObjectPasswordInvalid();
        this.loginRequestDTOUsernameInvalid = FactoryAuthentication.createLoginRequestDTOObjectUsernameInvalid();
    }

    @Test
    @DisplayName(value = "Should throw exception when password doesn't match")
    public void shouldThrowExceptionWhenPasswordDoesNotMatch() throws Exception {
        User user = new User(1L, "teste.teste", "$2a$10$AsMzGLVz.4ADQQvZsXByKuBrGFVatlYPswMiOS9BwpsFqFwfnRp9y", LocalDate.now(),
                false, EnumStatus.ACTIVE, EnumTypeUser.ADMIN, null);

        given(this.userRepository.findUserByUsername(this.loginRequestDTOPasswordInvalid.getUsername())).willReturn(user);
        given(this.bCryptPasswordEncoder.matches(this.loginRequestDTOPasswordInvalid.getPassword(), user.getPassword())).willReturn(false);

        UserAuthenticationException exception = assertThrows(UserAuthenticationException.class, () -> {
            this.authenticationServiceImpl.findUserByUsername(this.loginRequestDTOPasswordInvalid);
        });

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
        assertEquals(401, exception.getCode());
        verify(this.userRepository).findUserByUsername(this.loginRequestDTOPasswordInvalid.getUsername());
        verify(this.userRepository, times(1)).findUserByUsername(this.loginRequestDTOPasswordInvalid.getUsername());
        verify(this.bCryptPasswordEncoder).matches(this.loginRequestDTOPasswordInvalid.getPassword(), user.getPassword());
        verify(this.bCryptPasswordEncoder, times(1)).matches(this.loginRequestDTOPasswordInvalid.getPassword(), user.getPassword());
        verify(this.jwtTokenProvider, never()).generateToken(this.loginRequestDTOPasswordInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
        verify(this.jwtTokenProvider, times(0)).generateToken(this.loginRequestDTOUsernameInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
    }

    @Test
    @DisplayName(value = "Should throw exception when login doesn't match")
    public void shouldThrowExceptionWhenLoginDoesntMatch() throws Exception {
        given(this.userRepository.findUserByUsername(loginRequestDTOPasswordInvalid.getUsername())).willReturn(null);

        UserAuthenticationException exception = assertThrows(UserAuthenticationException.class, () -> {
            this.authenticationServiceImpl.findUserByUsername(this.loginRequestDTOPasswordInvalid);
        });

        assertEquals("Usuário ou senha inválidos", exception.getMessage());
        assertEquals(401, exception.getCode());
        verify(this.userRepository, times(1)).findUserByUsername(this.loginRequestDTOPasswordInvalid.getUsername());
        verify(this.bCryptPasswordEncoder, never()).encode(this.loginRequestDTOPasswordInvalid.getPassword());
        verify(this.bCryptPasswordEncoder, times(0)).encode(this.loginRequestDTOPasswordInvalid.getPassword());
        verify(this.jwtTokenProvider, never()).generateToken(loginRequestDTOUsernameInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
        verify(this.jwtTokenProvider, times(0)).generateToken(loginRequestDTOUsernameInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
    }

    @Test
    @DisplayName(value = "Should return LoginResponseDTO when login is successful")
    public void shouldReturnLoginResponseDTOWhenLoginIsSuccessful() throws Exception {
        User user = new User(1L, "teste.teste", "$2a$10$AsMzGLVz.4ADQQvZsXByKuBrGFVatlYPswMiOS9BwpsFqFwfnRp9y", LocalDate.now(),
                false, EnumStatus.ACTIVE, EnumTypeUser.ADMIN, null);
        long expectedExpirationTime = System.currentTimeMillis() + 1200000;

        given(this.userRepository.findUserByUsername(this.loginRequestDTOPasswordInvalid.getUsername())).willReturn(user);
        given(this.bCryptPasswordEncoder.matches(eq(this.loginRequestDTOValid.getPassword()), eq(user.getPassword()))).willReturn(true);
        given(this.jwtTokenProvider.generateToken(eq(this.loginRequestDTOValid.getUsername()), eq(EnumTypeUser.ADMIN.getDescription())))
                .willReturn("Bearer ");

        LoginResponseDTO loginResponseDTO = this.authenticationServiceImpl.findUserByUsername(this.loginRequestDTOValid);

        assertNotNull(loginResponseDTO);
        assertTrue(expectedExpirationTime >= loginResponseDTO.getExpiresIn());

        verify(this.userRepository).findUserByUsername(this.loginRequestDTOValid.getUsername());
        verify(this.userRepository, times(1)).findUserByUsername(this.loginRequestDTOValid.getUsername());
        verify(this.bCryptPasswordEncoder).matches(eq(this.loginRequestDTOValid.getPassword()), eq(user.getPassword()));
        verify(this.bCryptPasswordEncoder, times(1)).encode(this.loginRequestDTOValid.getPassword());
        verify(this.jwtTokenProvider).generateToken(loginRequestDTOUsernameInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
        verify(this.jwtTokenProvider, times(1)).generateToken(loginRequestDTOUsernameInvalid.getUsername(), EnumTypeUser.ADMIN.getDescription());
    }
}
