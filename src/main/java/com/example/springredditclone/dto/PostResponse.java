package com.example.springredditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PostResponse {
	private Long id;
	private String PostName;
	private String url;
	private String description;
	private String userName;
	private String subredditName;
}