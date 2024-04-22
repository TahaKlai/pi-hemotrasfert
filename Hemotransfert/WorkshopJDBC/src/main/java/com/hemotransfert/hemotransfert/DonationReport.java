package com.hemotransfert.hemotransfert;

import java.time.LocalDate;

public class DonationReport {
    private LocalDate date;
    private int donationCount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDonationCount() {
        return donationCount;
    }

    public void setDonationCount(int donationCount) {
        this.donationCount = donationCount;
    }
}