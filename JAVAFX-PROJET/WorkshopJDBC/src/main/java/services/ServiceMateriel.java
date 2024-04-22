package services;

import javafx.collections.FXCollections;
import models.Materiel;
import utils.DBConnection;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;

public class ServiceMateriel implements CRUD<Materiel>{

    private Connection cnx ;

    public ServiceMateriel() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Materiel materiel) throws SQLException {
        String req = "INSERT INTO `materiel`(`type_m`, `description_m`, `quantite_m`, `dateexp_m`, `statut_m`,`livreur_id`) VALUES " +
                "('"+materiel.getType_m()+"','"+materiel.getDescription_m()+"','"+materiel.getQuantite_m()+"','"+materiel.getDateexp_m()+"','"+materiel.getStatut_m()+"'," +materiel.getLivreur_id()+")";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("Materiel Added !");
    }

    public void insertOne1(Materiel materiel) throws SQLException {
        String req = "INSERT INTO `materiel`(`type_m`, `description_m`, `quantite_m`, `dateexp_m`, `statut_m`,`livreur_id`) VALUES " +
                "(?,?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, materiel.getType_m());
        ps.setString(2, materiel.getDescription_m());
        ps.setInt(3, materiel.getQuantite_m());
        ps.setDate(4, materiel.getDateexp_m());
        ps.setString(5, materiel.getStatut_m());
        ps.setInt(6, materiel.getLivreur_id());
        ps.executeUpdate();
        System.out.println("Materiel created !");
    }

    @Override
    public void updateOne(Materiel materiel) throws SQLException {
        String req = "UPDATE `materiel` SET `type_m`=?, `description_m`=?, `quantite_m`=?, `dateexp_m`=?, `statut_m`=?, `livreur_id`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, materiel.getType_m());
        ps.setString(2, materiel.getDescription_m());
        ps.setInt(3, materiel.getQuantite_m());
        ps.setDate(4, materiel.getDateexp_m());
        ps.setString(5, materiel.getStatut_m());
        ps.setInt(6, materiel.getLivreur_id());
        ps.setInt(7, materiel.getId());

        ps.executeUpdate();
        System.out.println("Materiel Updated !");

    }

    @Override
    public void deleteOne(Materiel materiel) throws SQLException {
        String req = "DELETE FROM `materiel` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, materiel.getId());

        ps.executeUpdate();
        System.out.println("Materiel Deleted !");

    }

    @Override
    public List<Materiel> selectAll() throws SQLException {
//        List<Materiel> materielList = new ArrayList<>();
        ObservableList<Materiel> materielList =  FXCollections.observableArrayList();
        String req = "SELECT * FROM `materiel`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Materiel p = new Materiel();

            p.setId(rs.getInt(("id")));
            p.setType_m(rs.getString(("type_m")));
            p.setDescription_m(rs.getString(("description_m")));
            p.setQuantite_m(rs.getInt(("quantite_m")));
            p.setStatut_m(rs.getString(("statut_m")));
            p.setDateexp_m(rs.getDate(("dateexp_m")));
           p.setLivreur_id(rs.getInt(("livreur_id")));

            materielList.add(p);
        }

        return materielList;
    }
}

