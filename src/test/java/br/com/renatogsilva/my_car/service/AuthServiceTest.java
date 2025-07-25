package br.com.renatogsilva.my_car.service;

import br.com.renatogsilva.my_car.api.config.auth.JwtTokenProvider;
import br.com.renatogsilva.my_car.api.config.auth.TokenRevocationConfig;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.login.LoginRequestDTO;
import br.com.renatogsilva.my_car.model.dto.login.LoginResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.auth.AuthenticationServiceImpl;
import br.com.renatogsilva.my_car.utils.FactoryAuthentication;
import br.com.renatogsilva.my_car.utils.FactoryUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@DisplayName(value = "Testing class User Service")
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Mock
    private Authentication authentication;

    @Mock
    private TokenRevocationConfig tokenRevocationConfig;

    private MockMvc mockMvc;
    private LoginRequestDTO loginRequestDTOValid;
    private LoginRequestDTO loginRequestDTOPasswordInvalid;
    private LoginRequestDTO loginRequestDTOUsernameInvalid;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationServiceImpl).build();

        this.loginRequestDTOValid = FactoryAuthentication.createLoginRequestDTOObjectValid();
        this.loginRequestDTOPasswordInvalid = FactoryAuthentication.createLoginRequestDTOObjectPasswordInvalid();
        this.loginRequestDTOUsernameInvalid = FactoryAuthentication.createLoginRequestDTOObjectUsernameInvalid();
        this.authenticationServiceImpl.EXPIRATION_TIME = 1200000L;
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
        assertTrue(expectedExpirationTime <= loginResponseDTO.getExpiresIn());

        verify(this.userRepository).findUserByUsername(this.loginRequestDTOValid.getUsername());
        verify(this.userRepository, times(1)).findUserByUsername(this.loginRequestDTOValid.getUsername());
        verify(this.bCryptPasswordEncoder).matches(eq(this.loginRequestDTOValid.getPassword()), eq(user.getPassword()));
        verify(this.jwtTokenProvider).generateToken(loginRequestDTOValid.getUsername(), EnumTypeUser.ADMIN.getDescription());
        verify(this.jwtTokenProvider, times(1)).generateToken(loginRequestDTOValid.getUsername(), EnumTypeUser.ADMIN.getDescription());
    }

    @Test
    @DisplayName(value = "Should return exception when user not found")
    public void shouldReturnExceptionWhenUserNotFound() throws Exception {
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(null);

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> {
           this.authenticationServiceImpl.getAuthenticatedUser();
        });

        assertEquals("Usuário não encontrado", userNotFoundException.getMessage());
        assertEquals(404, userNotFoundException.getCode());
        verify(mockSecurityContext, times(1)).getAuthentication();
    }

    @Test
    @DisplayName(value = "Should return user with successful")
    public void shouldReturnUserWithSuccessfulAuthentication() throws Exception {
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockSecurityContext);
        given(mockSecurityContext.getAuthentication()).willReturn(this.authentication);
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getPrincipal()).willReturn("validUser");

        User userMock = FactoryUser.createUserObjectValid();
        given(userRepository.findUserByUsername("validUser")).willReturn(userMock);

        User result = this.authenticationServiceImpl.getAuthenticatedUser();

        assertNotNull(result);
        assertEquals("username.login", result.getUsername());
        assertEquals("Emanuel", result.getPerson().getFirstName());
        verify(this.userRepository, times(1)).findUserByUsername("validUser");
        verify(authentication, times(1)).isAuthenticated();
        verify(authentication, times(1)).getPrincipal();
        verifyNoMoreInteractions(this.userRepository);
        verifyNoMoreInteractions(this.bCryptPasswordEncoder);
        verifyNoMoreInteractions(this.bCryptPasswordEncoder);
        verifyNoMoreInteractions(this.jwtTokenProvider);
    }

    @Test
    @DisplayName(value = "Should revoke token when valid")
    public void shouldRevokeTokenWhenValid(){
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        given(mockSecurityContext.getAuthentication()).willReturn(mockAuthentication);

        String token = "Bearer validToken123qweasdf";
        String replaceToken = "validToken123qweasdf";
        given(mockAuthentication.getCredentials()).willReturn(token);
        given(jwtTokenProvider.validateToken(replaceToken)).willReturn(true);

        this.authenticationServiceImpl.logout();

        verify(mockAuthentication, times(1)).getCredentials();
        verify(jwtTokenProvider, times(1)).validateToken(replaceToken);
        verify(tokenRevocationConfig, times(1)).revokeToken(replaceToken);
    }

    @Test
    @DisplayName(value = "Should not revoke token when invalid")
    public void shouldNotRevokeTokenWhenInvalid(){
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        given(mockSecurityContext.getAuthentication()).willReturn(mockAuthentication);

        String token = "validToken123qweasdf";
        String replaceToken = "validToken123qweasdf";
        given(mockAuthentication.getCredentials()).willReturn(token);
        given(jwtTokenProvider.validateToken(replaceToken)).willReturn(false);

        this.authenticationServiceImpl.logout();

        verify(mockAuthentication, times(1)).getCredentials();
        verify(jwtTokenProvider, times(1)).validateToken(replaceToken);
        verify(tokenRevocationConfig, never()).revokeToken(replaceToken);
    }

    @Test
    @DisplayName(value = "Should return null authentication")
    public void shouldReturnNullAuthentication(){
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        given(mockSecurityContext.getAuthentication()).willReturn(null);

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            this.authenticationServiceImpl.getAuthenticatedUser();
        });

        assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), thrown.getCode());

        verify(mockSecurityContext, times(1)).getAuthentication();
        verify(this.userRepository, never()).findUserByUsername("TESTE");
    }

    @Test
    @DisplayName(value = "Should return false authentication")
    public void shouldReturnFalseAuthentication(){
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        given(mockSecurityContext.getAuthentication()).willReturn(mockAuthentication);
        given(mockAuthentication.isAuthenticated()).willReturn(false);

        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> {
            this.authenticationServiceImpl.getAuthenticatedUser();
        });

        assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(), thrown.getMessage());
        assertEquals(EnumMessageUserExceptions.USER_NOT_FOUND.getCode(), thrown.getCode());

        verify(mockSecurityContext, times(1)).getAuthentication();
        verify(this.userRepository, never()).findUserByUsername("TESTE");
    }
}