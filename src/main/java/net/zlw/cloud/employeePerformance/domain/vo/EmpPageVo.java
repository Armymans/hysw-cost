package net.zlw.cloud.employeePerformance.domain.vo;

import lombok.Data;

/**
 * @Classname EmpVo
 * @Description TODO
 * @Date 2020/12/17 19:53
 * @Created by sjf
 */
@Data
public class EmpPageVo {
    private int pageNum;
    private int pageSize;
    private String dept;
    private String projectNum;
    private String achievementsType;
    private String keword;
    private String overFlag;
}
