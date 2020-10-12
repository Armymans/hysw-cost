package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

@Data
public class PageBVo {
    private int pageNum;
    private int pageSize;
    private String district;
    private String waterSupplyType;
    private String projectNature;
    private String shouldBe;
    private String whetherAccount;
    private String startTime;
    private String endTime;
    private String keyword;
    private String budgetingStatus;
    private String designCategory;

}
