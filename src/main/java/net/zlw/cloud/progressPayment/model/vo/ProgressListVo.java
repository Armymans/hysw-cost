package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProgressListVo {
    private String id;
    private String baseId;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String progressPaymentStatus;
    private String district;
    private String waterAddress;
    private String constructionUnit;
    private String projectCategory;
    private String projectNature;
    private String projectType;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String username;
    private String outsourcing;
    private String nameOfCostUnit;
    private BigDecimal amountCost;
    private BigDecimal contractAmount;
    private BigDecimal totalPaymentAmount;
    private String accumulativePaymentProportion;
    private BigDecimal currentPaymentInformation;
    private String currentPaymentRatio;
    private BigDecimal cumulativeNumberPayment;
    private String constructionOrganization;
    private String receivingTime;
    private String compileTime;
    private String isShow;

    //当前处理人
    private String currentHandler;

    private String showUpdate;
    private String founderId;

    private List<AuditInfo> auditInfos;
}
