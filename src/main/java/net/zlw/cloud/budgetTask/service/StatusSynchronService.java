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


    public void updateStatusSynchron(String applicationNum,String status){
        BaseProject baseProject = new BaseProject();
        Budgeting budgeting = new Budgeting();
        CostPreparation costPreparation = new CostPreparation();
        FileInfo fileInfo = new FileInfo();
        VeryEstablishment veryEstablishment = new VeryEstablishment();
        DesignInfo designInfo = new DesignInfo();
        if (applicationNum != null){
            baseProject.setStatus(status);
            budgeting.setDelFlag(status);
            costPreparation.setDelFlag(status);
            fileInfo.setStatus(status);
            veryEstablishment.setStatus(status);
            designInfo.setStatus(status);
        }
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
        budgetingDao.updateByPrimaryKeySelective(budgeting);
        costPreparationDao.updateByPrimaryKeySelective(costPreparation);
        fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);
        designInfoMapper.updateByPrimaryKeySelective(designInfo);

    }
}
