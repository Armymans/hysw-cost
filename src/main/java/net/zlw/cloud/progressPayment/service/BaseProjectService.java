package net.zlw.cloud.progressPayment.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;

import java.util.List;

public interface BaseProjectService {

    void addProgress(BaseProjectVo baseProject, UserInfo loginUser);

    BaseProjectVo seachProgressById(String id);

    void updateProgress(BaseProjectVo baseProjectVo);

    List<BaseProject> findBaseProject(String name);

    List<BaseProject> findAllBaseProject();

    void batchReview(BatchReviewVo batchReviewVo);

    BaseProject findById(String id);

    NumberVo NumberItems();

    List<ProgressListVo> searchAllProgress(PageVo pageVo);

    void deleteProgress(String id);

    List<VisaBaseProjectVo> selectByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo);
}
