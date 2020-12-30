package net.zlw.cloud.VisaChange.model.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class VisaChangeListVo {
    private String id;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String status;
    private String currentHandler;
    private String district;
    private String constructionUnit;
    private String projectNature;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String waterAddress;
    private String outsourcing;
    private String nameOfCostUnit;
    private int amountCost;
    private BigDecimal contractAmountShang;
    private String amountVisaChangeAddShang;
    private String proportionContractShang;
    private BigDecimal contractAmountXia;
    private String amountVisaChangeAddXia;
    private String proportionContractXia;
    private String currentShang;
    private String currentXia;
    private String createTime;
    private String compileTime;

    private String upAndDownMark;
    private String baseProjectId;
    private String showUpdate;
    private String founderId;

    private String showUnderway;
    private Integer changeNum;
    private String isShow;
    private String backShow;

}
