package net.zlw.cloud.designProject.model;

import lombok.Data;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
@Data
public class ProjectVo3 {
    private BaseProject baseProject; //基本信息

    private DesignInfo designInfo;  //设计信息
    private AnhuiMoneyinfo anhuiMoneyinfo;  //安徽支付信息
    private WujiangMoneyInfo wujiangMoneyInfo; //吴江支付信息
    private ProjectExploration projectExploration; //项目勘探
    private PackageCame packageCame; //方案会审
    private DesignChangeInfo DesignChangeInfo; //设计变更

    private Budgeting budgeting; //预算编制
    private CostPreparation costPreparation; //成本编制
    private VeryEstablishment veryEstablishment; //控价编制

    private TrackAuditInfo trackAuditInfo; //跟踪审计

    private LastSettlementReview lastSettlementReview; //上家结算审核
    private SettlementAuditInformation settlementAuditInformation; //下家结算审核


    private ProgressPaymentInformation progressPaymentInformation; //进度款支付信息

    private String NewcurrentPaymentInformation; //最新支付金额
    private String SumcurrentPaymentInformation; //累计支付金额
    private String currentPaymentRatio; //本期支付比例
    private String cumulativePaymentTimes; //累计支付次数

    private String amountVisaChangeSum;
    private String contractAmount;
    private String changeCount;
    // 查看上下家显隐状态
    private Integer wori;
}
