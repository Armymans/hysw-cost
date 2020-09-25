package net.zlw.cloud.statisticalAnalysis.model.vo;

import lombok.Data;

@Data
public class NumberVo {
    private Integer totalNumberOfProjects; //项目总数
    private Integer totalNumberOfApproval; //待审核项目总数
    private Integer projectTotal; //进行中项目总数
    private Integer totalNumberOfCompleted; //已完成项目总数

}
