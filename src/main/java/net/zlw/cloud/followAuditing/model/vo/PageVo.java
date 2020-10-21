package net.zlw.cloud.followAuditing.model.vo;

import lombok.Data;

@Data
public class PageVo {
    //页码
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
    private String district;
    private String projectNature;
    private String waterSupplyType;
    private String startTime;
    private String endTIme;
    private String designCategory;
    private String constructionOrganization;
    private String keyword;

    // 跟踪审计的状态
    private String trackStatus;
    private String uid; //当前登录用户id


}
