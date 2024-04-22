package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Livreur;
import models.Materiel;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceLivreur implements CRUD<Livreur> {

    private Connection cnx;

    public ServiceLivreur() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Livreur livreur) throws SQLException {
        String req = "INSERT INTO `livreur`(`cin`, `nom`, `prenom`, `tel`, `email`,`statut_l`) VALUES " +
                "('" + livreur.getCin() + "','" + livreur.getNom() + "','" + livreur.getPrenom() + "','" + livreur.getTel() + "','" + livreur.getEmail() + "'," + livreur.getStatut_l() + ")";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("Livreur Added !");
    }

    public void insertOne1(Livreur livreur) throws SQLException {
        String req = "INSERT INTO `livreur`(`cin`, `nom`, `prenom`, `tel`, `email`,`statut_l`, `zone_liv`) VALUES " +
                "(?,?,?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, livreur.getCin());
        ps.setString(2, livreur.getNom());
        ps.setString(3, livreur.getPrenom());
        ps.setInt(4, livreur.getTel());
        ps.setString(5, livreur.getEmail());
        ps.setString(6, livreur.getStatut_l());
        ps.setString(7, livreur.getZone_liv());
        ps.executeUpdate();
    }

    @Override
    public void updateOne(Livreur livreur) throws SQLException {
        String req = "UPDATE `livreur` SET `cin`=?, `nom`=?, `prenom`=?, `tel`=?, `email`=?, `statut_l`=?, `zone_liv`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, livreur.getCin());
        ps.setString(2, livreur.getNom());
        ps.setString(3, livreur.getPrenom());
        ps.setInt(4, livreur.getTel());
        ps.setString(5, livreur.getEmail());
        ps.setString(6, livreur.getStatut_l());
        ps.setString(7, livreur.getZone_liv());
        ps.setInt(8, livreur.getId());

        ps.executeUpdate();
        System.out.println("Livreur Updated !");

    }

    @Override
    public void deleteOne(Livreur livreur) throws SQLException {
        String req = "DELETE FROM `livreur` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, livreur.getId());

        ps.executeUpdate();
        System.out.println("Livreur Deleted !");

    }

    @Override
    public List<Livreur> selectAll() throws SQLException {
        //List<Livreur> livreurList = new ArrayList<>();
        ObservableList<Livreur> livreurList =  FXCollections.observableArrayList();
        String req = "SELECT * FROM `livreur`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Livreur p = new Livreur();

            p.setId(rs.getInt(("id")));
            p.setCin(rs.getString(("cin")));
            p.setNom(rs.getString(("nom")));
            p.setPrenom(rs.getString(("prenom")));
            p.setEmail(rs.getString(("email")));
            p.setTel(rs.getInt(("tel")));
            p.setStatut_l(rs.getString(("statut_l")));
            p.setZone_liv(rs.getString(("zone_liv")));

            livreurList.add(p);
        }

        return livreurList;
    }

    public Livreur selectOne(int id) throws SQLException {
        Livreur p = new Livreur();

        String req = "SELECT * FROM `livreur` where id = "+id;
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {

            p.setId(rs.getInt(("id")));
            p.setCin(rs.getString(("cin")));
            p.setNom(rs.getString(("nom")));
            p.setPrenom(rs.getString(("prenom")));
            p.setEmail(rs.getString(("email")));
            p.setTel(rs.getInt(("tel")));
            p.setStatut_l(rs.getString(("statut_l")));
            p.setZone_liv(rs.getString(("zone_liv")));

        }

        return p;
    }
}



