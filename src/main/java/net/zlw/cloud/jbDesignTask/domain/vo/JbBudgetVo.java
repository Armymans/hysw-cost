package net.zlw.cloud.jbDesignTask.domain.vo;


import lombok.Data;

import java.util.List;

/***
 * 江北造价vo
 */
@Data
public class JbBudgetVo {

    private String id;
    private String project_id;
    private String budgeting_people;
    private String receipt_time;
    private String founder_id;
    private String project_name;
    private String remark;
    private String amount_cost;
    private String sure_result;
    private String sure_man;
    private String budgetingTime;
    private String remarks;
    private List<FileInfos> FileInfo;

}
