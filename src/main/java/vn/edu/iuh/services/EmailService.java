package vn.edu.iuh.services;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationLink);
}
