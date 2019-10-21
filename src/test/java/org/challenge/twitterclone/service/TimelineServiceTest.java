package org.challenge.twitterclone.service;

import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    private static final String TEST_USER = "testUser";
    @Mock
    private UserService userService;

    private TimelineService timelineService;

    @BeforeEach
    void setUp() {
        timelineService = new TimelineService(userService);
    }

    @Test
    void getFollowedPosts_followedUserHasOnePost() {
        User followed = createFollowed();
        when(userService.getAllFollowedBy(TEST_USER))
                .thenReturn(List.of(followed));

        List<Post> followedPosts = timelineService.getFollowedPosts(TEST_USER);

        assertThat(followedPosts).hasSize(1);
        assertThat(followedPosts).containsExactly(new Post("Followed post", followed.getUserName(),
                ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]")));
    }

    @Test
    void getFollowedPosts_userNotFound() {
        User followed = createFollowed();
        when(userService.getAllFollowedBy(TEST_USER))
                .thenReturn(List.of(followed));

        List<Post> followedPosts = timelineService.getFollowedPosts(TEST_USER);

        assertThat(followedPosts).hasSize(1);
        assertThat(followedPosts).containsExactly(new Post("Followed post", followed.getUserName(),
                ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]")));
    }

    private User createFollowed() {
        String followedUser = "followedUser";
        User user = new User(followedUser);
        user.addPost(new Post("Followed post", followedUser,
                ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]")));
        return user;

    }
}