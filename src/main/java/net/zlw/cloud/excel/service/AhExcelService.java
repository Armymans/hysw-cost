package net.zlw.cloud.excel.service;

import java.io.FileInputStream;

/**
 * @Classname AhExcelService
 * @Description TODO
 * @Date 2020/11/10 19:07
 * @Created by sjf
 */
public interface AhExcelService {

    void coverImport(String id, FileInputStream fileInputStream);

    void summarySheetImport(String id,FileInputStream fileInputStream);

    void quantitiesPartialWorksImport(String id,FileInputStream fileInputStream);

    void competitiveItemValuationImport(String id,FileInputStream fileInputStream);

    void taxStatementImport(String id,FileInputStream fileInputStream);

    void summaryMaterialsSuppliedAImport(String id,FileInputStream fileInputStream);

    void summaryMaterialsSuppliedBImport(String id,FileInputStream fileInputStream);





}
