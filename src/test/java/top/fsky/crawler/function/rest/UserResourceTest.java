package top.fsky.crawler.function.rest;

import top.fsky.crawler.adapter.inbound.rest.UserResource;
import top.fsky.crawler.application.model.User;
import top.fsky.crawler.application.model.UserProfile;
import top.fsky.crawler.application.service.UserBusinessService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("function")
@WebMvcTest(value = UserResource.class)
public class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBusinessService userBusinessService;

    @Test
    @WithMockUser
    public void given_user_when_find_by_username_return_found() throws Exception {
        User testUser = User.builder()
                .id(1l).name("wayde").username("wayde sun")
                .email("wayde.sun@gmail.com").password("987123")
                .build();
        UserProfile userProfile = new UserProfile(testUser.getId(), testUser.getUsername(),
                testUser.getName(), testUser.getCreatedAt());
        when(userBusinessService.getUserProfileByUsername(ArgumentMatchers.any(String.class))).thenReturn(userProfile);

        mockMvc.perform(get("/api/users/wayde sun")
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}

