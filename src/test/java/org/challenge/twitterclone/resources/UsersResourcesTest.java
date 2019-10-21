package org.challenge.twitterclone.resources;

import org.challenge.twitterclone.exception.UserNotFoundException;
import org.challenge.twitterclone.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsersResourcesTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UsersResources usersResources = new UsersResources(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(usersResources).build();
    }

    @Test
    void follow_success() throws Exception {
        mockMvc.perform(post("/api/users/follower/follow/followed"))
                .andExpect(status().isOk());

        verify(userService).follow("follower", "followed");
    }

    @Test
    void follow_followerNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found"))
                .when(userService)
                .follow("follower", "followed");

        String message = mockMvc.perform(post("/api/users/follower/follow/followed"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException()
                .getMessage();

        assertThat(message).isEqualTo("User not found");
    }

    @Test
    void follow_followerTriesToFollowHimself() throws Exception {
        doCallRealMethod().when(userService).follow("follower", "follower");

        String message = mockMvc.perform(post("/api/users/follower/follow/follower"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResolvedException()
                .getMessage();

        assertThat(message).isEqualTo("Users are not able to follow themselves");
    }
}