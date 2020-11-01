package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "last_summary_cover")
public class LastSummaryCover {
    /*
    *   `id` varchar(60) NOT NULL,
  `construction_unit` varchar(60) DEFAULT NULL COMMENT '建设单位',
  `name_project` varchar(60) DEFAULT NULL COMMENT '工程名称',
  `amount_cost_lowercase` decimal(20,2) DEFAULT NULL COMMENT '造价金额小写',
  `amount_cost_capital` varchar(60) DEFAULT NULL COMMENT '造价金额大写',
  `unit` varchar(60) DEFAULT NULL COMMENT '编制单位',
  `prepare_people` varchar(60) DEFAULT NULL COMMENT '编制人',
  `reviewer` varchar(60) DEFAULT NULL COMMENT '审核人',
  `compile_time` varchar(60) DEFAULT NULL COMMENT '编制时间',
  `budget_id` varchar(60) DEFAULT NULL COMMENT '预算编制外键',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',

    * */
    @Id
    private String id;
    @Column(name = "construction_unit")
    private String constructionUnit;
    @Column(name = "name_project")
    private String nameProject;
    @Column(name = "amount_cost_lowercase")
    private BigDecimal amountCostLowercase;
    @Column(name = "amount_cost_capital")
    private String amountCostCapital;
    private String unit;
    @Column(name = "prepare_people")
    private String preparePeople;
    private String reviewer;
    @Column(name = "compile_time")
    private String compileTime;
    @Column(name = "last_settlement_id")
    private String lastSettlementId;
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
