package net.zlw.cloud.statisticAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVo {
    private String district;
    private String memberId;
    private String StatTime;
    private String endTime;
    private String keyword;
}
