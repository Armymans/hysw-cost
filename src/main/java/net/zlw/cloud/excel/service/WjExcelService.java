package net.zlw.cloud.excel.service;

/**
 * @Classname WjExcelService
 * @Description TODO
 * @Date 2020/11/10 9:59
 * @Created by sjf
 */

public interface WjExcelService {


    void coverWjExcelImport(String id);

    void summaryUnitsImport();

    void partTableQuantitiesImport(String id);
}
