package com.enterprisehub.backend.identity.presentation;

import com.enterprisehub.backend.identity.application.AuthenticationService;
import com.enterprisehub.backend.identity.application.TokenResult;
import com.enterprisehub.backend.identity.application.UserResult;
import com.enterprisehub.backend.identity.domain.Role;
import com.enterprisehub.backend.identity.domain.UserRepository;
import com.enterprisehub.backend.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
@ImportAutoConfiguration({
        SecurityAutoConfiguration.class,
        ServletWebSecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void registersAnEmployeeWithoutAuthentication() throws Exception {
        when(authenticationService.register("khalil@example.com", "secure-password"))
                .thenReturn(new UserResult(
                        UUID.randomUUID(),
                        "khalil@example.com",
                        Set.of(Role.EMPLOYEE),
                        true,
                        Instant.now()
                ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "khalil@example.com",
                                  "password": "secure-password"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("khalil@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("EMPLOYEE"));
    }

    @Test
    void returnsAJwtAfterLogin() throws Exception {
        Instant expiresAt = Instant.now().plusSeconds(3600);
        when(authenticationService.login("khalil@example.com", "secure-password"))
                .thenReturn(new TokenResult("signed.jwt.token", "Bearer", expiresAt));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "khalil@example.com",
                                  "password": "secure-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("signed.jwt.token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void rejectsInvalidCredentialsWithoutRevealingDetails() throws Exception {
        when(authenticationService.login("khalil@example.com", "wrong-password"))
                .thenThrow(new BadCredentialsException("Internal authentication detail"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "khalil@example.com",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
}
