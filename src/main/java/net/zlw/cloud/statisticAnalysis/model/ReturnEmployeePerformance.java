package net.zlw.cloud.statisticAnalysis.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ReturnEmployeePerformance {
    private Integer projectNum;
    private String comparedMonthProjectNum;
    private Double achievemen;
    private String comparedMonthAchievemen;
    private JSONArray picture;
}
