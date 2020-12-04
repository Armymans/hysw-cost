package net.zlw.cloud.budgeting.service;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;

import java.util.List;

public interface BudgetingService {
    void addBudgeting(BudgetingVo budgetingVo,UserInfo loginUser);

    BudgetingVo selectBudgetingById(String id, UserInfo loginUser);

    void updateBudgeting(BudgetingVo budgetingVo,UserInfo loginUser);

    void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser);

    void intoAccount(String s, String ids);

    List<BudgetingListVo> findAllBudgeting(PageBVo pageBVo, String id);

    UnionQueryVo unionQuery(String id, UserInfo loginUser);

    void singleAudit(SingleAuditVo singleAuditVo);

    List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo, String sid);

    void addAttribution(String id, String designCategory, String district);

    List<DesignInfo> findDesignAll(PageBVo pageBVo);

    void deleteBudgeting(String id);

    void deleteBudgetingFile(String id);

    void updateCEA(String baseId, String ceaNum);


    List<FileInfo> selectById(String id);

    List<MkyUser> findPreparePeople(String id);
}
