package net.zlw.cloud.index.service;

import com.github.pagehelper.PageHelper;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.index.model.vo.ModuleNumber;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProjectOverviewService {
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private MemberManageDao memberManageDao;


    public Integer findCommissionCount(UserInfo loginUser) {
        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        //造价
        Example example1 = new Example(MemberManage.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("depId","2");
        criteria.andEqualTo("depAdmin","1");
        //造价
        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
        Example example2 = new Example(MemberManage.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("depId","2");
        criteria2.andEqualTo("depAdmin","1");
        MemberManage memberManage2 = memberManageDao.selectOneByExample(example2);
        if (loginUser.getId().equals("user305")){
            Integer i = 0;
            List<Budgeting> budgetings = budgetingDao.selectAll();
            for (Budgeting budgeting : budgetings) {
                c.andEqualTo("baseProjectId",budgeting.getId());
                c.andEqualTo("auditorId","3");
                c.andEqualTo("auditResult","0");
                c.andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo!=null){
                    i++;
                }
            }
            return i;
            //设计

        }else if(loginUser.getId().equals(memberManage2.getId())){
            Integer i = 0;
            List<DesignInfo> designInfos = designInfoMapper.selectAll();
            for (DesignInfo designInfo : designInfos) {
                c.andEqualTo("baseProjectId",designInfo.getId());
                c.andEqualTo("auditorId","4");
                c.andEqualTo("auditResult","0");
                c.andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo!=null){
                    i++;
                }
            }
            return i;
        }


        return null;
    }

    public List<ModuleNumber> moduleNumber(List<BaseProject> allBaseProject) {
        ArrayList<ModuleNumber> moduleNumbers = new ArrayList<>();
        for (BaseProject baseProject : allBaseProject) {
            ModuleNumber moduleNumber = new ModuleNumber();
            moduleNumber.setBaseProject(baseProject);
            if (baseProject.getDesginStatus()!=null){
                if (baseProject.getDesginStatus().equals("2")){
                    moduleNumber.setDesignFlow("1");
                }else if(baseProject.getDesginStatus().equals("4")){
                    moduleNumber.setDesignFlow("1,2");
                }
            }
            if (baseProject.getProjectFlow()!=null){
                moduleNumber.setProjectFlow(baseProject.getProjectFlow());
            }
            moduleNumbers.add(moduleNumber);
        }
        return moduleNumbers;
    }
}
