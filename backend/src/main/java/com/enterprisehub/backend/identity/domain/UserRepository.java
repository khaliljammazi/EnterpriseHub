package com.enterprisehub.backend.identity.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);
}
