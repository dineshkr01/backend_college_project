package com.example.charityManagementSystem.rough_codes;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "password_verification_token")
public class PasswordVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_type", nullable = false)
    private String userType; // "admin", "donor", or "needy"

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID of the user (admin, donor, or needy)

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // Default constructor
    public PasswordVerificationToken() {}

    // Constructor with necessary fields
    public PasswordVerificationToken(String token, String userType, Long userId, LocalDateTime expiryDate) {
        this.token = token;
        this.userType = userType;
        this.userId = userId;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
