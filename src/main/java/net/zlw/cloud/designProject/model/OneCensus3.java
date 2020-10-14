package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class OneCensus3 {
    private String yearTime;
    private String monthTime;
    private String outMoney; //委外支出
    private String advMoney; //员工绩效

    private Integer revie;//待审核
    private Integer plot;//出图中
    private Integer comple;//完成
    private Integer total;//全部

    private String projectName;
    private String id;
}
