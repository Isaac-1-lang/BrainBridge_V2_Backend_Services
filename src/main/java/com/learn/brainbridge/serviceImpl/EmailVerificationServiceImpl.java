package com.learn.brainbridge.serviceImpl;

import com.learn.brainbridge.Exception.BadRequestException;
import com.learn.brainbridge.Exception.ResourceNotFoundException;
import com.learn.brainbridge.entity.EmailVerificationToken;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.EmailVerificationTokenRepository;
import com.learn.brainbridge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationServiceImpl implements com.learn.brainbridge.service.EmailVerificationService {

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Generate a random 6-digit OTP code
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates 6-digit number
        return String.valueOf(otp);
    }

    @Override
    @Transactional
    public String sendVerificationEmail(User user) {
        // Remove existing tokens for this user
        tokenRepository.deleteByUser(user);

        String otp = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10); // OTP expires in 10 minutes

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(otp);
        verificationToken.setExpiresAt(expiresAt);
        verificationToken.setUsed(false);
        tokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify your BrainBridge account");
        message.setText("Hi " + (user.getFirstName() != null ? user.getFirstName() : user.getUsername()) + ",\n\n"
                + "Your verification code is: " + otp + "\n\n"
                + "This code will expire in 10 minutes.\n\n"
                + "If you did not create an account, please ignore this email.\n\n"
                + "BrainBridge Team");

        mailSender.send(message);
        return otp; // Return for testing purposes (remove in production)
    }

    @Override
    @Transactional
    public void verifyOTP(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        EmailVerificationToken verificationToken = tokenRepository.findByUserAndToken(user, otp)
                .orElseThrow(() -> new BadRequestException("Invalid verification code"));

        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
            throw new BadRequestException("Verification code has already been used");
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification code has expired");
        }

        user.setIsEmailVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public String resendOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (Boolean.TRUE.equals(user.getIsEmailVerified())) {
            throw new BadRequestException("Email is already verified");
        }

        return sendVerificationEmail(user);
    }
}

