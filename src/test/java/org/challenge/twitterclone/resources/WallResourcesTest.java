package org.challenge.twitterclone.resources;

import org.challenge.twitterclone.exception.UserNotFoundException;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.service.WallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WallResourcesTest {

    @Mock
    private WallService wallService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        WallResources wallResources = new WallResources(wallService);
        mockMvc = MockMvcBuilders.standaloneSetup(wallResources).build();
    }

    @Test
    void postMessageToUserWall_success() throws Exception {
        mockMvc.perform(post("/api/wall/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"test message\",\"owner\":\"testOwner\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserPosts_success() throws Exception {
        String testUser = "testUser";
        Post testMessage = new Post("testMessage", testUser, ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]"));

        when(wallService.getUserWall(testUser)).thenReturn(List.of(testMessage));

        mockMvc.perform(get("/api/wall/posts/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"message\":\"testMessage\",\"owner\":\"testUser\",\"timeStamp\":\"2019-10-18T18:58:54+02:00 Europe/Belgrade\"}]"));
    }

    @Test
    void getUserPosts_userNotFound() throws Exception {
        String testUser = "testUser";

        when(wallService.getUserWall(testUser))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/wall/posts/testUser"))
                .andExpect(status().isNotFound());
    }
}