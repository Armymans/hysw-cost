package net.zlw.cloud.progressPayment.service;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.*;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BaseProjectService {

    void addProgress(BaseProjectVo baseProject, UserInfo loginUser, HttpServletRequest request);

    BaseProjectVo seachProgressById(String id, UserInfo userInfo, String visaNum);

    BaseProjectVo editProgressById(String id, UserInfo userInfo, String visaNum);

    void updateProgress(BaseProjectVo baseProjectVo ,UserInfo loginUser,HttpServletRequest request);

    void updateProgressPayment(BaseProjectVo baseProjectVo);

    List<BaseProject> findBaseProject(String name);

    List<BaseProject> findAllBaseProject(pageVo pageVo);

    void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser,HttpServletRequest request);

    BaseProject findById(String id);

    NumberVo NumberItems();

    PageInfo<ProgressListVo> searchAllProgress(PageVo pageVo);
//    PageInfo<ProgressListVo> searchAllProgressList(PageVo pageVo);

    void deleteProgress(String id,UserInfo loginUser,HttpServletRequest request);

    List<VisaBaseProjectVo> selectByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo);

    void updateProject(BaseProject baseProject);

    List<BaseProject> findByBuildingProject(String id);

    BaseProject findByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo);

    BaseProject findByBuilding(String id);

    List<AuditChekedVo> auditChek(String id);

    List<AuditChekedVo> auditDesginChek(String id);

    List<AuditChekedVo> auditChangeDesginChek(String id);

    BaseProject findBaseProjectById(String id);

    List<AuditChekedVo> auditMaintenanceChek(String id);

    List<AuditChekedVo> auditAgainMaintenanceChek(String id);

    List<ProgressPaymentInformation> findTotalList(String baseId);

    TotalVo findTotal(String baseId);

    List<AuditChekedVo> auditChekAccount();

    List<AuditChekedVo> findcheckAll(String num, String id);

    void editOutSourceMoney(String id, String outSourceMoney);

    void accomplish(String ids, UserInfo loginUser);

    void sendBack(String id,String auditOpinion);
}
