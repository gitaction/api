package com.gitaction.api.application.service;

import com.gitaction.api.application.concept.BusinessService;
import com.gitaction.api.application.exception.AppException;
import com.gitaction.api.application.exception.ResourceNotFoundException;
import com.gitaction.api.application.model.Role;
import com.gitaction.api.application.model.RoleName;
import com.gitaction.api.application.model.User;
import com.gitaction.api.application.model.UserProfile;
import com.gitaction.api.application.repository.RoleRepository;
import com.gitaction.api.application.repository.UserRepository;
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
