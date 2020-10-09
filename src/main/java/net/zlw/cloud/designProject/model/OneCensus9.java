package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneCensus9 {
    private String id;
    private String projectName;  //项目名
    private String projectDeferral;  //项目递延
    private String aB;  //ab
    private String district; //地区
    private String takeTime;  //接收时间
    private String blueprintStartTime;  //完成时间
    private BigDecimal amountCost;  //造价金额
    private BigDecimal DesginMoney; //设计费
    private String isAmount; //是否到账
    private String isConfirm; //是否确认
    private Double desginAchievements; //计提个数
    private Double desginAchievements2; //建议计提个数
    private String accountTime; //到账时间
    private Double balance; //余额
}
