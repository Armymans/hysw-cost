package net.zlw.cloud.statisticAnalysis.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
/*
员工绩效分析
 */
@Data
public class EmployeePerformance {
    private Integer projectNum;
    private Double achievemen;
    private String yearTime;
    private String monthTIme;
}
