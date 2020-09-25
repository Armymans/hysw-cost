package net.zlw.cloud.budgeting.service;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.budgeting.model.vo.BudgetingVo;

public interface BudgetingService {
    void addBudgeting(BudgetingVo budgetingVo,UserInfo loginUser);

    BudgetingVo selectBudgetingById(String id);

    void updateBudgeting(BudgetingVo budgetingVo);

    void batchReview(BatchReviewVo batchReviewVo);
}
