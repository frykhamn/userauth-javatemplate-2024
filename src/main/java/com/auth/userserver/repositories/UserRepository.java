package com.auth.userserver.repositories;

import com.auth.userserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAllByUsername(String name);

    User findByPublicId(String publicId);
}