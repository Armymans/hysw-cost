package net.zlw.cloud.excel.service;

/**
 * @Classname AhExcelService
 * @Description TODO
 * @Date 2020/11/10 19:07
 * @Created by sjf
 */
public interface AhExcelService {

    void coverImport(String id);

    void summarySheetImport(String id);

    void quantitiesPartialWorksImport(String id);

    void competitiveItemValuationImport(String id);

    void taxStatementImport(String id);

    void summaryMaterialsSuppliedAImport(String id);

    void summaryMaterialsSuppliedBImport(String id);

}
