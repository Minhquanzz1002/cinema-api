package vn.edu.iuh.services;

public interface OTPService {
    void saveOTP(String email, String otp);
    boolean validateOTP(String email, String otp);
}
