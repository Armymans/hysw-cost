package net.zlw.cloud.budgeting.service;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.designProject.model.DesignInfo;

import java.util.List;

public interface BudgetingService {
    void addBudgeting(BudgetingVo budgetingVo,UserInfo loginUser);

    BudgetingVo selectBudgetingById(String id, UserInfo loginUser);

    void updateBudgeting(BudgetingVo budgetingVo);

    void batchReview(BatchReviewVo batchReviewVo);

    void intoAccount(String ids);

    List<BudgetingListVo> findAllBudgeting(PageBVo pageBVo);

    UnionQueryVo unionQuery(String id, UserInfo loginUser);

    void singleAudit(SingleAuditVo singleAuditVo);

    List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo, String sid);

    void addAttribution(String id, String designCategory, String district);

    List<DesignInfo> findDesignAll(PageBVo pageBVo);

    void deleteBudgeting(String id);

    void deleteBudgetingFile(String id);
}
