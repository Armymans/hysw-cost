package net.zlw.cloud.warningDetails.model;

import lombok.Data;

@Data
public class PageVo {
    private int pageNum;
    private int pageSize;
    private String status;
    private String startTime;
    private String endTime;
    private String keyword;
}
