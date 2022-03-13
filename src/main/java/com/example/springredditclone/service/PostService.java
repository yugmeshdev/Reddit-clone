package com.example.springredditclone.service;



import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springredditclone.dto.PostRequest;
import com.example.springredditclone.dto.PostResponse;
import com.example.springredditclone.exception.PostNotFoundException;
import com.example.springredditclone.exception.SubredditNotFoundException;
import com.example.springredditclone.mapper.PostMapper;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.model.User;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.SubredditRepository;
import com.example.springredditclone.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostMapper postMapper; 
	 private final UserRepository userRepository;
	private final PostRepository postRepository;
	 public void save(PostRequest postRequest) {
	        Subreddit subreddit =subredditRepository.findByName(postRequest.getSubredditName())
	                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
	        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
	    }

	    @Transactional(readOnly = true)
	    public PostResponse getPost(Long id) {
	        Post post = postRepository.findById(id)
	                .orElseThrow(() -> new PostNotFoundException(id.toString()));
	        return postMapper.mapToDto(post);
	    }

	    @Transactional(readOnly = true)
	    public List<PostResponse> getAllPosts() {
	        return postRepository.findAll()
	                .stream()
	                .map(postMapper::mapToDto)
	                .collect(toList());
	    }

	    @Transactional(readOnly = true)
	    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
	        Subreddit subreddit = subredditRepository.findById(subredditId)
	                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
	        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
	        return posts.stream().map(postMapper::mapToDto).collect(toList());
	    }

	    @Transactional(readOnly = true)
	    public List<PostResponse> getPostsByUsername(String username) {
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException(username));
	        return postRepository.findByUser(user)
	                .stream()
	                .map(postMapper::mapToDto)
	                .collect(toList());
	    }
    
}
