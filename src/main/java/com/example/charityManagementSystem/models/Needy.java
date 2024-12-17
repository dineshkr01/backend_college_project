package com.example.charityManagementSystem.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "needy_table")
public class Needy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "collected_donation")
    private Double collectedDonation;
    
    @Column(name = "account_verified_status")
    private Boolean accountVerifiedStatus;
    
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_verification_status")
    private boolean emailVerificationStatus;

    @Column(name = "email_expiry_date")
    private LocalDateTime expriyDateTime;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_status")
    private boolean passwordResetStatus;

    @Column(name = "pass_expiry_date")
    private LocalDateTime passexpriyDateTime;



    public Needy() {
    }


    public Needy(Long id, String username, String email,
     String password, Double collectedDonation, Boolean accountVerifiedStatus,
      String emailVerificationToken, boolean emailVerificationStatus, LocalDateTime expriyDateTime, 
      String passwordResetToken, boolean passwordResetStatus, LocalDateTime passexpriyDateTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.collectedDonation = collectedDonation;
        this.accountVerifiedStatus = accountVerifiedStatus;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationStatus = emailVerificationStatus;
        this.expriyDateTime = expriyDateTime;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetStatus = passwordResetStatus;
        this.passexpriyDateTime = passexpriyDateTime;
    }
    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getCollectedDonation() {
        return this.collectedDonation;
    }

    public void setCollectedDonation(Double collectedDonation) {
        this.collectedDonation = collectedDonation;
    }

    public Boolean isAccountVerifiedStatus() {
        return this.accountVerifiedStatus;
    }

    public Boolean getAccountVerifiedStatus() {
        return this.accountVerifiedStatus;
    }

    public void setAccountVerifiedStatus(Boolean accountVerifiedStatus) {
        this.accountVerifiedStatus = accountVerifiedStatus;
    }

    public String getEmailVerificationToken() {
        return this.emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public boolean isEmailVerificationStatus() {
        return this.emailVerificationStatus;
    }

    public boolean getEmailVerificationStatus() {
        return this.emailVerificationStatus;
    }

    public void setEmailVerificationStatus(boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public LocalDateTime getExpriyDateTime() {
        return this.expriyDateTime;
    }

    public void setExpriyDateTime(LocalDateTime expriyDateTime) {
        this.expriyDateTime = expriyDateTime;
    }

    public String getPasswordResetToken() {
        return this.passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public boolean isPasswordResetStatus() {
        return this.passwordResetStatus;
    }

    public boolean getPasswordResetStatus() {
        return this.passwordResetStatus;
    }

    public void setPasswordResetStatus(boolean passwordResetStatus) {
        this.passwordResetStatus = passwordResetStatus;
    }

    public LocalDateTime getPassexpriyDateTime() {
        return this.passexpriyDateTime;
    }

    public void setPassexpriyDateTime(LocalDateTime passexpriyDateTime) {
        this.passexpriyDateTime = passexpriyDateTime;
    }
    
}
