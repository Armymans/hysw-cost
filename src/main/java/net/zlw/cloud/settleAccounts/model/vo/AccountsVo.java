package net.zlw.cloud.settleAccounts.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsVo {
    private String id;
    private String accountId;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String settleAccountsStatus;
    private String district;
    private String constructionUnit;
    private String projectNature;
    private String customerName;
    private String waterAddress;
    private String preparePeople;
    private String outsourcing;
    private String nameOfCostUnit;
    private BigDecimal lReviewNumber;
    private BigDecimal sumbitMoney;
    private BigDecimal authorizedNumber;
    private String compileTime;
    private String takeTime;
    private String projectCategory;
    private String designCategory;
    private String waterSupplyType;
    private String whetherAccount;
    private String auditorId;
    //创建人
    private String founderId;

    //当前处理人
    private String currentHandler;
    private List<AuditInfo> auditInfoList;

    //回显待确认按钮
    private String showConfirmed;
    //回显归属
    private String attributionShow;
    //回显委外金额
    private String isShow;
    //殷丽萍退回回显
    private String yinShow;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsVo that = (AccountsVo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
