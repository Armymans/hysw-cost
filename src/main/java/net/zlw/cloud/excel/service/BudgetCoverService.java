package net.zlw.cloud.excel.service;

import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excel.model.ReportTextVo;
import net.zlw.cloud.excel.model.SummaryShenji;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;

public interface BudgetCoverService {
    void coverImport(String id);

    void summaryUnitsImport(String id);


    void partTableQuantitiesImport(String id);

    void bomTableImport(String id);

    void LastSummaryCoverImport(String id);

    void UnitProjectSummaryImport(String id);

    void summaryTableImport(String id);

    void verificationSheetImport(String id);

    SummaryShenji findBudgetCoverById(String id);

    void materialAnalysisImport(String id);

    void updateFinalReport(FinalReport finalReport);

    void updateDirectory(SettlementDirectory settlementDirectory);

    void updateReportContent(ReportTextVo reportTextVo);

    void addbudgetAll(String id);
}
