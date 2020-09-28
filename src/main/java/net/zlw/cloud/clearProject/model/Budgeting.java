package net.zlw.cloud.clearProject.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "budgeting")
public class Budgeting implements Serializable {
    /**
     * 唯一标识
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 造价金额(含税)
     */
    @Column(name = "amount_cost")
    private BigDecimal amountCost;

    /**
     * 预算编制人
     */
    @Column(name = "budgeting_people")
    private String budgetingPeople;

    /**
     * 造价增值税金额
     */
    @Column(name = "added_tax_amount")
    private BigDecimal addedTaxAmount;

    /**
     * 预算编制时间
     */
    @Column(name = "budgeting_time")
    private String budgetingTime;

    /**
     * 是否委外 1是 2否
     */
    @Column(name = "outsourcing")
    private String outsourcing;

    /**
     * 造价单位名称 1,XXX有限公司 2,XXX有限公司
     */
    @Column(name = "name_of_cost_unit")
    private String nameOfCostUnit;

    /**
     * 联系人
     */
    @Column(name = "contact")
    private String contact;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 委外金额
     */
    @Column(name = "amount_outsourcing")
    private BigDecimal amountOutsourcing;

    /**
     * 接收时间
     */
    @Column(name = "receipt_time")
    private String receiptTime;

    /**
     * 项目基本信息外键id
     */
    @Column(name = "base_project_id")
    private String baseProjectId;

    /**
     * 成本编制外键
     */
    @Column(name = "cost_preparation_id")
    private String costPreparationId;

    /**
     * 控价编制外键
     */
    @Column(name = "very_establishment_id")
    private String veryEstablishmentId;

    /**
     * 勘察信息外键
     */
    @Column(name = "survey_information_id")
    private String surveyInformationId;

    /**
     * 是否到账 0到账1未到账
     */
    @Column(name = "whether_account")
    private String whetherAccount;

    /**
     * 备注
     */
    @Column(name = "remarkes")
    private String remarkes;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 创建人id
     */
    @Column(name = "founder_id")
    private String founderId;

    /**
     * 创建人公司id
     */
    @Column(name = "founder_company_id")
    private String founderCompanyId;

    /**
     * 状态 0,正常 1,删除
     */
    @Column(name = "del_flag")
    private String delFlag;

    private static final long serialVersionUID = 1L;
}