package net.zlw.cloud.designProject.model;

import lombok.Data;

/**
 * @Author dell
 * @Date 2020/10/14 13:42
 * @Version 1.0
 */
@Data
public class ProjectNumber {
    private Integer total;
    private Integer withAuditCount;
    private Integer conductCount;
    private Integer completeCount;
}
