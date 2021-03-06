package com.example.springredditclone.repository;
import com.example.springredditclone.model.*;
import com.example.springredditclone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	 List<Post> findAllBySubreddit(Subreddit subreddit);

	    List<Post> findByUser(User user);
}