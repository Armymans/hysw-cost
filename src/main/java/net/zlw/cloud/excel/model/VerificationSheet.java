package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "verification_sheet")
@Data
public class VerificationSheet {
    /*
    * `id` varchar(60) NOT NULL,
  `construction_unit` varchar(60) DEFAULT NULL COMMENT '建设单位',
  `type` varchar(60) DEFAULT NULL COMMENT '类型',
  `construction organization` varchar(60) DEFAULT NULL COMMENT '施工单位',
  `professional` varchar(60) DEFAULT NULL COMMENT '专业',
  `project_name` varchar(60) DEFAULT NULL COMMENT '工程名称',
  `cea_num` varchar(60) DEFAULT NULL COMMENT 'CEA',
  `total` decimal(20,2) DEFAULT NULL COMMENT '合计',
  `authorized_amount` varchar(60) DEFAULT NULL COMMENT '审定金额大写',
  `subtract_forehead` decimal(20,2) DEFAULT NULL COMMENT '净核减额',
  `subtract_rate` decimal(20,2) DEFAULT NULL COMMENT '核减率',
  `excess_verification_reduction` decimal(20,2) DEFAULT NULL COMMENT '超额核减审计费',
  `actual_settlement_project` decimal(20,2) DEFAULT NULL COMMENT '工程实际结算款',
  `upper_case` varchar(60) DEFAULT NULL COMMENT '大写',
  `auditor` varchar(60) DEFAULT NULL COMMENT '审核人',
  `construction_organization_chapter` varchar(60) DEFAULT NULL COMMENT '施工单位(盖章)',
  `reviewer` varchar(60) DEFAULT NULL COMMENT '复核人',
  `operator` varchar(60) DEFAULT NULL COMMENT '经办人',
  `moddate` varchar(60) DEFAULT NULL COMMENT '审核/复核人日期',
  `date_possession` varchar(60) DEFAULT NULL COMMENT '施工单位日期',
  `development_organization_chapter` varchar(60) DEFAULT NULL COMMENT '建设单位(盖章)',
  `project_leader` varchar(60) DEFAULT NULL COMMENT '项目负责人',
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
    private String type;
    @Column(name = "construction_organization")
    private String constructionOrganization;
    private String professional;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "cea_num")
    private String ceaNum;
    private String total;
    @Column(name = "authorized_amount")
    private String authorizedAmount;
    @Column(name = "subtract_forehead")
    private BigDecimal subtractForehead;
    @Column(name = "subtract_rate")
    private BigDecimal subtractRate;
    @Column(name = "excess_verification_reduction")
    private BigDecimal excessVerificationReduction;
    @Column(name = "actual_settlement_project")
    private BigDecimal actualSettlementProject;
    @Column(name = "upper_case")
    private String upperCase;
    private String auditor;
    @Column(name = "construction_organization_chapter")
    private String constructionOrganizationChapter;
    private String reviewer;
    private String operator;
    private String moddate;
    @Column(name = "date_possession")
    private String datePossession;
    @Column(name = "development_organization_chapter")
    private String developmentOrganizationChapter;
    @Column(name = "project_leader")
    private String projectLeader;
    @Column(name = "settlement_id")
    private String settlementId;
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
