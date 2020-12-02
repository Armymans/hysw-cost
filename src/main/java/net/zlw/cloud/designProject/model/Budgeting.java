package net.zlw.cloud.designProject.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "budgeting")
@Data
public class Budgeting{
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "amount_cost")
    private BigDecimal amountCost; //造价金额

    @Column(name = "budgeting_people")
    private String budgetingPeople;

    @Column(name = "added_tax_amount")
    private String addedTaxAmount;

    @Column(name = "budgeting_time")
    private String budgetingTime;

    @Column(name = "amount_outsourcing")
    private BigDecimal amountOutsourcing;

    @Column(name = "receipt_time")
    private String receiptTime;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "remarkes")
    private String remarkes;

    @Column(name = "contact_phone")
    private String contactPhone;

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

    @Column(name = "whether_account")
    private String whetherAccount;

    @Column(name = "clear_status")
    private String clearStatus;

    @Transient
    private String monthTime;
    @Transient
    private String projectName; //项目名称
    @Transient
    private String aB;//A/B  1A 2B'
    @Transient
    private BigDecimal biddingPriceControl; //招标控制价
    @Transient
    private Double budgetingCost; //预算编制造价咨询金额
    @Transient
    private Double budgetingStandard; //预算编制标底咨询金额
    @Transient
    private Double budgetingBase; //预算编制咨询费计算基数
    @Transient
    private Double budgetingCommission; //预算编制技提
    @Transient
    private String district; //地区
    @Transient
    private String projectType;//工程类别  固定安装
    @Transient
    private Double fiveHundredCost;//500
    @Transient
    private Double aThousandCost;//1000
    @Transient
    private Double twoThousandCost;//2000
    @Transient
    private Double subtotal;//跟踪审计小计
}
