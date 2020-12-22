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
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.whDesignTask.dao.DockLogDao;
import net.zlw.cloud.whDesignTask.model.DockLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

    @Autowired
    private MemberService memberService;
    @Autowired
    private DockLogDao dockLogDao;


    public void updateStatusSynchron(String applicationNum, String status , HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date());
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
            // 日志
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            DockLog dockLog = new DockLog();
            dockLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
//            dockLog.setName(); // 操作人
            dockLog.setType("5"); // 状态更新
            dockLog.setContent(applicationNum+","+status);
            dockLog.setDoTime(data);
            dockLog.setDoObject(applicationNum);
            dockLog.setStatus("0");
            String ip = memberService.getIp(request);
            dockLog.setIp(ip);
            dockLogDao.insertSelective(dockLog);
        }
    }


}
