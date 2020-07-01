package top.fsky.crawler.unit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.fsky.crawler.application.exception.ResourceNotFoundException;
import top.fsky.crawler.application.model.Role;
import top.fsky.crawler.application.model.RoleName;
import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.model.UserProfile;
import top.fsky.crawler.application.repository.RoleRepository;
import top.fsky.crawler.application.repository.UserRepository;
import top.fsky.crawler.application.service.UserBusinessService;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserBusinessServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserBusinessService userBusinessService;

    @BeforeEach
    void initService(){
        userBusinessService = new UserBusinessService(
                userRepository, roleRepository, passwordEncoder
        );
    }

    @Test
    void usernameAvailable(){
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        boolean isValid = userBusinessService.isUsernameAvailable("username");

        assertTrue(isValid);
    }

    @Test
    void usernameUnAvailable(){
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        boolean isValid = userBusinessService.isUsernameAvailable("username");

        assertFalse(isValid);
    }

    @Test
    void emailAvailable(){
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        boolean isValid = userBusinessService.isEmailAvailable("email");

        assertTrue(isValid);
    }

    @Test
    void emailUnAvailable(){
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        boolean isValid = userBusinessService.isEmailAvailable("email");

        assertFalse(isValid);
    }

    @Test
    void given_exist_username_when_check_profile_return_userProfile(){
        // Given
        Instant current_time = Instant.now();
        User user_moc = User.builder().id(1L).name("name").username("username").email("email").build();
        user_moc.setCreatedAt(current_time);
        UserProfile userProfile_expect = new UserProfile(user_moc.getId(), 
                user_moc.getUsername(), user_moc.getName(), current_time);

        // When
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user_moc));
        UserProfile userProfile_real = userBusinessService.getUserProfileByUsername("username");

        // Then
        assertEquals(userProfile_expect.getId(), userProfile_real.getId());
        assertEquals(userProfile_expect.getName(), userProfile_real.getName());
        assertEquals(userProfile_expect.getUsername(), userProfile_real.getUsername());
        assertEquals(userProfile_expect.getJoinedAt(), userProfile_real.getJoinedAt());
    }

    @Test
    void given_not_exist_username_when_check_profile_throw_exception() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userBusinessService.getUserProfileByUsername("username");
        });
    }

    @Test
    void create_user_succeed(){
        // Given
        User user_moc = User.builder()
                .name("name").username("username")
                .email("email").password("password")
                .build();
        User user_expected = User.builder()
                .name("name").username("username")
                .email("email").password("encoded_password")
                .build();
        Role role_moc = Role.builder().name(RoleName.ROLE_USER).build();
        user_expected.setRoles(Collections.singleton(role_moc));

        // When
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded_password");
        when(roleRepository.findByName(any(RoleName.class))).thenReturn(Optional.of(role_moc));
        when(userRepository.save(any(User.class))).thenReturn(user_moc);
        User user_real = userBusinessService.createUser(
                user_moc.getName(),
                user_moc.getUsername(),
                user_moc.getEmail(),
                user_moc.getPassword()
        );

        // Then
        assertEquals(user_expected.getName(), user_real.getName());
        assertEquals(user_expected.getUsername(), user_real.getUsername());
        assertEquals(user_expected.getEmail(), user_real.getEmail());
        assertEquals(user_expected.getPassword(), "encoded_password");
        assertEquals(user_expected.getRoles(), Collections.singleton(role_moc));
    }
}