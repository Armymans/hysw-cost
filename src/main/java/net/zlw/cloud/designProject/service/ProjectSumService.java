package net.zlw.cloud.designProject.service;


import net.zlw.cloud.designProject.mapper.ProjectMapper;
import net.zlw.cloud.designProject.model.CostVo;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.CostVo3;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectSumService {

    @Resource
    private ProjectMapper projectMapper;  //基本信息

    /**
     * 全部项目
     * @return
     */
    public Integer AllprojectCount(){
        CostVo3 costVo3 = projectMapper.AllprojectCount();
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }

    /**
     * 待审核项目
     * @return
     */
    public Integer withAuditCount(){
        CostVo3 costVo3 = projectMapper.withAuditCount();
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }

    /**
     * 已完成的项目
     * @return
     */
    public Integer conductCount(){
        CostVo3 costVo3 = projectMapper.conductCount();
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }

    /**
     * 进行中项目
     * @return
     */
    public Integer completeCount(){
        CostVo3 costVo3 = projectMapper.completeCount();
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }
}
