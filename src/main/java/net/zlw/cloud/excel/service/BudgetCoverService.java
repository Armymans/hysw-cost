package net.zlw.cloud.excel.service;

import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excel.model.PartTableQuantities;
import net.zlw.cloud.excel.model.ReportTextVo;
import net.zlw.cloud.excel.model.SummaryShenji;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;

import java.io.FileInputStream;
import java.util.List;

public interface BudgetCoverService {
    void coverImport(String id, FileInputStream fileInputStream);

    void summaryUnitsImport(String id, FileInputStream fileInputStream);


    void partTableQuantitiesImport(String id, FileInputStream fileInputStream);

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

    void addbudgetAll(String id, FileInputStream inputStream, FileInputStream inputStream2, FileInputStream fileInputStream);

     void addbudgetAllXindian(String id, FileInputStream inputStream, FileInputStream inputStream2, FileInputStream inputStream3, FileInputStream inputStream4, FileInputStream inputStream5, FileInputStream inputStream6, FileInputStream fileInputStream);

    List<PartTableQuantities> findPartTableQuantitiesAll(String id);
}
