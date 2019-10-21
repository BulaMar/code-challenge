package org.challenge.twitterclone.service;

import org.challenge.twitterclone.exception.UserNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WallServiceTest {

    private static final String TEST_OWNER = "testOwner";
    @Mock
    private UserService userService;

    private WallService wallService;

    @BeforeEach
    void setUp() {
        wallService = new WallService(userService);
    }

    @Test
    void addPostToWall_success() {
        User user = createUser();
        User userWithPost = createUserWithPost();

        when(userService.getOrCreate(TEST_OWNER))
                .thenReturn(user);
        wallService.addPostToUserWall(createPost());

        verify(userService).save(userWithPost);
    }

    @Test
    void getUserWall_userHasPost() {
        Post userPost = createPost();

        when(userService.getUserPosts(TEST_OWNER)).thenReturn(List.of(userPost));
        List<Post> userWall = wallService.getUserWall(TEST_OWNER);

        assertThat(userWall).hasSize(1);
        assertThat(userWall).containsExactly(userPost);
    }

    @Test
    void getUserWall_userHasNoPosts() {
        when(userService.getUserPosts(TEST_OWNER)).thenReturn(List.of());
        List<Post> userWall = wallService.getUserWall(TEST_OWNER);

        assertThat(userWall).isEmpty();
    }

    @Test
    void getUserWall_userNotFound() {
        String exceptionMessage = "User not found";

        when(userService.getUserPosts(TEST_OWNER)).thenThrow(new UserNotFoundException(exceptionMessage));

        assertThatThrownBy(() -> wallService.getUserWall(TEST_OWNER))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(exceptionMessage);
    }

    private User createUserWithPost() {
        User user = createUser();
        user.addPost(createPost());
        return user;
    }

    private User createUser() {
        return new User(TEST_OWNER);
    }

    private Post createPost() {
        return new Post("testMessage", TEST_OWNER,
                ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]"));
    }

}