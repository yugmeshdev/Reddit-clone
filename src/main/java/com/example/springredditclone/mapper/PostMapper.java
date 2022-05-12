package com.example.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.CommentRepository;
import com.example.springredditclone.repository.VoteRepository;
import com.example.springredditclone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import static com.example.springredditclone.model.VoteType.UPVOTE;

import static  com.example.springredditclone.model.VoteType.DOWNVOTE;
//import com.example.springredditclone.dto.PostRequest;
//import com.example.springredditclone.dto.PostResponse;
//import com.example.springredditclone.model.Post;
//import com.example.springredditclone.model.Subreddit;
//import com.example.springredditclone.model.User;
//import com.example.springredditclone.model.Vote;
//import com.example.springredditclone.model.VoteType;
//import com.example.springredditclone.repository.CommentRepository;
//import com.example.springredditclone.repository.VoteRepository;
//import com.example.springredditclone.service.AuthService;
//import com.github.marlonlom.utilities.timeago.TimeAgo;

//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;


@Mapper(componentModel = "spring")
public abstract class PostMapper {
	@Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount",constant="0")
    @Mapping(target = "user", source = "user")
  

    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
 
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

   

}