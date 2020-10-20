package net.zlw.cloud.settleAccounts.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.budgeting.model.vo.BudgetingListVo;

import java.math.BigDecimal;
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
