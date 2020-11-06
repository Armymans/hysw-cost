package net.zlw.cloud.settleAccounts.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAccountsVo {
    private String id;
    private BaseProject baseProject;
    private SettlementInfo lastSettlementInfo;
    private SettlementInfo settlementInfo;
    private LastSettlementReview lastSettlementReview;
    private SettlementAuditInformation settlementAuditInformation;
    private InvestigationOfTheAmount investigationOfTheAmount;

    private String auditId;
    private String auditNumber;
    private String auditType;
    private String checkAudit;





}
