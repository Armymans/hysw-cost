package net.zlw.cloud.statisticAnalysis.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.designProject.mapper.LastSettlementReviewMapper;
import net.zlw.cloud.designProject.mapper.SettlementAuditInformationMapper;
import net.zlw.cloud.designProject.model.BaseProject;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.statisticAnalysis.model.StatisticAnalysis;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class StatusticAnalysisService {
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private LastSettlementReviewMapper lastSettlementReviewMapper;
    @Resource
    private SettlementAuditInformationMapper settlementAuditInformationMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private BaseProjectDao baseProjectDao;


    public StatisticAnalysis findAnalysis(pageVo pageVo) {
        Date date = new Date();
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        if (pageVo.getDistrict()!=null && !pageVo.getDistrict().equals("")){
            c.andEqualTo("district",pageVo.getDistrict());
        }

        return null;

    }
}
