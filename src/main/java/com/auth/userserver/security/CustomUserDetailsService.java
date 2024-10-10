package com.auth.userserver.security;

import com.auth.userserver.entities.User;
import com.auth.userserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Laddar en användare baserat på användarnamn.
     *
     * @param username Användarnamn.
     * @return UserDetails Objekt som innehåller användarens detaljer.
     * @throws UsernameNotFoundException Om användaren inte hittas.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(user);
    }

    /**
     * Laddar en användare baserat på publicId.
     *
     * @param publicId Användarens publicId.
     * @return UserDetails Objekt som innehåller användarens detaljer.
     * @throws UsernameNotFoundException Om användaren inte hittas.
     */
    public UserDetails loadUserByPublicId(String publicId) throws UsernameNotFoundException {
        User user = userRepository.findByPublicId(publicId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with publicId: " + publicId);
        }
        return new CustomUserDetails(user);
    }
}
