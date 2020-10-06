package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class CostVo3{
    private String yearTime;
    private String monthTime;
    private Integer budgetingCount;
    private Integer desginStatus;
    private Integer progressPaymentInformation;
    private Integer visaApplyChangeInformationCount;
    private Integer trackAuditInfoCount;
    private Integer settleAccountsCount;
    private Integer total;
}
