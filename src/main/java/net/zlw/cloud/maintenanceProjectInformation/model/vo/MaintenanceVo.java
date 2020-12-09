package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;

import java.util.List;

/**
 * @Author dell
 * @Date 2020/10/12 14:03
 * @Version 1.0
 */
@Data
public class MaintenanceVo {
    private MaintenanceProjectInformation maintenanceProjectInformation;

//    private SurveyInformation surveyInformation;

    private SettlementAuditInformation settlementAuditInformation;

    private InvestigationOfTheAmount investigationOfTheAmount;

    private List<AuditInfo> auditInfos;

    private AuditInfo auditInfo;

    private String auditType;
    private String auditNumber;

    private String auditAgainFlag;
    private String json;

    private String pre1;
    private String pre2;

}
