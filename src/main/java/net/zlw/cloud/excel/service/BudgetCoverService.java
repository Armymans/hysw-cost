package net.zlw.cloud.excel.service;

import net.zlw.cloud.excel.model.BudgetCover;
import net.zlw.cloud.excel.model.SummaryShenji;

public interface BudgetCoverService {
    void coverImport(String id);

    void summaryUnitsImport();


    void partTableQuantitiesImport(String id);

    void bomTableImport(String id);

    void LastSummaryCoverImport(String id);

    void UnitProjectSummaryImport(String id);

    void summaryTableImport(String id);

    void verificationSheetImport(String id);

    SummaryShenji findBudgetCoverById(String id);
}
