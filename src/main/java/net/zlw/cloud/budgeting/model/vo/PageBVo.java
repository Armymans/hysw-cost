package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

@Data
public class PageBVo {
    //页码
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
    private String projectCategory;
    private String district;
    private String waterSupplyType;
    private String projectNature;
    private String shouldBe;
    private String whetherAccount;
    private String startTime;
    private String endTime;
    private String keyword;
    private String budgetingStatus;

}
