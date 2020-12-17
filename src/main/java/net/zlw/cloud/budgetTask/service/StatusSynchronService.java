package net.zlw.cloud.budgetTask.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname StatusSynchronService
 * @Description TODO
 * @Date 2020/11/6 19:48
 * @Created by sjf
 */
@Service
@Transactional
public class StatusSynchronService {


    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private BudgetingDao budgetingDao;

    @Autowired
    private CostPreparationDao costPreparationDao;

    @Autowired
    private VeryEstablishmentDao veryEstablishmentDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private DesignInfoMapper designInfoMapper;


    public void updateStatusSynchron(String applicationNum, String status) {
        BaseProject baseProject = baseProjectDao.findByApplicationNum(applicationNum);
        if (baseProject != null) {
            Budgeting budgeting = budgetingDao.findByBaseId(baseProject.getId());
            CostPreparation costPreparation = costPreparationDao.findByBaseId(baseProject.getId());
            VeryEstablishment veryEstablishment = veryEstablishmentDao.findByBaseId(baseProject.getId());
            DesignInfo designInfo = designInfoMapper.findByBaseId(baseProject.getId());
            if (budgeting != null) {
                budgeting.setDelFlag(status);
                budgetingDao.updateByPrimaryKeySelective(budgeting);
            }
            if (costPreparation != null) {
                costPreparation.setDelFlag(status);
                costPreparationDao.updateByPrimaryKeySelective(costPreparation);
            }
            if (veryEstablishment != null) {
                veryEstablishment.setStatus(status);
                veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);
            }
            if (designInfo != null) {
                designInfo.setStatus(status);
                designInfoMapper.updateByPrimaryKeySelective(designInfo);
            }
            baseProject.setStatus(status);
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }
    }


}
