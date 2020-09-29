package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "settlement_audit_information")
@Data
public class SettlementAuditInformation {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "authorized_number")
    private BigDecimal authorizedNumber;

    @Column(name = "subtract_the_number")
    private BigDecimal subtractTheNumber;

    @Column(name = "nuclear_number")
    private BigDecimal nuclearNumber;

    @Column(name = "remarkes")
    private String remarkes;

    @Column(name = "contract_amount")
    private BigDecimal contractAmount;

    @Column(name = "contract_remarkes")
    private String contractRemarkes;

    @Column(name = "prepare_people")
    private String preparePeople;

    @Column(name = "outsourcing")
    private String outsourcing;

    @Column(name = "name_of_the_cost")
    private String nameOfTheCost;

    @Column(name = "contact")
    private String contact;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "amount_outsourcing")
    private BigDecimal amountOutsourcing;

    @Column(name = "maintenance_project_information")
    private String maintenanceProjectInformation;

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

    @Column(name = "takeTime")
    private String take_time;

    @Column(name = "compile_time")
    private String compileTime;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "whether_account")
    private String whetherAccount;

    @Transient
    private String monthTime;  //月份
    @Transient
    private String district;  //地区
    @Transient
    private String projectName; //项目名
    @Transient
    private Double reviewNumberCost; //咨询费
    @Transient
    private Double Commission; //计提
    @Transient
    private BigDecimal sumbitMoney; //送审金额
    @Transient
    private Double subtractTheNumberMoney; //核减金额
    @Transient
    private Double BaseMoney; //基本金额
}
