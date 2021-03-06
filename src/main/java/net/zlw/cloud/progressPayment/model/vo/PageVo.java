package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

@Data
public class PageVo {
    private Integer pageNum;
    private Integer pageSize;
    private String district;
    private String projectNature;
    private String projectType;
    private String startTime;
    private String endTime;
    private String keyword;
    private String progressStatus;
    private String uid; //当前登录用户id
    private String currentPeople; // 当前处理人

}
