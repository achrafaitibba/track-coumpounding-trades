package com.achrafaitibba.trackcompoundingtrades.repository;

import com.achrafaitibba.trackcompoundingtrades.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
