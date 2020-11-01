package net.zlw.cloud.whFinance.domain.vo;

import lombok.Data;
import net.zlw.cloud.excel.model.BomTable;

import java.util.List;

/***
 * 财务对接Vo
 */
@Data
public class BomTableVo {

    private String id;
    private String projectId;
    private String businessCode;
    private String businessProcess;
    private String dateOf;
    private String salesOrganization;
    private String inventoryOrganization;
    private String ceaNum;
    private String acquisitionTypes;
    private String contractor;
    private String projectCategoriesCoding;
    private String projectTypes;
    private String itemCoding;
    private String projectName;
    private String acquisitionDepartment;
    private List<BomTable> bomTables;
}
