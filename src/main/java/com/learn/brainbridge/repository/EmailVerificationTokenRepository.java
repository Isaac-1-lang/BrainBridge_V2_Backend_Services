package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.EmailVerificationToken;
import com.learn.brainbridge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUserAndToken(User user, String token);

    void deleteByUser(User user);
}

