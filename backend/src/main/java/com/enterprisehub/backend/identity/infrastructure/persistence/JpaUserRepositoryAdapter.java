package com.enterprisehub.backend.identity.infrastructure.persistence;

import com.enterprisehub.backend.identity.domain.Email;
import com.enterprisehub.backend.identity.domain.User;
import com.enterprisehub.backend.identity.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(UserJpaEntity.fromDomain(user)).toDomain();
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmailIgnoreCase(email.value())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return repository.existsByEmailIgnoreCase(email.value());
    }
}
