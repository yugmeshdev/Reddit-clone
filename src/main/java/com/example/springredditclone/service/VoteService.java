package com.example.springredditclone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springredditclone.dto.VoteDto;
import com.example.springredditclone.exception.PostNotFoundException;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.model.Post;
import com.example.springredditclone.model.Vote;
import com.example.springredditclone.repository.PostRepository;
import com.example.springredditclone.repository.UserRepository;
import com.example.springredditclone.repository.VoteRepository;
import com.google.common.base.Optional;
import static com.example.springredditclone.model.VoteType.UPVOTE;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	@Transactional
	public void vote(VoteDto voteDto) {
		// TODO Auto-generated method stub
		  Post post = postRepository.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));			
		  java.util.Optional<Vote> voteByPostAndUser=voteRepository.findTopByPostAndUserOrderByVoteDesc(post,authService.getCurrentUser());
		  if (voteByPostAndUser.isPresent() &&
	                voteByPostAndUser.get().getVoteType()
	                        .equals(voteDto.getVoteType())) {
	            throw new SpringRedditException("You have already "
	                    + voteDto.getVoteType() + "'d for this post");
	        }
		  if(UPVOTE.equals(voteDto.getVoteType())) {
			  post.setVoteCount(post.getVoteCount()+1);
		  }
		  else {
			  post.setVoteCount(post.getVoteCount()-1);
		  }
	}

}
