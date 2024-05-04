package com.hemotransfert.hemotransfert;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DonData {
    private final SimpleStringProperty date;
    private final SimpleIntegerProperty dons;

    public DonData(String date, Integer dons) {
        this.date = new SimpleStringProperty(date);
        this.dons = new SimpleIntegerProperty(dons);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public Integer getDons() {
        return dons.get();
    }

    public void setDons(Integer dons) {
        this.dons.set(dons);
    }
}