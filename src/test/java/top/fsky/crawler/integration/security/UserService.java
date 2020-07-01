package top.fsky.crawler.integration.security;

import top.fsky.crawler.application.model.Role;
import top.fsky.crawler.application.model.RoleName;
import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class UserService {

    Map<String, UserPrincipal> userList = new HashMap<>();

    public UserService() {
        User admin = User.builder()
                .id(2l)
                .name("admin")
                .username("admin tester")
                .email("admin-test@air.com")
                .password("admin pwd")
                .roles(new HashSet<>(Arrays.asList(
                        Role.builder().name(RoleName.ROLE_ADMIN).build())))
                .build();
        UserPrincipal adminPrincipal = UserPrincipal.create(admin);

        User user = User.builder()
                .id(1l)
                .name("wayde")
                .username("wayde sun")
                .email("wayde.sun@gmail.com")
                .password("987123")
                .roles(new HashSet<>(Arrays.asList(
                        Role.builder().name(RoleName.ROLE_USER).build())))
                .build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        
        userList.put("admin", adminPrincipal);
        userList.put("user", userPrincipal);
    }

    public UserPrincipal loadUserByUsername(String userName) {
        UserPrincipal user = userList.get(userName);
        return user;
    }
}
