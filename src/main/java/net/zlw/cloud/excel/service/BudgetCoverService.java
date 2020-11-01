package net.zlw.cloud.excel.service;

public interface BudgetCoverService {
    void coverImport();

    void summaryUnitsImport();


    void partTableQuantitiesImport(String id);

    void bomTableImport(String id);

    void LastSummaryCoverImport(String id);

    void UnitProjectSummaryImport(String id);

    void summaryTableImport(String id);

    void verificationSheetImport(String id);
}
