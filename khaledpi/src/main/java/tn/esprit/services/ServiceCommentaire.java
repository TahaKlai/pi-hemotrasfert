package tn.esprit.services;



import tn.esprit.interfaces.ICommentaire;
import tn.esprit.models.Commentaire;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ServiceCommentaire implements ICommentaire<Commentaire> {
    private Connection cnx;

    public ServiceCommentaire() {
        cnx = DataSource.getInstance().getCnx();
    }


    @Override
    public void Add(Commentaire commentaire) {
        String qry = "INSERT INTO `commentaire`(`theme`, `description`) VALUES (?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, commentaire.getTheme());
            stm.setString(2, commentaire.getDescription());



            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Commentaire> getAll() {
        return null;
    }

    @Override
    public List<Commentaire> afficher() {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT `id`,`theme`, `description` FROM `commentaire`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setTheme(rs.getString("theme"));
                commentaire.setDescription(rs.getString("description"));

                commentaires.add(commentaire);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return commentaires;
    }
    @Override
    public List<Commentaire> TriparNom() {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT `id`,`theme`, `description` FROM `commentaire` ORDER BY `theme`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {

                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setTheme(rs.getString("theme"));
                commentaire.setDescription(rs.getString("description"));

                commentaires.add(commentaire);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return commentaires;
    }

    @Override
    public List<Commentaire> TriparEmail() {
        return null;
    }
    @Override
    public List<Commentaire> Rechreche(String recherche) {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT `id`,`theme`, `description` FROM `commentaire` WHERE `theme` LIKE '%" + recherche + "%' OR `description`LIKE '%" + recherche + "%'";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {

                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setTheme(rs.getString("theme"));
                commentaire.setDescription(rs.getString("description"));

                commentaires.add(commentaire);


            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return commentaires;
    }

    @Override
    public void Update(Commentaire commentaire) {
        String qry = "UPDATE `commentaire` SET `description`=? WHERE `theme`=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, commentaire.getDescription());
            stm.setString(2, commentaire.getTheme()); // Utilisation du thème comme condition de mise à jour

            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void Delete(Commentaire commentaire) {
        try {
            String qry = "DELETE FROM `commentaire` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, commentaire.getId());
            smt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void DeleteByID(int id) {
        try {
            String qry = "DELETE FROM `commentaire` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, id);
            smt.executeUpdate();
            System.out.println("Suppression Effectué");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
