package net.zlw.cloud.excel.service;

public interface BudgetCoverService {
    void coverImport();

    void summaryUnitsImport();


    void partTableQuantitiesImport();

    void bomTableImport(String id);

    void LastSummaryCoverImport(String id);
}
