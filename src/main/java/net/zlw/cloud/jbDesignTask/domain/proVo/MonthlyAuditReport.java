package net.zlw.cloud.jbDesignTask.domain.proVo;

import lombok.Data;

@Data
public class MonthlyAuditReport {
    private String id;
    private String belong_time;
    private String title;
    private String execution_money;
    private String completed_time;
    private String completed_by;
    private String file_link;
}
