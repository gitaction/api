package top.fsky.crawler.function.rpc;

import top.fsky.crawler.adapter.inbound.rpc.controller.UserController;
import top.fsky.crawler.application.service.UserBusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("function")
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBusinessService userBusinessService;

    @Test
    @WithMockUser
    public void given_unused_username_when_check_availability_then_return_ok() throws Exception {
        when(userBusinessService.isUsernameAvailable(any(String.class))).thenReturn(true);

        mockMvc.perform(get("/api/users/checkUsernameAvailability?username=wayde")
                .characterEncoding("UTF-8")
                .contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}

