package net.zlw.cloud.designProject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dell
 * @Date 2020/10/12 14:50
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOverviewVo {
    private Long buildDay;
    private String projectCount;
    private Integer missionCount;
}
