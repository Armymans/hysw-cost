package net.zlw.cloud.progressPayment.service;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;

import java.util.List;

public interface BaseProjectService {

    void addProgress(BaseProjectVo baseProject, UserInfo loginUser);

    BaseProjectVo seachProgressById(String id,UserInfo userInfo);

    void updateProgress(BaseProjectVo baseProjectVo ,UserInfo loginUser);
    void updateProgressPayment(BaseProjectVo baseProjectVo);

    List<BaseProject> findBaseProject(String name);

    List<BaseProject> findAllBaseProject(pageVo pageVo);

    void batchReview(BatchReviewVo batchReviewVo);

    BaseProject findById(String id);

    NumberVo NumberItems();

    PageInfo<ProgressListVo> searchAllProgress(PageVo pageVo);
//    PageInfo<ProgressListVo> searchAllProgressList(PageVo pageVo);

    void deleteProgress(String id);

    List<VisaBaseProjectVo> selectByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo);

    void updateProject(BaseProject baseProject);

    List<BaseProject> findByBuildingProject(String id);

    BaseProject findByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo);

    BaseProject findByBuilding(String id);

    List<AuditChekedVo> auditChek(String id);

    List<AuditChekedVo> auditDesginChek(String id);

    List<AuditChekedVo> auditChangeDesginChek(String id);

    BaseProject findBaseProjectById(String id);
}
