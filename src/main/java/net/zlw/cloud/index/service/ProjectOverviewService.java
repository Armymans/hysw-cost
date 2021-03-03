package net.zlw.cloud.index.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.index.model.vo.ModuleNumber;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
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
//        Example example = new Example(AuditInfo.class);
//        Example.Criteria c = example.createCriteria();
//        //造价
//        Example example1 = new Example(MemberManage.class);
//        Example.Criteria criteria = example1.createCriteria();
//        criteria.andEqualTo("depId","2");
//        criteria.andEqualTo("depAdmin","1");
//        //造价
//        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//        Example example2 = new Example(MemberManage.class);
//        Example.Criteria criteria2 = example2.createCriteria();
//        criteria2.andEqualTo("depId","2");
//        criteria2.andEqualTo("depAdmin","1");
//        MemberManage memberManage2 = memberManageDao.selectOneByExample(example2);

//        if (loginUser.getId().equals("user305")){
//            Integer i = 0;
//            List<Budgeting> budgetings = budgetingDao.selectAll();
//            for (Budgeting budgeting : budgetings) {
//                c.andEqualTo("baseProjectId",budgeting.getId());
//                c.andEqualTo("auditorId","3");
//                c.andEqualTo("auditResult","0");
//                c.andEqualTo("auditType","1");
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//                if (auditInfo!=null){
//                    i++;
//                }
//            }
//            return i;
//            //设计
//
//        }else if(loginUser.getId().equals(memberManage2.getId())){
//            Integer i = 0;
//            List<DesignInfo> designInfos = designInfoMapper.selectAll();
//            for (DesignInfo designInfo : designInfos) {
//                c.andEqualTo("baseProjectId",designInfo.getId());
//                c.andEqualTo("auditorId","4");
//                c.andEqualTo("auditResult","0");
//                c.andEqualTo("auditType","1");
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//                if (auditInfo!=null){
//                    i++;
//                }
//            }
//            return i;
//        }

        //获取当前登陆人id
        String userId = loginUser.getId();
        //根据登入人查找该负责人的待审项目个数
        Example example = new Example(AuditInfo.class);
        example.createCriteria()
                .andEqualTo("status","0")
                .andEqualTo("auditorId",userId)
                .andEqualTo("auditResult","0");
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
        if(auditInfos.size()>0){
            return auditInfos.size();
        }
        return 0;
    }

    public List<ModuleNumber> moduleNumber(List<BaseProject> allBaseProject) {
        ArrayList<ModuleNumber> moduleNumbers = new ArrayList<>();

        for (BaseProject baseProject : allBaseProject) {
            ModuleNumber moduleNumber = new ModuleNumber();
            moduleNumber.setBaseProject(baseProject);
            if (baseProject.getDesginStatus()!=null){
                if (!baseProject.getDesginStatus().equals("4")){
                    moduleNumber.setDesignFlow("1");
                }else {
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

    public List<ModuleNumber> moduleNumber2(List<BaseProject> allBaseProject) {
        ArrayList<ModuleNumber> moduleNumbers = new ArrayList<>();
        for (BaseProject baseProject : allBaseProject) {
            ModuleNumber moduleNumber = new ModuleNumber();
            moduleNumber.setBaseProject(baseProject);
            //新进度阶段
            String newflow = "";
            //如果当前造价流程不为空
            if (baseProject.getProjectFlow()!=null){
                String[] strings = baseProject.getProjectFlow().split(",");
                //如果当前项目 设计完成 则记录为 2
                if (baseProject.getDesginStatus().equals("4")){
                    newflow = newflow+"2";
                }
                for (String s : strings) {
                    //如果当前项目处于预算编制
                    if(s.equals("2")){
                        newflow = newflow + ",2";
                    }
                    //如果当前项目处于跟踪审计
                    if(s.equals("3")){
                        newflow = newflow + ",3";
                    }
                    //如果当前项目处于进度款支付
                    if(s.equals("4")){
                        newflow = newflow + ",4";
                    }
                    //如果当前项目签证变更
                    if(s.equals("5")){
                        newflow = newflow + ",5";
                    }
                    //如果当前项目结算编制
                    if(s.equals("6")){
                        newflow = newflow + ",6";
                    }
                }
                moduleNumber.setProjectFlow(newflow);
            }
            moduleNumbers.add(moduleNumber);
        }
        return moduleNumbers;
    }
}
