package org.challenge.twitterclone.service;

import lombok.RequiredArgsConstructor;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class WallService {

    private final UserService userService;

    public void addPostToUserWall(Post post) {
        User user = userService.getOrCreate(post.getOwner());
        user.addPost(post);
        userService.save(user);
    }

    public List<Post> getUserWall(String userName) {
        return userService.getUserPosts(userName)
                .stream()
                .sorted(comparing(Post::getTimeStamp).reversed())
                .collect(toList());
    }

}
