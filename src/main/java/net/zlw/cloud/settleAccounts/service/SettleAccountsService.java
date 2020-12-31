package net.zlw.cloud.settleAccounts.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
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

    void addUniProjectImport(String id, FileInputStream inputStream, FileInputStream inputStream2);
     LastSettlementReview selectPeople(UserInfo loginUser);

    void addsettleImport(String id, FileInputStream fileInputStream, FileInputStream stream, FileInputStream inputStream, FileInputStream inputStream2, FileInputStream inputStream3);

    void editOutsourceMoney(String id, String upOutMoney, String downOutMoney);

    void accountsSuccess(String ids, String s);

    void backOpnion(String id, String backOpnion);
}
