package com.denisov.onbalance.auth.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    public Optional<UserEntity> findByName(String name);

    public Optional<UserEntity> findByEmail(String email);
}
