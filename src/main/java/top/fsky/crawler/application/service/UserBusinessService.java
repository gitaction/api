package top.fsky.crawler.application.service;

import top.fsky.crawler.application.concept.BusinessService;
import top.fsky.crawler.application.exception.AppException;
import top.fsky.crawler.application.exception.ResourceNotFoundException;
import top.fsky.crawler.application.model.Role;
import top.fsky.crawler.application.model.RoleName;
import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.model.UserProfile;
import top.fsky.crawler.application.repository.RoleRepository;
import top.fsky.crawler.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserBusinessService implements BusinessService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserBusinessService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Boolean isUsernameAvailable(String username){
        return !userRepository.existsByUsername(username);
    }

    public Boolean isEmailAvailable(String email){
        return !userRepository.existsByEmail(email);
    }

    public UserProfile getUserProfileByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        return  new UserProfile(user.getId(), user.getUsername(), 
                user.getName(), user.getCreatedAt());
    }

    public User createUser(String name, String userName, String email, String password){
        User user = User.builder()
                .name(name).username(userName)
                .email(email).password(password)
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }
}
