package net.zlw.cloud.excel.model;

import lombok.Data;
import net.zlw.cloud.excelLook.domain.SettlementAuditReportTextXontent;
import net.zlw.cloud.excelLook.domain.SettlementReportText;
import net.zlw.cloud.excelLook.domain.SettlementReportTextAttachment;
import net.zlw.cloud.excelLook.domain.SettlementReportTextReductionReasons;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Data
public class ReportTextVo {
    private SettlementReportText settlementReportText;
    //结算报告正文审核内容
    private List<SettlementAuditReportTextXontent> setAuditLists;
    //结算报告附件名称
    private List<SettlementReportTextAttachment> setReportLists;
    //结算报告审减原因
    private List<SettlementReportTextReductionReasons> settReductionLists;
}
