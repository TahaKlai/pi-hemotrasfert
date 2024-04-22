package tn.esprit.services;
import tn.esprit.interfaces.IActualite;
import tn.esprit.models.Actualite;
import tn.esprit.utils.DataSource;
import java.util.Date;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;


public class ServiceActualite implements IActualite<Actualite> {
    private Connection cnx;

    public ServiceActualite() {
        cnx = DataSource.getInstance().getCnx();
    }
    @Override
    public void Add(Actualite actualite) {
        String qry = "INSERT INTO `actualite`(`titre`, `date`,`priorite`, `categorie`) VALUES (?,?,?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, actualite.getTitre());
            stm.setDate(2, actualite.getDate());
            stm.setString(3, actualite.getPriorite());
            stm.setString(4, actualite.getCategorie());



            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Actualite> getAll() {
        return null;
    }

    @Override
    public List<Actualite> afficher() {
        List<Actualite> actualites  = new ArrayList<>();
        String sql = "SELECT `id`,`titre`, `date`,`priorite`, `categorie` FROM `actualite`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Actualite actualite = new Actualite();
                actualite.setId(rs.getInt("id"));
                actualite.setDate(rs.getDate("date"));
                actualite.setPriorite(rs.getString("priorite"));
                actualite.setCategorie(rs.getString("categorie"));

                actualites.add(actualite);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return actualites;
    }
    @Override
    public List<Actualite> TriparNom() {
        List<Actualite> actualites = new ArrayList<>();
        String sql = "SELECT  `id`,`titre`, `date`,`priorite`, `categorie` FROM `actualite` ORDER BY `titre`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {

                Actualite actualite = new Actualite();
                actualite.setId(rs.getInt("id"));
                actualite.setTitre(rs.getString("titre"));
                actualite.setDate(rs.getDate("date"));
                actualite.setPriorite(rs.getString("priorite"));
                actualite.setCategorie(rs.getString("categorie"));

                actualites.add(actualite);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return actualites;
    }

    @Override
    public List<Actualite> TriparEmail() {
        return null;
    }
    @Override
    public List<Actualite> Rechreche(String recherche) {
        List<Actualite> actualites = new ArrayList<>();
        String sql = "SELECT  `id`,`titre`, `date`,`priorite`, `categorie` FROM `actualite` WHERE `titre` LIKE '%" + recherche + "%' OR `date`LIKE '%" + recherche + "%'";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {

                Actualite actualite = new Actualite();
                actualite.setId(rs.getInt("id"));
                actualite.setTitre(rs.getString("titre"));
                actualite.setDate(rs.getDate("date"));
                actualite.setPriorite(rs.getString("priorite"));
                actualite.setCategorie(rs.getString("categorie"));

                actualites.add(actualite);


            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return actualites;
    }

    @Override
    public void Update(Actualite actualite) {
        try {
            String qry = "UPDATE `actualite` SET `Date`=?, `Priorite`=?, `categorie`=? WHERE `titre`=?";
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setDate(1, actualite.getDate());
            stm.setString(2, actualite.getPriorite());
            stm.setString(3, actualite.getCategorie());
            stm.setString(4, actualite.getTitre());

            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public void Delete(Actualite actualite) {
        try {
            String qry = "DELETE FROM `actualite` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, actualite.getId());
            smt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void DeleteByID(int id) {
        try {
            String qry = "DELETE FROM `actualite` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, id);
            smt.executeUpdate();
            System.out.println("Suppression Effectué");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
