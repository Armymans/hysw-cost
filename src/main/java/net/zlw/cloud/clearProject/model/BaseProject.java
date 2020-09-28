package net.zlw.cloud.clearProject.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "base_project")
public class BaseProject implements Serializable {
    /**
     * 唯一标识
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 申请编号
     */
    @Column(name = "application_num")
    private String applicationNum;

    /**
     * CEA编号
     */
    @Column(name = "cea_num")
    private String ceaNum;

    /**
     * 项目编号
     */
    @Column(name = "project_num")
    private String projectNum;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 关联项目ID
     */
    @Column(name = "project_id")
    private String projectId;

    /**
     * 所属地区1芜湖2马鞍山3江北4吴江
     */
    @Column(name = "district")
    private String district;

    /**
     * 设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业
     */
    @Column(name = "design_category")
    private String designCategory;

    /**
     * 建设单位
     */
    @Column(name = "construction_unit")
    private String constructionUnit;

    /**
     * 联系人
     */
    @Column(name = "contacts")
    private String contacts;

    /**
     * 联系电话
     */
    @Column(name = "contact_number")
    private String contactNumber;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     *  主体 1居民住户2开发商 3政府事业 4工商户 5芜湖华衍
     */
    @Column(name = "subject")
    private String subject;

    /**
     * 客户联系电话
     */
    @Column(name = "customer_phone")
    private String customerPhone;

    /**
     * 施工单位 1 xxx有限公司 2 xxx有限公司 3 xxx有限公司
     */
    @Column(name = "construction_organization")
    private String constructionOrganization;

    /**
     * 项目性质:1新建 2改造
     */
    @Column(name = "project_nature")
    private String projectNature;

    /**
     * 项目类别1住宅区配套 2商业区配套 3工商区配套
     */
    @Column(name = "project_category")
    private String projectCategory;

    /**
     * 用水地址
     */
    @Column(name = "water_address")
    private String waterAddress;

    /**
     * 供水类型 1直供水 2二次供水
     */
    @Column(name = "water_supply_type")
    private String waterSupplyType;

    /**
     * 本次申报内容
     */
    @Column(name = "this_declaration")
    private String thisDeclaration;

    /**
     * 经办人
     */
    @Column(name = "agent")
    private String agent;

    /**
     * 经办人联系电话
     */
    @Column(name = "agent_phone")
    private String agentPhone;

    /**
     * 申请日期
     */
    @Column(name = "application_date")
    private String applicationDate;

    /**
     * 经营所在地
     */
    @Column(name = "business_location")
    private String businessLocation;

    /**
     * 业务类别
     */
    @Column(name = "business_types")
    private String businessTypes;

    /**
     * A/B  1A 2B
     */
    @Column(name = "a_b")
    private String aB;

    /**
     * 用水用途
     */
    @Column(name = "water_use")
    private String waterUse;

    /**
     * 消防表径
     */
    @Column(name = "fire_table_size")
    private String fireTableSize;

    /**
     * 分类口径
     */
    @Column(name = "classification_caliber")
    private String classificationCaliber;

    /**
     * 水表口径
     */
    @Column(name = "water_meter_diameter")
    private String waterMeterDiameter;

    /**
     * 站点
     */
    @Column(name = "site")
    private String site;

    /**
     * 系统编号
     */
    @Column(name = "system_number")
    private String systemNumber;

    /**
     * 申请人
     */
    @Column(name = "proposer")
    private String proposer;

    /**
     * 申报户数
     */
    @Column(name = "application_number")
    private String applicationNumber;

    /**
     * 设计状态 1待审核 2出图中 3未通过 4已完成
     */
    @Column(name = "desgin_status")
    private String desginStatus;

    /**
     * 预算状态 1待审核 2处理中 3未通过 4已完成 5未提交
     */
    @Column(name = "budget_status")
    private String budgetStatus;

    /**
     * 跟踪审计状态 1待审核 2未提交 3进行中 4未通过 5已完成
     */
    @Column(name = "track_status")
    private String trackStatus;

    /**
     * 签证变更状态 1待审核 2处理中 3未通过 4待确认 5进行中 6以完成
     */
    @Column(name = "visa_status")
    private String visaStatus;

    /**
     * 进度款支付状态 0未提交 1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
     */
    @Column(name = "progress_payment_status")
    private String progressPaymentStatus;

    /**
     * 结算状态 1待审核 2处理中 3未通过 4待确认 5已完成
     */
    @Column(name = "settle_accounts_status")
    private String settleAccountsStatus;

    /**
     * 判断是否删除 0:未删除 1:删除
     */
    @Column(name = "`status`")
    private String status;

    /**
     * 建设项目id
     */
    @Column(name = "building_project_id")
    private String buildingProjectId;

    /**
     * 项目合并后生成的虚拟编号
     */
    @Column(name = "virtual_code")
    private String virtualCode;

    /**
     * 是否为合并主项目:0是 1不是 
     */
    @Column(name = "merge_flag")
    private String mergeFlag;

    /**
     * 项目流程 例如 1项目设计,2预算编制,3跟踪审计,4进度款支付,5签证变更,6结算编制
     */
    @Column(name = "project_flow")
    private String projectFlow;

    /**
     * 营商0紧急 1不紧急
     */
    @Column(name = "should_be")
    private String shouldBe;

    /**
     * 是否到账 0是 1否
     */
    @Column(name = "whether_account")
    private String whetherAccount;

    /**
     * 结算是否到账 0是 1否
     */
    @Column(name = "sa_whether_account")
    private String saWhetherAccount;

    /**
     * 审核状态 1一审 2二审
     */
    @Column(name = "audit_number")
    private String auditNumber;

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
     * 删除标识 0,正常1,删除
     */
    @Column(name = "del_flag")
    private String delFlag;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}