package com.hemotransfert.hemotransfert;

import java.util.*;

public class ReportService {
    private final DemandededonRepository demandededonRepository;

    public ReportService(DemandededonRepository demandededonRepository) {
        this.demandededonRepository = demandededonRepository;
    }

    public Map<String, Object> generateReport() {
        List<Demandededon> demandededons = demandededonRepository.findAll();

        Map<String, Object> report = new HashMap<>();
        report.put("total_demandededon", demandededons.size());
        report.put("dons_par_date", calculateDonsByDate(demandededons));
        report.put("repartition_type_don", calculateDonTypeDistribution(demandededons));
        report.put("quantite_demande", calculateTotalDemandQuantity(demandededons));
        report.put("status_demande", calculateDemandStatus(demandededons));

        return report;
    }

    private Map<String, Integer> calculateDonsByDate(List<Demandededon> demandededons) {
        Map<String, Integer> donsByDate = new HashMap<>();
        for (Demandededon demandededon : demandededons) {
            String date = demandededon.getDateDemande().toString();
            donsByDate.put(date, donsByDate.getOrDefault(date, 0) + 1);
        }
        return donsByDate;
    }

    private Map<String, Integer> calculateDonTypeDistribution(List<Demandededon> demandededons) {
        Map<String, Integer> donTypeDistribution = new HashMap<>();
        for (Demandededon demandededon : demandededons) {
            String typeDon = demandededon.getTypeDon();
            donTypeDistribution.put(typeDon, donTypeDistribution.getOrDefault(typeDon, 0) + 1);
        }
        return donTypeDistribution;
    }

    private int calculateTotalDemandQuantity(List<Demandededon> demandededons) {
        int totalDemandQuantity = 0;
        for (Demandededon demandededon : demandededons) {
            totalDemandQuantity += demandededon.getQuantiteDemande();
        }
        return totalDemandQuantity;
    }

    private Map<String, Integer> calculateDemandStatus(List<Demandededon> demandededons) {
        Map<String, Integer> demandStatus = new HashMap<>();
        for (Demandededon demandededon : demandededons) {
            String status = demandededon.getStatusDemande();
            demandStatus.put(status, demandStatus.getOrDefault(status, 0) + 1);
        }
        return demandStatus;
    }
}