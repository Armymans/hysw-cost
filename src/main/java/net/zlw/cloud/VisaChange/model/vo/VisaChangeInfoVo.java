package net.zlw.cloud.VisaChange.model.vo;


import lombok.Data;
import net.zlw.cloud.progressPayment.model.BaseProject;

import java.util.List;

@Data
public class VisaChangeInfoVo {
//上家签证变更申请
    private String applicantNameShang;
    private String remarkShang;


//    下家签证变更申请
    private String submitMoneyXia;
    private String applicantNameXia;
    private String remarkXia;

//   上家签证信息
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

//   下家签证信息
    private String amountVisaChangeXia;
    private String contractAmountXia;
    private String compileTimeXia;
    private String completionTimeXia;
    private String proportionContractXia;
    private String outsourcingXia;
    private String nameOfCostUnitXia;
    private String contactXia;
    private String contactNumberXia;
    private String outsourcingAmountXia;
    private String visaChangeReasonXia;

//    baseProjectId外键
    private String baseProjectId;

    private String upAndDownMark;


//    审核id
    private String auditId;
//   审核意见
    private String auditOpinion;

    private String loginUserId;

    private BaseProject baseProject;

    private VisaChangeStatisticVo visaChangeStatisticVo;





}
