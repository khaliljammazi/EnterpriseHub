package com.enterprisehub.backend.identity.application;

import com.enterprisehub.backend.identity.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public UserResult register(String email, String password) {
        return userService.register(new RegisterUserCommand(
                email,
                password,
                Set.of(Role.EMPLOYEE)
        ));
    }

    public TokenResult login(String email, String password) {
        var authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password)
        );
        return jwtTokenService.generate(authentication);
    }
}
