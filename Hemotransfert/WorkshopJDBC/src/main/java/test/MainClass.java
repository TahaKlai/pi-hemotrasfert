package test;

import models.Person;
import services.ServicePerson;
import utils.DBConnection;

import java.sql.SQLException;

public class MainClass {

    public static void main(String[] args) {
        DBConnection cn1 = DBConnection.getInstance();

        Person p = new Person("Ben Daoued","Yosra", 22);

        ServicePerson sp = new ServicePerson();

        try {
            //sp.insertOne(p);
            System.out.println(sp.selectAll());
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }


    }
}
