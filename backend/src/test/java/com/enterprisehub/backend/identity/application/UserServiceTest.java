package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.PasswordHash;
import com.enterprisehub.backend.identity.domain.Role;
import com.enterprisehub.backend.identity.domain.User;
import com.enterprisehub.backend.identity.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UserService userService;

    @Test
    void registersAUserWithAHashedPassword() {
        Email email = Email.of("khalil@enterprisehub.com");
        PasswordHash passwordHash = new PasswordHash("bcrypt-hash");
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordHasher.hash("secure-password")).thenReturn(passwordHash);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.rehydrate(
                    UUID.randomUUID(),
                    user.email(),
                    user.passwordHash(),
                    user.roles(),
                    user.enabled(),
                    user.createdAt()
            );
        });

        UserResult result = userService.register(new RegisterUserCommand(
                " Khalil@EnterpriseHub.com ",
                "secure-password",
                Set.of(Role.EMPLOYEE)
        ));

        assertThat(result.id()).isNotNull();
        assertThat(result.email()).isEqualTo("khalil@enterprisehub.com");
        assertThat(result.roles()).containsExactly(Role.EMPLOYEE);
        verify(passwordHasher).hash("secure-password");
    }

    @Test
    void rejectsADuplicateEmailBeforeHashingThePassword() {
        Email email = Email.of("khalil@enterprisehub.com");
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> userService.register(new RegisterUserCommand(
                email.value(),
                "secure-password",
                Set.of(Role.EMPLOYEE)
        )))
                .isInstanceOf(ConflictException.class)
                .hasMessage("A user with this email already exists");
    }
}
