package services;

import models.Person;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePerson implements CRUD<Person>{

    private Connection cnx ;

    public ServicePerson() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Person person) throws SQLException {
        String req = "INSERT INTO `person`(`nom`, `prenom`, `age`) VALUES " +
                "('"+person.getNom()+"','"+person.getPrenom()+"',"+person.getAge()+")";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("Person Added !");
    }

    public void insertOne1(Person person) throws SQLException {
        String req = "INSERT INTO `person`(`nom`, `prenom`, `age`) VALUES " +
                "(?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, person.getNom());
        ps.setString(2, person.getPrenom());
        ps.setInt(3, person.getAge());

        ps.executeUpdate(req);
    }

    @Override
    public void updateOne(Person person) throws SQLException {

    }

    @Override
    public void deleteOne(Person person) throws SQLException {

    }

    @Override
    public List<Person> selectAll() throws SQLException {
        List<Person> personList = new ArrayList<>();

        String req = "SELECT * FROM `person`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Person p = new Person();

            p.setId(rs.getInt(("id")));
            p.setNom(rs.getString((2)));
            p.setPrenom(rs.getString(("prenom")));
            p.setAge(rs.getInt((4)));

            personList.add(p);
        }

        return personList;
    }
}
