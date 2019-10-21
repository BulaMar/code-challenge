package org.challenge.twitterclone.resources;

import org.challenge.twitterclone.exception.UserNotFoundException;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.service.TimelineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TimelineResourcesTest {

    @Mock
    private TimelineService timelineService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TimelineResources timelineResources = new TimelineResources(timelineService);
        mockMvc = MockMvcBuilders.standaloneSetup(timelineResources).build();
    }

    @Test
    void getFollowedPosts_followedUserHasOnePost() throws Exception {
        when(timelineService.getFollowedPosts("testUser")).thenReturn(List.of(createPost()));

        mockMvc.perform(get("/api/timeline/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"message\":\"testMessage\",\"owner\":\"testUser\",\"timeStamp\":\"2019-10-18T18:58:54+02:00 Europe/Belgrade\"}]"));
    }

    @Test
    void getFollowedPosts_userDoNotFollowAnyone() throws Exception {
        when(timelineService.getFollowedPosts("testUser")).thenReturn(List.of());

        mockMvc.perform(get("/api/timeline/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getFollowedPosts_userNotFound() throws Exception {
        String exceptionMessage = "User not found";
        when(timelineService.getFollowedPosts("testUser")).thenThrow(new UserNotFoundException(exceptionMessage));

        String message = mockMvc.perform(get("/api/timeline/testUser"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException()
                .getMessage();

        assertThat(message).isEqualTo(exceptionMessage);
    }

    private Post createPost() {
        return new Post("testMessage", "testUser",
                ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]"));
    }
}