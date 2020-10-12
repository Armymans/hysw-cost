package net.zlw.cloud.progressPayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "base_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProject implements Serializable {
    /*
    * `id` varchar(60) NOT NULL COMMENT '唯一标识',
  `application_num` varchar(120) DEFAULT NULL COMMENT '申请编号',
  `cea_num` varchar(120) DEFAULT NULL COMMENT 'CEA编号',
  `project_num` varchar(120) DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(120) DEFAULT NULL COMMENT '项目名称',
  `project_id` varchar(120) DEFAULT NULL COMMENT '关联项目ID',
  `district` varchar(1) DEFAULT NULL COMMENT '所属地区1芜湖2马鞍山3江北4吴江',
  `design_category` varchar(1) DEFAULT NULL COMMENT '设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业',
  `construction_unit` varchar(120) DEFAULT NULL COMMENT '建设单位',
  `contacts` varchar(120) DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(120) DEFAULT NULL COMMENT '联系电话',
  `customer_name` varchar(120) DEFAULT NULL COMMENT '客户名称',
  `subject` varchar(1) DEFAULT NULL COMMENT ' 主体 1居民住户2开发商 3政府事业 4工商户 5芜湖华衍',
  `customer_phone` varchar(120) DEFAULT NULL COMMENT '客户联系电话',
  `construction_organization` varchar(1) DEFAULT NULL COMMENT '施工单位 1 xxx有限公司 2 xxx有限公司 3 xxx有限公司',
  `project_nature` varchar(1) DEFAULT NULL COMMENT '项目性质:1新建 2改造',
  `project_category` varchar(1) DEFAULT NULL COMMENT '项目类别1住宅区配套 2商业区配套 3工商区配套',
  `water_address` varchar(120) DEFAULT NULL COMMENT '用水地址',
  `water_supply_type` varchar(1) DEFAULT NULL COMMENT '供水类型 1直供水 2二次供水',
  `this_declaration` varchar(120) DEFAULT NULL COMMENT '本次申报内容',
  `agent` varchar(120) DEFAULT NULL COMMENT '经办人',
  `agent_phone` varchar(120) DEFAULT NULL COMMENT '经办人联系电话',
  `application_date` varchar(120) DEFAULT NULL COMMENT '申请日期',
  `business_location` varchar(120) DEFAULT NULL COMMENT '经营所在地',
  `business_types` varchar(120) DEFAULT NULL COMMENT '业务类别',
  `a_b` varchar(1) DEFAULT NULL COMMENT 'A/B  1A 2B',
  `water_use` varchar(120) DEFAULT NULL COMMENT '用水用途',
  `fire_table_size` varchar(120) DEFAULT NULL COMMENT '消防表径',
  `classification_caliber` varchar(120) DEFAULT NULL COMMENT '分类口径',
  `water_meter_diameter` varchar(120) DEFAULT NULL COMMENT '水表口径',
  `site` varchar(120) DEFAULT NULL COMMENT '站点',
  `system_number` varchar(60) DEFAULT NULL COMMENT '系统编号',
  `proposer` varchar(60) DEFAULT NULL COMMENT '申请人',
  `application_number` varchar(10) DEFAULT NULL COMMENT '申报户数',
  `desgin_status` varchar(1) DEFAULT NULL COMMENT '设计状态 1待审核 2出图中 3未通过 4未到账 5已到账',
  `budget_status` varchar(1) DEFAULT NULL COMMENT '预算状态 1待审核 2处理中 3未通过 4已完成 5未提交',
  `track_status` varchar(1) DEFAULT NULL COMMENT '跟踪审计状态 1待审核 2未提交 3进行中 4未通过 5已完成',
  `visa_status` varchar(1) DEFAULT NULL COMMENT '签证变更状态 1待审核 2处理中 3未通过 4待确认 5进行中 6以完成',
  `progress_payment_status` varchar(1) DEFAULT NULL COMMENT '进度款支付状态',
  `settle_accounts_status` varchar(1) DEFAULT NULL COMMENT '结算状态',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_croatian_ci DEFAULT '0' COMMENT '判断是否删除 0:未删除 1:删除',
  `building_project_id` varchar(60) DEFAULT NULL COMMENT '建设项目id',
  `virtual_code` varchar(60) DEFAULT NULL COMMENT '项目合并后生成的虚拟编号',
  `merge_flag` varchar(1) DEFAULT NULL COMMENT '是否合并 0:合并 1:未合并',
  `project_flow` varchar(60) DEFAULT NULL COMMENT '项目流程 例如 1项目设计,2预算编制,3跟踪审计,4进度款支付,5签证变更,6结算编制',
  `should_be` varchar(1) DEFAULT NULL COMMENT '营商0紧急 1不紧急',
  `whether_account` varchar(1) DEFAULT NULL COMMENT '是否到账 0是 1否',
  `audit_number` varchar(60) DEFAULT NULL COMMENT '审核状态 1一审 2二审',
  `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '删除标识 0,正常1,删除',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "application_num")
    private String applicationNum;
    @Column(name = "cea_num")
    private String ceaNum;
    @Column(name = "project_num")
    private String projectNum;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_id")
    private String projectId;
    private String district;
    @Column(name = "design_category")
    private String designCategory;
    @Column(name = "construction_unit")
    private String constructionUnit;
    private String contacts;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "customer_name")
    private String customerName;
    private String subject;
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "construction_organization")
    private String constructionOrganization;
    @Column(name = "project_nature")
    private String projectNature;
    @Column(name = "project_category")
    private String projectCategory;
    @Column(name = "water_address")
    private String waterAddress;
    @Column(name = "water_supply_type")
    private String waterSupplyType;
    @Column(name = "this_declaration")
    private String thisDeclaration;
    private String agent;
    @Column(name = "agent_phone")
    private String agentPhone;
    @Column(name = "application_date")
    private String applicationDate;
    @Column(name = "business_location")
    private String businessLocation;
    @Column(name = "business_types")
    private String businessTypes;
    @Column(name = "a_b")
    private String aB;
    @Column(name = "water_use")
    private String waterUse;
    @Column(name = "fire_table_size")
    private String fireTableSize;
    @Column(name = "classification_caliber")
    private String classificationCaliber;
    @Column(name = "water_meter_diameter")
    private String waterMeterDiameter;
    private String site;
    @Column(name = "system_number")
    private String systemNumber;
    private String proposer;
    @Column(name = "application_number")
    private String applicationNumber;
    @Column(name = "desgin_status")
    private String desginStatus;
    @Column(name = "budget_status")
    private String budgetStatus;
    @Column(name = "track_status")
    private String trackStatus;
    @Column(name = "visa_status")
    private String visaStatus;
    @Column(name = "progress_payment_status")
    private String progressPaymentStatus;
    @Column(name = "settle_accounts_status")
    private String settleAccountsStatus;
    private String status;
    @Column(name = "building_project_id")
    private String buildingProjectId;
    @Column(name = "virtual_code")
    private String virtualCode;
    @Column(name = "merge_flag")
    private String mergeFlag;
    @Column(name = "project_flow")
    private String projectFlow;
    @Column(name = "should_be")
    private String shouldBe;
    @Column(name = "whether_account")
    private String whetherAccount;
    @Column(name = "sa_whether_account")
    private String saWhetherAccount;
    @Column(name = "audit_number")
    private String auditNumber;
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
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "updated_at")
    private String updatedAt;


}
