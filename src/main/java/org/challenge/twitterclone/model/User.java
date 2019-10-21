package org.challenge.twitterclone.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("User")
public class User {

    private final String userName;
    private final List<Post> wall = new ArrayList<>();
    private final List<User> followed = new ArrayList<>();

    public User(String userName) {
        this.userName = userName;
    }

    public void addPost(Post post) {
        this.wall.add(post);
    }

    public void follow(User user) {
        followed.add(user);
    }
}
