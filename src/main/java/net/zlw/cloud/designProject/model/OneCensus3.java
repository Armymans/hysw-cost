package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class OneCensus3 {
    private String yearTime;
    private String monthTime;
    private String outMoney; //委外支出
    private String advMoney; //员工绩效

    private Integer revie;
    private Integer plot;
    private Integer comple;
    private Integer total;

    private String projectName;
    private String id;
}
