package org.challenge.twitterclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.challenge.twitterclone.exception.SelfFollowException;
import org.challenge.twitterclone.exception.UserNotFoundException;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.model.User;
import org.challenge.twitterclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    void save(User user) {
        userRepository.save(user);
    }

    List<Post> getUserPosts(String userName) {
        return userRepository.findById(userName)
                .map(User::getWall)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    List<User> getAllFollowedBy(String subscriber) {
        return userRepository.findById(subscriber)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                .getFollowed();
    }

    User getOrCreate(String owner) {
        return userRepository.findById(owner)
                .orElseGet(() -> userRepository.save(new User(owner)));
    }

    public void follow(String userName, String userNameToFollow) {
        if (userName.equals(userNameToFollow)) {
            throw new SelfFollowException("Users are not able to follow themselves");
        }

        User user = userRepository.findById(userName)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        User userToFollow = userRepository.findById(userNameToFollow)
                .orElseThrow(() -> new UserNotFoundException("User to follow not found"));

        user.follow(userToFollow);
        save(user);
    }
}
