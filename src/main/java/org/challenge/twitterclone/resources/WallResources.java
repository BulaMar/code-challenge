package org.challenge.twitterclone.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.challenge.twitterclone.model.Post;
import org.challenge.twitterclone.service.WallService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/wall/")
@RequiredArgsConstructor
public class WallResources {

    private final WallService wallService;

    @ApiOperation("Create post in user wall.")
    @ApiResponses({
            @ApiResponse(code = 200 , message = "Successfully created post on the wall.")
    })
    @PostMapping(value = "/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postMessageToUserWall(@RequestBody @ApiParam("Post") Post post) {
        wallService.addPostToUserWall(post);
    }

    @ApiOperation("Get user posts from his wall.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully get user wall"),
            @ApiResponse(code = 404, message = "User was not found.")
    })
    @GetMapping("/posts/{userName}")
    public ResponseEntity<List<Post>> getUserWall(@PathVariable("userName") String userName) {
        return ok(wallService.getUserWall(userName));
    }
}
