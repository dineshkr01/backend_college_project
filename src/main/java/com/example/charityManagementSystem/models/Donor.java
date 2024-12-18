package com.example.charityManagementSystem.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "donor_table")
public class Donor {

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


    public Donor(Long id, String username, String email, String password, 
    Double donatedAmt, Long donatedOrgId, Boolean donationStatus,
     String emailVerificationToken, boolean emailVerificationStatus,
      String passwordResetToken, boolean passwordResetStatus) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerificationStatus = emailVerificationStatus;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetStatus = passwordResetStatus;
    }



    public Donor() {
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



    public LocalDateTime getExpriyDateTime() {
        return this.expriyDateTime;
    }

    public void setExpriyDateTime(LocalDateTime expriyDateTime) {
        this.expriyDateTime = expriyDateTime;
    }



    public LocalDateTime getPassexpriyDateTime() {
        return this.passexpriyDateTime;
    }

    public void setPassexpriyDateTime(LocalDateTime passexpriyDateTime) {
        this.passexpriyDateTime = passexpriyDateTime;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", emailVerificationToken='" + getEmailVerificationToken() + "'" +
            ", emailVerificationStatus='" + isEmailVerificationStatus() + "'" +
            ", passwordResetToken='" + getPasswordResetToken() + "'" +
            ", passwordResetStatus='" + isPasswordResetStatus() + "'" +
            "}";
    }
    
}
