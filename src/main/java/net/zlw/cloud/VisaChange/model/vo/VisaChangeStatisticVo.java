package net.zlw.cloud.VisaChange.model.vo;


import lombok.Data;

/***
 * 签证变更信息统计
 */

@Data
public class VisaChangeStatisticVo {

    private String id;

    //签证变更金额
    private String amountVisaChangeUp;
    private String amountVisaChangeDown;
    //占合同比例
    private String proportionContractUp;
    private String proportionContractDown;
    //编制人
    private String contact;
    //编制时间及创建时间
    private String createTime;
    private String compileTime;







}
