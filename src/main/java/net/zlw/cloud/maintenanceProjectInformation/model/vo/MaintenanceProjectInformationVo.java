package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author dell
 * @Date 2020/9/27 15:47
 * @Version 1.0
 */
@Data
public class MaintenanceProjectInformationVo {
    private String id;
    //维修项目编号
    private String maintenanceItemId;
    //维修项目名称
    private String maintenanceItemName;
    //维修项目类型
    private String maintenanceItemType;
    // 报送部门
    private String submittedDepartment;
    //报送时间
    private String submitTime;

    private String baseProjectId;
    /**
     * 编制人
     */
    private String preparePeople;

    /**
     * 项目地址
     */
    private String projectAddress;

    /**
     * 施工单位Id
     */
    private String constructionUnitId;

    /**
     * 送审金额
     */
    private BigDecimal reviewAmount;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 备注
     */

    private String remarkes;
    // 勘察日期
    private String surveyDate;
    //勘察人员
    private String investigationPersonnel;
    //勘察简述
    private String surveyBriefly;
    //勘察备注
    private String amountRemarks;


    // 结算审核信息
//    private SettlementAuditInformation settlementAuditInformation;
    // 审定数
    private BigDecimal authorizedNumber;
    // 核减率
    private BigDecimal subtractTheNumber;
    // 核增数
    private BigDecimal nuclearNumber;
    // 核减率
    private BigDecimal subtractRate;
    private BigDecimal contractAmount;
    private String contractRemarkes;
    private String preparePeople2;
    private String outsourcing;
    private String nameOfTheCost;
    private String contact;
    private String contactPhone;
    private BigDecimal amountOutsourcing;
    private String compileTime;
    private String remark;

    // 勘探金额
    private BigDecimal unbalancedQuotationAdjustment;
    private BigDecimal punishAmount;
    private BigDecimal outboundAmount;
    private BigDecimal materialDifferenceAmount;
    //审核信息
//    private AuditInfo auditInfo;
    //审核人id
    private String auditorId;
    private String auditResult;
    private String auditOpinion;
    private String auditType;
    private String auditNumber;

    private List<AuditInfo> auditInfos;

    //登录人id
    private String loginUserId;


    private String key;

    // 项目信息 jwxxjxmxx
    private String type;
    //勘查记录 jwxxjkcjl
    private String type1;
    // 甲供 jwxxjjgclsbfx
    private String type2;
    // 乙供 jwxxjygctc
    private String type3;
    // 不平衡报价 jwxxjbphbjtzxx
    private String type4;
    // 其他扣罚条款 jwxxjqtkftkxx
    private String type5;
    // 其他相关信息 jwxxjqtxgxx
    private String type6;
    // 结算汇总表 jwxxjjsshxx
    private String type7;
    // 结算报告 jwxxjjsbg
    private String type8;
    // 结算审核其他资料 jwxxjjsshxxqtzl
    private String type9;

}
