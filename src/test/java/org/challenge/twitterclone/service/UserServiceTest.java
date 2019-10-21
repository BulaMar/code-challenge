package org.challenge.twitterclone.service;

import org.challenge.twitterclone.exception.SelfFollowException;
import org.challenge.twitterclone.exception.UserNotFoundException;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.model.User;
import org.challenge.twitterclone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_USER = "testUser";
    private static final ZonedDateTime TIMESTAMP = ZonedDateTime.parse("2019-10-18T18:58:54+02:00[Europe/Belgrade]");
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void save() {
        User user = new User(TEST_USER);
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void getUserPosts_userHasPosts() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(createUserWithPosts()));
        List<Post> posts = userService.getUserPosts(TEST_USER);

        assertThat(posts).hasSize(3);
        assertThat(posts).contains(new Post("firstMessage", TEST_USER, TIMESTAMP));
        assertThat(posts).contains(new Post("secondMessage", TEST_USER, TIMESTAMP));
        assertThat(posts).contains(new Post("thirdMessage", TEST_USER, TIMESTAMP));
    }

    @Test
    void getUserPosts_userHasNoPosts() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(createNewUser()));
        List<Post> posts = userService.getUserPosts(TEST_USER);

        assertThat(posts).hasSize(0);
    }

    @Test
    void getUserPosts_userNotFound() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserPosts(TEST_USER))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getOrCreate_userAlreadyExist() {
        User user = createNewUser();

        when(userRepository.findById(TEST_USER))
                .thenReturn(Optional.of(user));
        User testUser = userService.getOrCreate(TEST_USER);

        assertThat(testUser).isEqualTo(user);
        verify(userRepository, never()).save(user);
    }

    @Test
    void getOrCreate_userNotExist() {
        User user = createNewUser();

        when(userRepository.findById(TEST_USER))
                .thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User testUser = userService.getOrCreate(TEST_USER);

        assertThat(testUser).isEqualTo(user);
    }

    @Test
    void getAllFollowedBy_userFollowsTwoOtherUsers() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(createTwoUsersSubscriber()));

        List<User> followed = userService.getAllFollowedBy(TEST_USER);

        assertThat(followed).hasSize(2);
        assertThat(followed).contains(new User("Publisher one"));
        assertThat(followed).contains(new User("Publisher two"));
    }

    @Test
    void getAllFollowedBy_userDoNotFollowsOtherUsers() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(createNewUser()));

        List<User> followed = userService.getAllFollowedBy(TEST_USER);

        assertThat(followed).hasSize(0);
    }

    @Test
    void getAllFollowedBy_userNotFound() {
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getAllFollowedBy(TEST_USER))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void follow_success() {
        User following = createNewUser();
        User followed = new User("followed");
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(following));
        when(userRepository.findById("followed")).thenReturn(Optional.of(followed));

        userService.follow(TEST_USER, "followed");

        verify(userRepository).save(following);
    }

    @Test
    void follow_userNotFound() {
        User following = createNewUser();
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.follow(TEST_USER, "followed"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).save(following);
    }

    @Test
    void follow_userToFollowNotFound() {
        User following = createNewUser();
        when(userRepository.findById(TEST_USER)).thenReturn(Optional.of(following));
        when(userRepository.findById("followed")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.follow(TEST_USER, "followed"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User to follow not found");;

        verify(userRepository, never()).save(following);
    }

    @Test
    void follow_userTriesToFollowHimself() {
        User following = createNewUser();

        assertThatThrownBy(() -> userService.follow(TEST_USER, TEST_USER))
                .isInstanceOf(SelfFollowException.class)
                .hasMessage("Users are not able to follow themselves");;

        verify(userRepository, never()).save(following);
    }

    private User createUserWithPosts() {
        User testUser = createNewUser();
        testUser.addPost(new Post("firstMessage", TEST_USER, TIMESTAMP));
        testUser.addPost(new Post("secondMessage", TEST_USER, TIMESTAMP));
        testUser.addPost(new Post("thirdMessage", TEST_USER, TIMESTAMP));
        return testUser;
    }

    private User createTwoUsersSubscriber() {
        User user = createNewUser();
        user.follow(new User("Publisher one"));
        user.follow(new User("Publisher two"));
        return user;
    }

    private User createNewUser() {
        return new User(TEST_USER);
    }

}