package org.challenge.twitterclone.service;

import lombok.RequiredArgsConstructor;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final UserService userService;

    public List<Post> getFollowedPosts(String userName) {
        return userService.getAllFollowedBy(userName)
                .stream()
                .map(User::getWall)
                .flatMap(Collection::stream)
                .sorted(comparing(Post::getTimeStamp).reversed())
                .collect(Collectors.toList());
    }
}
