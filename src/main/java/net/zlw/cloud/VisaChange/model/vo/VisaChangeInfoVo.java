package net.zlw.cloud.VisaChange.model.vo;


import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;

import java.util.List;

@Data
public class VisaChangeInfoVo {

    //项目基本信息
    private BaseProject baseProject;
    private String id;
    private String applicantNameUp;
    private String remarkUp;


    private String submitMoneyDown;
    private String applicantNameDown;
    private String remarkDown;

    private List<VisaChangeStatisticVo> changeStatisticVos;

    //变更次数
    private String changeNum;
    //上家累计签证/变更金额
    private String totalAmountVisaChangeUp;
    //下家累计签证/变更金额
    private String totalAmountVisaChangeDown;
    //累计占上家合同比例
    private String totalProportionContractUp;
    //累计占下家合同比例
    private String totalProportionContractDown;


    private String amountVisaChange;
    private String contractAmount;
    private String compileTime;
    private String completionTime;
    private String proportionContract;
    private String outsourcing;
    private String nameOfCostUnit;
    private String contact;
    private String contactNumber;
    private String outsourcingAmount;
    private String visaChangeReason;

    //下家签证信息
    private String changeDownId;
    private String amountVisaChangeDown;
    private String contractAmountDown;
    private String compileTimeDown;
    private String completionTimeDown;
    private String proportionContractDown;
    private String outsourcingDown;
    private String nameOfCostUnitDown;
    private String contactDown;
    private String contactNumberDown;
    private String outsourcingAmountDown;
    private String visaChangeReasonDown;


    //baseProjectId外键
    private String baseProjectId;
    private String upAndDownMark;


    private String loginUserId;


    private List<AuditInfo> auditInfos;


    private String pId;
    private String auditId;
    private String auditOpinion;
    private String auditResult;


}
