package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.AuthResponseDTO;
import com.helmes.sectorsapi.dto.UserAuthDTO;
import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.exception.EntityExistsException;
import com.helmes.sectorsapi.mapper.UserMapper;
import com.helmes.sectorsapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.helmes.sectorsapi.exception.ErrorCode.INVALID_CREDENTIALS;
import static com.helmes.sectorsapi.exception.ErrorCode.USER_EXISTS_ERROR;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO register(UserAuthDTO userAuthDTO) {
        if (userRepository.existsByUsername(userAuthDTO.username())) {
            throw new EntityExistsException("Username %s already exists", USER_EXISTS_ERROR.name());
        }

        var hashedPassword = passwordEncoder.encode(userAuthDTO.password());
        var user = userRepository.save(userMapper.toEntity(userAuthDTO.username(), hashedPassword));

        return new AuthResponseDTO(jwtService.generateToken(user));
    }

    public AuthResponseDTO authenticate(UserAuthDTO userAuthDTO) {
        var user = userRepository.findByUsername(userAuthDTO.username()).orElse(null);
        var passwordMatches = user != null && passwordEncoder.matches(userAuthDTO.password(), user.getPasswordHash());

        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid username or password", INVALID_CREDENTIALS.name());
        }

        return new AuthResponseDTO(jwtService.generateToken(user));
    }
}
