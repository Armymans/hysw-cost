package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneCensus8 {
    private String memberName;  //成员名
    private Double desginAchievements; //计提个数
    private Double desginAchievements2; //建议计提个数
    private Double balance; //余额
}
