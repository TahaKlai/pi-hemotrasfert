package tn.esprit.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company {

    @Id
    private int id;
    private String name;
    private String country;
    private String website;
    private String mail;
    private int phone;
    private String address;
    private String description;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Volunteer> volunteers;

    public Company(int id, String name, String country, String website, String mail, int phone, String address, String description) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.website = website;
        this.mail = mail;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }

    public Company() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", website='" + website + '\'' +
                ", mail='" + mail + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
