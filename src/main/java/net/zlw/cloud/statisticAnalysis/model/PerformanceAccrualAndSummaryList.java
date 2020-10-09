package net.zlw.cloud.statisticAnalysis.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceAccrualAndSummaryList {
    private List<PerformanPeople> peopleList = new ArrayList<>();
    private Double PerformanceProvisionTotal;
    private Double IssuedMonthTotal;
}
