package net.zlw.cloud.statisticAnalysis.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ReturnEmployeePerformance {
    private Integer projectNum;
    private Double comparedMonthProjectNum;
    private Double achievemen;
    private Double comparedMonthAchievemen;
    private JSONArray picture;
}
