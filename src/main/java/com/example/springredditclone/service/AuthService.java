package com.example.springredditclone.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.model.User;

@Service
public class AuthService {
	public void signup(RegisterRequest registerRequest) {
		User user=new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(registerRequest.getPassword());
		user.setCreated(Instant.now());
		user.setEnabled(false);
	}
}
