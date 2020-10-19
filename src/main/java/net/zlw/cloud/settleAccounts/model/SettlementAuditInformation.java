package net.zlw.cloud.settleAccounts.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "settlement_audit_information")
@Data
public class SettlementAuditInformation implements Serializable {
    @Id
    private String id;
    @Column(name = "authorized_number")
    private BigDecimal authorizedNumber;
    @Column(name = "subtract_the_number")
    private BigDecimal subtractTheNumber;
    @Column(name = "nuclear_number")
    private BigDecimal nuclearNumber;
    private String remarkes;
    @Column(name = "contract_amount")
    private BigDecimal contractAmount;
    @Column(name = "contract_remarkes")
    private String contractRemarkes;
    @Column(name = "prepare_people")
    private String preparePeople;
    private String outsourcing;
    @Column(name = "name_of_the_cost")
    private String nameOfTheCost;
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
    @Column(name = "take_time")
    private String takeTime;
    @Column(name = "compile_time")
    private String compileTime;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "whether_account")
    private String whetherAccount;


}
