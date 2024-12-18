package com.example.charityManagementSystem.models;

import jakarta.persistence.*;




@Entity
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "donor_id", nullable = false)
    private Long donorId; 

    @Column(name = "donated_org_id", nullable = false)
    private Long donatedOrgId; 

    @Column(name = "donated_amount", nullable = false)
    private Double donatedAmount; 

    public Donation() {
    }

    public Donation(Long donorId, Long donatedOrgId, Double donatedAmount) {
        this.donorId = donorId;
        this.donatedOrgId = donatedOrgId;
        this.donatedAmount = donatedAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Long getDonatedOrgId() {
        return donatedOrgId;
    }

    public void setDonatedOrgId(Long donatedOrgId) {
        this.donatedOrgId = donatedOrgId;
    }

    public Double getDonatedAmount() {
        return donatedAmount;
    }

    public void setDonatedAmount(Double donatedAmount) {
        this.donatedAmount = donatedAmount;
    }
}