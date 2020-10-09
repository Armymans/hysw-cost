package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class OneCensus7 {
    private Integer budgeting;  //预算个数
    private Integer lastSettlementReview; //上家审核个数
    private Integer settlementAuditInformation; //下家送审个数
    private Integer trackAuditInfo; //跟踪审计
    private Integer visaChangeInformation; //签证变更个数
    private Integer progressPaymentInformation; //进度款支付个数
}
