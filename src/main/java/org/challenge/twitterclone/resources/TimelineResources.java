package org.challenge.twitterclone.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.service.TimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timeline")
public class TimelineResources {

    private final TimelineService timelineService;

    @GetMapping("/{userName}")
    @ApiOperation("Get posts of followed users by given user.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully get all posts of followed users by given user."),
            @ApiResponse(code = 404, message = "User following or follower not found.")
    })
    public ResponseEntity<List<Post>> getFollowedPosts(@PathVariable @ApiParam("Following user") String userName) {
        List<Post> followedPosts = timelineService.getFollowedPosts(userName);
        return ResponseEntity.ok(followedPosts);
    }
}
