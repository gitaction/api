package top.fsky.crawler.integration;

import top.fsky.crawler.application.model.User;
import top.fsky.crawler.integration.security.WithMockJwtUser;
import org.junit.jupiter.api.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest extends IntegrationBaseTest {
    private static User testUser;

    @BeforeAll
    public static void BeforeAll() {
        testUser = User.builder()
                .id(1l).name("wayde").username("wayde sun")
                .email("wayde.sun@gmail.com").password("987123")
                .build();
    }

    @Test
    @Order(1)
    public void given_non_exist_username_when_check_username_availability_then_return_available() throws Exception {
        mockMvc.perform(get("/api/users/checkUsernameAvailability")
                .param("username", testUser.getUsername())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    @Order(2)
    public void given_non_exist_email_when_check_email_availability_then_return_available() throws Exception {
        mockMvc.perform(get("/api/users/checkEmailAvailability")
                .param("email", testUser.getEmail())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    @Order(3)
    @Sql(value = {"classpath:/sql/insert_users.sql"})
    public void given_exist_username_when_check_username_availability_then_return_not_available() throws Exception {
        mockMvc.perform(get("/api/users/checkUsernameAvailability")
                .param("username", testUser.getUsername())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
    @Order(4)
    public void given_exist_email_when_check_email_availability_then_return_not_available() throws Exception {
        mockMvc.perform(get("/api/users/checkEmailAvailability")
                .param("email", testUser.getEmail())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(false));
    }

    @Test
    @Order(5)
    @WithMockJwtUser("user")
    public void given_valid_user_when_check_me_then_detail_user_information_received() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("wayde sun"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("wayde"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockJwtUser("user")
    @Order(6)
    public void given_valid_user_when_get_user_by_name_then_detail_user_information_received() throws Exception {
        mockMvc.perform(get("/api/users/wayde sun 1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("wayde sun 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("wayde"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
    }
}
