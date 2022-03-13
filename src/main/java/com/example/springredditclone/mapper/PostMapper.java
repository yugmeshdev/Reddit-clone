package com.example.springredditclone.mapper;



import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.model.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.Optional;


@Mapper(componentModel = "spring")
public interface PostMapper {



    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
  

    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target ="id", source = "postId")
    @Mapping(target ="subredditName", source ="subreddit.name")
    @Mapping(target ="userName", source ="user.username")
  
    PostResponse mapToDto(Post post);

   

}