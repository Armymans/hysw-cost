package net.zlw.cloud.VisaChange.model.vo;


import lombok.Data;

import java.math.BigDecimal;

/***
 * 签证变更信息统计
 */

@Data
public class VisaChangeStatisticVo {
    private String baseId;
    private Integer changeNum;
    private BigDecimal visaChangeUpAmount;
    private BigDecimal visaChangeDownAmount;
    private String visaChangeUpProportionContract;
    private String visaChangeDownProportionContract;
    private String creatorName;
    //接收时间
    private String completionTime;
    //编制时间
    private String compileTime;





}
