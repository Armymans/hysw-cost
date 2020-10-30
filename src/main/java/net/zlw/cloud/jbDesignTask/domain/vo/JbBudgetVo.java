package net.zlw.cloud.jbDesignTask.domain.vo;


import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.FileInfos;

import java.util.List;

/***
 * 江北造价vo
 */
@Data
public class JbBudgetVo {

    private String id;
    private String projectId;
    private String budgetingPeople;
    private String receiptTime;
    private String founderId;
    private String projectName;
    private String remark;
    private String amountCost;
    private String sureResult;
    private String sureMan;
    private String budgetingTime;
    private List<FileInfos> fileInfos;

}
