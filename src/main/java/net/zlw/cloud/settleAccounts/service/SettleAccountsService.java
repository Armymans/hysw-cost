package net.zlw.cloud.settleAccounts.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;

import java.util.List;

public interface SettleAccountsService {
    List<AccountsVo> findAllAccounts(PageVo pageVo, UserInfo loginUser);

    void deleteAcmcounts(String id);

    void updateAccount(String s, UserInfo loginUser, String checkWhether);

    void addAccount(BaseAccountsVo baseAccountsVo, UserInfo loginUser);

    BaseAccountsVo findAccountById(String id, UserInfo loginUser);

    void updateAccountById(BaseAccountsVo baseAccountsVo,UserInfo loginUser);

    void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser);

    List<OtherInfo> selectInfoList(String baseId);
}
