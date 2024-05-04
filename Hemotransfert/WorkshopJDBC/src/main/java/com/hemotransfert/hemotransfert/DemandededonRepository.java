package com.hemotransfert.hemotransfert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandededonRepository {
    private final Connection connection;

    public DemandededonRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Demandededon> findAll() {
        List<Demandededon> demandededons = new ArrayList<>();
        String select = "SELECT * FROM demandededons";
        try (PreparedStatement st = connection.prepareStatement(select)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Demandededon demandededon = new Demandededon();
                demandededon.setIdDemande(rs.getInt("idDemande"));
                demandededon.setDateDemande(rs.getDate("dateDemande").toLocalDate());
                demandededon.setTypeDon(rs.getString("typeDon"));
                demandededon.setQuantiteDemande(rs.getInt("quantiteDemande"));
                demandededon.setStatusDemande(rs.getString("statusDemande"));
                demandededons.add(demandededon);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return demandededons;
    }
}