package top.fsky.crawler.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.security.UserPrincipal;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest extends IntegrationBaseTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static User testUser;
    private static String encodedPassword;
    private static Authentication authentication;

    @BeforeAll
    public static void beforeAll() {
        testUser = User.builder()
                .id(1L).name("wayde").username("Wayde Sun")
                .email("wayde.sun@gmail.com").password("987123")
                .build();
        encodedPassword = "$2a$10$4bQz7v/hC5iY4XHf.Qv7SuJxf5InAaX9myIJrd4gooZwkk/aBTjAi";
        UserPrincipal userPrincipal = UserPrincipal.create(testUser);
        authentication = new PreAuthenticatedAuthenticationToken(
                userPrincipal, "fake_one", userPrincipal.getAuthorities()
        );
    }
    
    @Test
    @Order(1)
    public void given_valid_user_when_sign_up_then_new_user_registered_success() throws Exception {
        when(passwordEncoder.encode(any(String.class))).thenReturn(encodedPassword);
        mockMvc.perform(post("/api/auth/sign-up")
                .content(objectMapper.writeValueAsString(testUser))
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    @Order(2)
    public void given_invalid_user_when_sign_up_then_bad_request_error_received() throws Exception {
        when(passwordEncoder.encode(any(String.class))).thenReturn(encodedPassword);
        String requestBody = "{\n" +
                "      \"name\": \"\",\n" +
                "      \"username\": \"\",\n" +
                "      \"email\": \"wrong-format-email\",\n" +
                "      \"password\": \"\"\n" +
                "}";
        mockMvc.perform(post("/api/auth/sign-up")
                .content(requestBody)
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(3)
    public void given_registered_user_when_sign_in_then_jwt_received() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        String requestBody = String.format("{\n" +
                "      \"email\": \"%s\",\n" +
                "      \"password\": \"%s\"\n" +
                "}", testUser.getEmail(), testUser.getPassword());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/sign-in")
                .content(requestBody)
                .contentType("application/json");

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenType").value("Bearer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists());
    }

    @Test
    @Order(4)
    public void given_invalid_user_when_sign_in_then_bad_request_error_received() throws Exception {
        String requestBody = "{\n" +
                "      \"usernameOrEmail\": \"\",\n" +
                "      \"password\": \"\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/sign-in")
                .content(requestBody)
                .contentType("application/json");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}
