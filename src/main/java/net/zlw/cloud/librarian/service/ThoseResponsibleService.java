package net.zlw.cloud.librarian.service;

import net.zlw.cloud.VisaChange.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.PackageCameMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.PackageCame;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ThoseResponsibleService  {
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private ProjectExplorationMapper projectExplorationMapper;
    @Resource
    private PackageCameMapper packageCameMapper;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private SurveyInformationDao surveyInformationDao;
    @Resource
    private CostPreparationDao costPreparationDao;
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;
    @Resource
    private VisaChangeMapper visaChangeMapper;
    @Resource
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;




    public List<ThoseResponsible> findthoseResponsiblAll() {
        List<ThoseResponsible> thoseResponsibles = thoseResponsibleDao.selectAll();
        for (ThoseResponsible thoseResponsible : thoseResponsibles) {
            String personnel = thoseResponsible.getPersonnel();
            String a = "";
            if (personnel!=null){
                String[] split = personnel.split(",");
                for (String s : split) {
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(s);
                    a += memberManage.getMemberName()+",";
                }
                a = a.substring(0,a.length()-1);
                thoseResponsible.setPersonnel(a);
            }
        }
        return thoseResponsibles;
    }

    public void addPerson(String remeberId) {
        ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("1");
        String personnel = thoseResponsible.getPersonnel();
        if (personnel == null){
            String[] split = remeberId.split(",");
            String rId = "";
            for (String s : split) {
                rId += s+",";
            }
            rId = rId.substring(0,rId.length()-1);
            thoseResponsible.setPersonnel(rId);
            thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
        }else{
            String[] split = remeberId.split(",");
            for (String s : split) {
                personnel += ","+s+",";
                personnel = personnel.substring(0,personnel.length()-1);
            }
            thoseResponsible.setPersonnel(personnel);
            thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
        }
    }

    public List<MemberManage> findAllTaskManager() {
      return   memberManageDao.findAllTaskManager();
    }

    private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void missionPerson(String missionType, String missionPerson, String id,String baseId) {
        //设计
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if ("1".equals(missionType)){

            baseProject.setDesginStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            //设计
            Example example = new Example(DesignInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseProject.getId());
            c.andEqualTo("status","0");
            DesignInfo designInfo1 = designInfoMapper.selectOneByExample(example);


            if (designInfo1 == null){
                DesignInfo designInfo = new DesignInfo();
                designInfo.setId(UUID.randomUUID().toString().replace("-",""));
                designInfo.setBaseProjectId(baseProject.getId());
                designInfo.setFounderId(missionPerson);
                designInfo.setStatus("0");
                designInfo.setDesigner(missionPerson);
                designInfo.setOutsource("1");
                designInfo.setCreateTime(s.format(new Date()));
                designInfoMapper.insertSelective(designInfo);

                ProjectExploration projectExploration = new ProjectExploration();
                projectExploration.setId(UUID.randomUUID().toString().replace("-",""));
                projectExploration.setFounderId(missionPerson);
                projectExploration.setStatus("0");
                projectExploration.setCreateTime(s.format(new Date()));
                projectExploration.setBaseProjectId(designInfo.getId());
                projectExplorationMapper.insertSelective(projectExploration);

                PackageCame packageCame = new PackageCame();
                packageCame.setId(UUID.randomUUID().toString().replace("-",""));
                packageCame.setBassProjectId(designInfo.getId());
                packageCame.setFounderId(missionPerson);
                packageCame.setStatus("0");
                packageCame.setCreateTime(s.format(new Date()));
                packageCameMapper.insertSelective(packageCame);

            }else{
                throw new RuntimeException("当前工程设计任务已存在");
            }

            //预算
        }else if("2".equals(missionType)){

            baseProject.setBudgetStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            //预算
            Example example1 = new Example(Budgeting.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId",baseProject.getId());
            c1.andEqualTo("delFlag","0");
            Budgeting budgeting = budgetingDao.selectOneByExample(example1);

            if (budgeting == null){



                Budgeting budgeting1 = new Budgeting();
                budgeting1.setId(UUID.randomUUID().toString().replace("-",""));
                budgeting1.setCreateTime(s.format(new Date()));
                budgeting1.setBaseProjectId(baseProject.getId());
                budgeting1.setFounderId(missionPerson);
                budgeting1.setBudgetingPeople(missionPerson);
                budgeting1.setDelFlag("0");
                budgeting1.setOutsourcing("2");
                budgetingDao.insertSelective(budgeting1);

                SurveyInformation surveyInformation = new SurveyInformation();
                surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
                surveyInformation.setBudgetingId(budgeting1.getId());
                surveyInformation.setBaseProjectId(baseProject.getId());
                surveyInformation.setCreateTime(s.format(new Date()));
                surveyInformation.setFounderId(missionPerson);
                surveyInformation.setDelFlag("0");
                surveyInformationDao.insertSelective(surveyInformation);

                CostPreparation costPreparation = new CostPreparation();
                costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
                costPreparation.setBaseProjectId(baseProject.getId());
                costPreparation.setBudgetingId(budgeting1.getId());
                costPreparation.setCostTogether(missionPerson);
                costPreparation.setCreateTime(s.format(new Date()));
                costPreparation.setFounderId(missionPerson);
                costPreparation.setDelFlag("0");
                costPreparationDao.insertSelective(costPreparation);

                VeryEstablishment veryEstablishment = new VeryEstablishment();
                veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
                veryEstablishment.setBaseProjectId(baseProject.getId());
                veryEstablishment.setPricingTogether(missionPerson);
                veryEstablishment.setBudgetingId(budgeting1.getId());
                veryEstablishment.setFounderId(missionPerson);
                veryEstablishment.setCreateTime(s.format(new Date()));
                veryEstablishment.setDelFlag("0");
                veryEstablishmentDao.insertSelective(veryEstablishment);

            }else{
                throw new RuntimeException("当前工程预算任务已存在");
            }
            //进度款
        } else if("3".equals(missionType)){

            //签证变更
        }else if("4".equals(missionType)){
            Example example = new Example(VisaChange.class);
            Example.Criteria cc = example.createCriteria();
            cc.andEqualTo("baseProjectId",baseProject.getId());
            cc.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
            if (visaChanges == null || visaChanges.size() == 0){

            }
        }
    }
}
