package net.zlw.cloud.designProject.model;

import lombok.Data;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.progressPayment.model.ProgressPaymentTotalPayment;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;

import java.util.Set;
@Data
public class ProjectVo3 {
    private BaseProject baseProject; //基本信息
    private DesignInfo designInfo;  //设计信息
    private Budgeting budgeting; //预算编制
    private TrackAuditInfo trackAuditInfo; //跟踪审计
    private LastSettlementReview lastSettlementReview; //上家结算审核
    private SettlementAuditInformation settlementAuditInformation; //下家结算审核
    private ProgressPaymentInformation progressPaymentInformation; //进度款支付信息

    private String NewcurrentPaymentInformation; //最新支付金额
    private String SumcurrentPaymentInformation; //累计支付次数
    private String currentPaymentRatio; //本期支付比例
    private String cumulativePaymentTimes; //累计支付次数


    private String amountVisaChangeSum;
    private String contractAmount;
    private String changeCount;
}
