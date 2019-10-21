package org.challenge.twitterclone.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.challenge.twitterclone.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UsersResources {

    private final UserService userService;

    @ApiOperation("Make one user follow another")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully make user follow another user."),
            @ApiResponse(code = 404, message = "One of the user were not found."),
            @ApiResponse(code = 400, message = "User tries to follow himself.")
    })
    @PostMapping("/{userName}/follow/{userToFollow}")
    public void follow(@PathVariable @ApiParam("Following user") String userName,
                       @PathVariable @ApiParam("Followed user") String userToFollow) {
        userService.follow(userName, userToFollow);
    }
}

