package vn.edu.iuh.services;

public interface EmailService {
    void sendEmail(String to, String subject, String verificationLink);
}
