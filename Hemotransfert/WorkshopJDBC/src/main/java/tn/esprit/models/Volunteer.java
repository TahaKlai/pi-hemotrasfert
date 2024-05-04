package tn.esprit.models;

import javax.persistence.*;

@Entity
public class Volunteer {

    @Id
    private int id;
    private String name;
    private String address;
    private int phone;
    private String mail;
    private String dispo;
    private String profession;


    private int company_id;


    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Company company;


    public Volunteer(int id, String name, String address, int phone, String mail, String dispo, String profession, int company_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
        this.dispo = dispo;
        this.profession = profession;
        this.company_id = company_id;
    }

    public Volunteer() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDispo() {
        return dispo;
    }

    public void setDispo(String dispo) {
        this.dispo = dispo;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // toString method to represent the object as a string
    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone=" + phone +
                ", mail='" + mail + '\'' +
                ", dispo='" + dispo + '\'' +
                ", profession='" + profession + '\'' +
                ", company_id=" + company_id +
                '}';
    }
}
