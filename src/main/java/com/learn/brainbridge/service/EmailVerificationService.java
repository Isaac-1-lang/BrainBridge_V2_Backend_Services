package com.learn.brainbridge.service;

import com.learn.brainbridge.entity.User;

public interface EmailVerificationService {

    String sendVerificationEmail(User user);

    void verifyOTP(String email, String otp);
    
    String resendOTP(String email);
}

