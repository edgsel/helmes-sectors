package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.UserRegisterDTO;
import com.helmes.sectorsapi.dto.UserAuthDTO;
import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.exception.EntityExistsException;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static com.helmes.sectorsapi.exception.ErrorCode.USER_EXISTS_ERROR;
import static com.helmes.sectorsapi.exception.ErrorCode.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(UserRegisterDTO userRegisterDTO) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRegisterDTO.getUsername()))) {
            throw new EntityExistsException("Username %s already exists", USER_EXISTS_ERROR.name());
        }

        var hashedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());

        return userRepository.save(User.toEntity(userRegisterDTO.getUsername(), hashedPassword));
    }

    public String authenticate(UserAuthDTO userAuthDTO) {
        var user = userRepository.findByUsername(userAuthDTO.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("User %s not found".formatted(userAuthDTO.getUsername()), USER_NOT_FOUND.name()));

        if (!passwordEncoder.matches(userAuthDTO.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password", INVALID_CREDENTIALS.name());
        }

        return jwtService.generateToken(user);
    }
}
