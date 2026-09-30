package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.common.ConflictException;
import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.User;
import com.enterprisehub.backend.identity.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserResult register(RegisterUserCommand command) {
        Email email = Email.of(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("A user with this email already exists");
        }

        User user = User.register(
                email,
                passwordHasher.hash(command.password()),
                command.roles()
        );

        return UserResult.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResult> findAll() {
        return userRepository.findAll().stream()
                .map(UserResult::from)
                .toList();
    }
}
