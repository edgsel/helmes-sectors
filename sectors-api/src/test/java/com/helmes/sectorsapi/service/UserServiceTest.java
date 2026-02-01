package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.request.UserAuthDTO;
import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.exception.UserExistsException;
import com.helmes.sectorsapi.mapper.UserMapper;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void register_Success() {
        // given
        var dto = new UserAuthDTO("testuser", "password123");
        var user = User.builder().id(1L).username("testuser").build();
        var hashedPassword = "hashedPassword";
        var jwtToken = "jwt-token";

        when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn(hashedPassword);
        when(userMapper.toEntity(dto.username(), hashedPassword)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        // when
        var result = userService.register(dto);

        // then
        assertThat(result.jwt()).isEqualTo(jwtToken);
        verify(userRepository).save(user);
    }

    @Test
    void register_UserExists_ThrowsException() {
        // given
        var dto = new UserAuthDTO("existinguser", "password123");
        when(userRepository.existsByUsername(dto.username())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.register(dto))
            .isInstanceOf(UserExistsException.class)
            .hasMessageContaining(dto.username());

        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_Success() {
        // given
        var dto = new UserAuthDTO("testuser", "password123");
        var user = User.builder().id(1L).username("testuser").passwordHash("hashedPassword").build();
        var jwtToken = "jwt-token";

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        // when
        var result = userService.authenticate(dto);

        // then
        assertThat(result.jwt()).isEqualTo(jwtToken);
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        // given
        var dto = new UserAuthDTO("testuser", "wrongpassword");
        var user = User.builder().id(1L).username(dto.username()).passwordHash("hashedPassword").build();

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPasswordHash())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.authenticate(dto)).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        // given
        var dto = new UserAuthDTO("unknown", "password123");
        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.authenticate(dto)).isInstanceOf(BadCredentialsException.class);
    }
}
