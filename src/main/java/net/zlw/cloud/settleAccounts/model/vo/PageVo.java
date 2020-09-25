package net.zlw.cloud.settleAccounts.model.vo;

import lombok.Data;

@Data
public class PageVo {
    private String district;
    private String projectNature;
    private String settleAccountsStatus;
    private String saWhetherAccount;
    private String startTime;
    private String endTime;
    private String keyword;
}
