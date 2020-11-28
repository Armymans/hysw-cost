package net.zlw.cloud.index.model.vo;

import lombok.Data;
import net.zlw.cloud.index.model.CostSum;
import net.zlw.cloud.index.model.DesignSum;

@Data
public class StatisticalData {
    private Integer NumberProjects;
    private Double GeneralIncome;
    private Double OutsourcingSpending;
    private Double OperatingIncome;
    private Double Achievements;

    private Integer NumberProjects2;
    private Double GeneralIncome2;
    private Double OutsourcingSpending2;
    private Double OperatingIncome2;
    private Double Achievements2;
}
