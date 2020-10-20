package ru.heikkz.jp.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
//@WebMvcTest(UserController.class)
public class TokenAuthenticationServiceTest extends AbstractRestControllerTest {

    @Test
    @WithAnonymousUser
    public void anonymous() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user")).andExpect(status().isForbidden());
    }

    /**
     * The question is "How could we most easily run the test as a specific user?" The answer is to use @WithMockUser.
     * The following test will be run as a user with the username "user", the password "password", and the roles "ROLE_USER".
     */
    @WithMockUser
    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user")).andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username="admin",roles={"USER","ADMIN"})
//    public void getMessageWithMockUserCustomUser() {
//
//    }
}
