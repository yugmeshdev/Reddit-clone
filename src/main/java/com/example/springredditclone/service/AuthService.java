package com.example.springredditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springredditclone.dto.AuthenticationResponse;
import com.example.springredditclone.dto.LoginRequest;
import com.example.springredditclone.dto.RegisterRequest;
import com.example.springredditclone.exception.SpringRedditException;
import com.example.springredditclone.model.NotificationEmail;
import com.example.springredditclone.model.User;
import com.example.springredditclone.model.VerificationToken;
import com.example.springredditclone.repository.UserRepository;
//constructor over property/field injection:https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
import com.example.springredditclone.repository.VerificationTokenRepository;
import com.example.springredditclone.security.JwtProvider;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
//why use dependency injection in spring: this allows for loose coupling of components and moves the responsibility of managing components onto the container.
@Service
@AllArgsConstructor
public class AuthService {
	
	//@Autowired
	//constructor over field so autowired is commented ,@allargsconst for CI
	
	private  final PasswordEncoder passwordEncoder;
	//@Autowired
	//when the annotation is directly used on properties, Spring looks for and injects UserRepository when AuthService is created. This is how it eliminates the need for getters and setters.
	private final UserRepository userRepository;
	private final MailService mailService;
	private final VerificationTokenRepository verificationTokenRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	@Transactional
	//cause we interactiong with relational db
	public void signup(RegisterRequest registerRequest) {
		User user=new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		//password encoder 
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		userRepository.save(user);
		String token= generateVerificationToken(user);
		 mailService.sendMail(new NotificationEmail("Please Activate your Account",
	                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
	                "please click on the below url to activate your account : " +
	                "http://localhost:8080/api/auth/accountVerification/" + token));
	}
	private String generateVerificationToken(User user) {
		String token=UUID.randomUUID().toString();
		VerificationToken verificationToken=new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}
	public void verifyAccount(String token) {
		// TODO Auto-generated method stub
		Optional<VerificationToken> verificationToken=verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(()->new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationToken.get());
	}
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		String username=verificationToken.getUser().getUsername();
		User user=userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User not found with name  -"+username));
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token=jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token,loginRequest.getUsername());
	}
	 @Transactional(readOnly = true)
	    public User getCurrentUser() {
	        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        return userRepository.findByUsername(principal.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	    }

}
