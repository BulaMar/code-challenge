package org.challenge.twitterclone.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@Data
@ApiModel("Post")
@AllArgsConstructor
public class Post {

    @Length(max = 140)
    private String message;
    private String owner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX[ VV]")
    private ZonedDateTime timeStamp;

    @JsonCreator
    public static Post createNewPost(@JsonProperty("message") String message,
                                     @JsonProperty("owner") String owner) {
        return new Post(message, owner, ZonedDateTime.now());
    }
}
