package com.example.springredditclone.repository;
import com.example.springredditclone.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {



}
