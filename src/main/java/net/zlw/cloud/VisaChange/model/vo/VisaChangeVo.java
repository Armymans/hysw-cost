package net.zlw.cloud.VisaChange.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.util.List;
import java.util.Objects;


@Data
public class VisaChangeVo {
    private int pageNum;
    private int pageSize;
    private String id;
    private String ceaNum;
    private String projectNum;
    private String proportionContract;
    private String district;
    private String constructionUnit;
    private String projectNature;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String waterAddress;
    private String proposer;
    private String outsourcing;
    private String nameOfCostUnit;
    private String status;
    private String createTime;
    private String compileTime;
    private String projectName;
    private String createStartTime;
    private String createEndTime;
    private String keyword;

    private String amountCost;
    private String contractAmount;
    private String contractAmountShang;
    private String contractAmountXia;
    private String baseProjectId;
    private String loginUserId;
    private String amountVisaChange;
    private String amountVisaChangeAddXia;
    private String amountVisaChangeAddShang;
    private String proportionContractXia;
    private String proportionContractShang;

    private String amountVisaChangeXia;
    private String amountVisaChangeShang;

    private String auditorId;
    private String founderId;

    private String currentHandler;
    private List<AuditInfo> auditInfoList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisaChangeVo that = (VisaChangeVo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
