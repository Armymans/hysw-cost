package net.zlw.cloud.progressPayment.service;

import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;

import java.util.List;

public interface BaseProjectService {

    void addProgress(BaseProjectVo baseProject);

    BaseProjectVo seachProgressById(String id);

    void updateProgress(BaseProjectVo baseProjectVo);

    List<BaseProject> findBaseProject(String name);

    List<BaseProject> findAllBaseProject();

    void batchReview(BatchReviewVo batchReviewVo);

    BaseProject findById(String id);

    NumberVo NumberItems();
}
