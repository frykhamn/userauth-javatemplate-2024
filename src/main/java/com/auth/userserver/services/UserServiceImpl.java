package com.auth.userserver.services;

import com.auth.userserver.dto.UserDto;
import com.auth.userserver.dto.UserRegisterRequest;
import com.auth.userserver.dto.UserResponse;
import com.auth.userserver.entities.User;
import com.auth.userserver.enums.UserRole;
import com.auth.userserver.exceptions.UserAlreadyExistsException;
import com.auth.userserver.exceptions.UserRegistrationException;
import com.auth.userserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public UserDto registerUser(UserRegisterRequest userRegisterRequest) {
        if (isUserExists(userRegisterRequest.getUsername())) {
            throw new UserAlreadyExistsException("User with name "
                    + userRegisterRequest.getUsername() + " already exists");
        }

        User user = buildUserFromRequest(userRegisterRequest);
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new UserRegistrationException("Failed to register user");
        }
        return new UserDto(savedUser);
    }


    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }


    private User buildUserFromRequest(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setEmail(userRegisterRequest.getEmail());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());
        user.setUserRole(UserRole.CUSTOMER);
        return user;
    }

    private boolean isUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
