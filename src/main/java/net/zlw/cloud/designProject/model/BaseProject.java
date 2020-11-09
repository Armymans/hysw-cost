package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;


@Table(name = "base_project")
@Data
public class BaseProject{
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "application_num")
    private String applicationNum; //申请编号

    @Column(name = "cea_num")
    private String ceaNum; //CEA编号

    @Column(name = "project_num")
    private String projectNum; //项目编号

    @Column(name = "project_name")
    private String projectName; //项目名称

    @Column(name = "project_id")
    private String projectId; //关联项目ID

    @Column(name = "district")
    private String district; //所属地区1芜湖2马鞍山3江北4吴江

    @Column(name = "design_category")
    private String designCategory; //设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业

    @Column(name = "construction_unit")
    private String constructionUnit;//建设单位

    @Column(name = "contacts")
    private String contacts;//联系人

    @Column(name = "contact_number")
    private String contactNumber;//联系电话

    @Column(name = "customer_name")
    private String customerName;//客户名称

    @Column(name = "subject")
    private String subject;//项目类别 1居民住户2开发商 3政府事业 4工商户 5芜湖华衍',

    @Column(name = "customer_phone")
    private String customerPhone;//客户联系电话

    @Column(name = "construction_organization")
    private String constructionOrganization;//施工单位

    @Column(name = "project_nature")
    private String projectNature;//项目性质 0新建 1改造

    @Column(name = "project_category")
    private String projectCategory;//1住宅区配套

    @Column(name = "water_address")
    private String waterAddress;//用水地址

    @Column(name = "water_supply_type")
    private String waterSupplyType;//供水类型

    @Column(name = "this_declaration")
    private String thisDeclaration;//本次申报内容

    @Column(name = "agent")
    private String agent;//经办人

    @Column(name = "agent_phone")
    private String agentPhone;//经办人联系电话

    @Column(name = "application_date")
    private String applicationDate;//申请日期

    @Column(name = "business_location")
    private String businessLocation;//经营所在地

    @Column(name = "business_types")
    private String businessTypes;//业务类别

    @Column(name = "a_b")
    private String aB;//A/B  1A 2B'

    @Column(name = "water_use")
    private String waterUse; //用水用途

    @Column(name = "fire_table_size")
    private String fireTableSize; //消防表径

    @Column(name = "classification_caliber")
    private String classificationCaliber; //分类口径

    @Column(name = "water_meter_diameter")
    private String waterMeterDiameter; //水表口径

    @Column(name = "site")
    private String site; //站点

    @Column(name = "system_number")
    private String systemNumber; //系统编号

    @Column(name = "proposer")
    private String proposer; //申请人

    @Column(name = "application_number")
    private String applicationNumber; //申报户数

    @Column(name = "management_table")
    private String managementTable; //管理表字段

    @Column(name = "desgin_status")
    private String desginStatus; //设计状态 1待审核 2出图中 3未通过 4未到账 5已到账',

    @Column(name = "budget_status")
    private String budgetStatus; //预算状态 1待审核 2处理中 3未通过 4已完成 5未提交',

    @Column(name = "track_status")
    private String trackStatus; //跟踪审计状态 1待审核 2未提交 3进行中 4未通过 5已完成',

    @Column(name = "visa_status")
    private String visaStatus; //签证变更状态 1待审核 2处理中 3未通过 4待确认 5进行中 6以完成

    @Column(name = "status")
    private String status; //判断是否删除 0:未删除 1:删除

    @Column(name = "building_project_id")
    private String buildingProjectId; //建设项目id

    @Column(name = "virtual_code")
    private String virtualCode; //项目合并后生成的虚拟编号

    @Column(name = "merge_flag")
    private String mergeFlag; //是否合并 0:合并 1:未合并

    @Column(name = "project_flow")
    private String projectFlow; //项目流程 例如 1项目设计,2预算编制,3跟踪审计,4进度款支付,5签证变更,6结算编制

    @Column(name = "should_be")
    private String shouldBe; //营商0紧急 1不紧急

    @Column(name = "audit_number")
    private String auditNumber; //审核状态 1一审 2二审'

    @Column(name = "create_time")
    private String createTime; //创建时间

    @Column(name = "update_time")
    private String updateTime; //修改时间

    @Column(name = "founder_id")
    private String founderId; //创建人id

    @Column(name = "founder_company_id")
    private String founderCompanyId; //创建人公司id

    @Column(name = "del_flag")
    private String delFlag; //删除标识 0,正常1,删除



    @Transient
    private String reviewerId; //互相审核人
    @Transient
    private String orsubmit;
    @Transient
    private BigDecimal amountCost; //造价金额
    @Transient
    private BigDecimal desMoney; //设计费
    @Transient
    private Double accrualMoney; //应计提金额
    @Transient
    private Double adviseMoney;//建议计提金额
    @Transient
    private Double surplus;//余额

    @Transient
    private BigDecimal reviewNumber;  //送审数
    @Transient
    private BigDecimal authorizedNumber; //审定数
    @Transient
    private BigDecimal outMoney; //委外支出
    @Transient
    private BigDecimal advMoney; //绩效
    @Transient
    private BigDecimal costTotalAmount;  //成本总金额
    @Transient
    private BigDecimal biddingPriceControl;  //招标控制价
    @Transient
    private BigDecimal totalPaymentAmount; //进度款累计
    @Transient
    private BigDecimal visaMoney; //签证变更情况
    @Transient
    private String designChangeTime; //设计时间
    @Transient
    private String designer; //设计时间
    @Transient
    private Double accumulativePaymentProportion; //实付进度款比例
    @Transient
    private Double surplusPaymentProportion;//剩余进度款比例
    @Transient
    private Double amountVisaChange; //变更金额
    @Transient
    private Double contractAmount; //合同金额
    @Transient
    private String compileTime;
//    @Transient
//    private String designer; //设计人
//
//    @Transient
//    private String outsource; //是否委外
//
//    @Transient
//    private String designUnit; //设计单位名称
//
//    @Transient
//    private BigDecimal outsourceMoney; //委外金额
//
//    @Transient
//    private String takeTime; //接受时间
//
//    @Transient
//    private String blueprintStartTime; //正式出图时间
}
