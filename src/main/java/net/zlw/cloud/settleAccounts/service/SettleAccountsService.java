package net.zlw.cloud.settleAccounts.service;

import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;

import java.util.List;

public interface SettleAccountsService {
    List<AccountsVo> findAllAccounts(PageVo pageVo);

    void deleteAcmcounts(String id);

    void updateAccount(String s);

    void addAccount(BaseAccountsVo baseAccountsVo);

    BaseAccountsVo findAccountById(String id);

    void updateAccountById(BaseAccountsVo baseAccountsVo);

    void batchReview(BatchReviewVo batchReviewVo);
}
