package net.zlw.cloud.VisaChange.model.vo;

import lombok.Data;

@Data
public class PageVo {
    private int pageNum;
    private int pageSize;
    private String district;
    private String projectNature;
    private String startTime;
    private String endTime;
    private String keyword;
    private String status;

    private String userId;

}
