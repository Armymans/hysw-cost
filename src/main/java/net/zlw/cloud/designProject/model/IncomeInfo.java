package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "income_info")
@Data
public class IncomeInfo {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "design_money")
    private BigDecimal designMoney;

    @Column(name = "budget_money")
    private BigDecimal budgetMoney;

    @Column(name = "upsubmit_money")
    private BigDecimal upsubmitMoney;

    @Column(name = "downsubmit_money")
    private BigDecimal downsubmitMoney;

    @Column(name = "truck_money")
    private BigDecimal truckMoney;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "founder_company_id")
    private String founderCompanyId;

    @Column(name = "del_flag")
    private String delFlag;
}
