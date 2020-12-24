package net.zlw.cloud.settleAccounts.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SettleAccountsService {
    List<AccountsVo> findAllAccounts(PageVo pageVo, UserInfo loginUser);

    void deleteAcmcounts(String id,UserInfo loginUser,HttpServletRequest request);

    void updateAccount(String s, UserInfo loginUser, String checkWhether);

    void addAccount(BaseAccountsVo baseAccountsVo, UserInfo loginUser, HttpServletRequest request);

    BaseAccountsVo findAccountById(String id, UserInfo loginUser);

    void updateAccountById(BaseAccountsVo baseAccountsVo,UserInfo loginUser,HttpServletRequest request);

    void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser,HttpServletRequest request);

    List<OtherInfo> selectInfoList(String baseId);

    void addAttribution(String baseId, String district, String designCategory, String prePeople);
}
