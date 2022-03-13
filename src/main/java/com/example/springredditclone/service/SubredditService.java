package com.example.springredditclone.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import com.example.springredditclone.dto.SubredditDto;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.mapper.SubredditMapper;
import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
@Service
@AllArgsConstructor
@Slf4j



public class SubredditService {
	
	
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	//consistency 
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit save=subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}
	
	//builder pattern to construct subreddit entity
//	private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//		// TODO Auto-generated method stub
//		return Subreddit.builder().name(subredditDto.getName())
//				.description(subredditDto.getDescription())
//				.build();
//	}

	@Transactional(readOnly=true)
	public List<SubredditDto> getAll() {
		// TODO Auto-generated method stub
		return subredditRepository.findAll()
		.stream()
		.map(subredditMapper::mapSubredditToDto)
		.collect(toList());
	}

	public SubredditDto getSubreddit(Long id) {
		// TODO Auto-generated method stub
		Subreddit subreddit=subredditRepository.findById(id)
				.orElseThrow(()->new SpringRedditException("No subreddit found with id -"+id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
//	//mapping back to DTO 
//	private SubredditDto mapToDto(Subreddit subreddit) {
//		return SubredditDto.builder().name(subreddit.getName())
//							.id(subreddit.getId())
//							.numberOfPosts(subreddit.getPosts().size())
//							.build();
//	}
}
