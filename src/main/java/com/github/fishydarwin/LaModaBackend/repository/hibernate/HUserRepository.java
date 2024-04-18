package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.hibernate.HUserWrapper;
import jakarta.annotation.Nullable;
import org.springframework.data.repository.CrudRepository;

public interface HUserRepository extends CrudRepository<HUserWrapper, Integer> {
    @Nullable
    HUserWrapper findByEmailAndPasswordObfuscated(String email, String passwordObfuscated);

    @Nullable
    HUserWrapper findByEmail(String email);
}
