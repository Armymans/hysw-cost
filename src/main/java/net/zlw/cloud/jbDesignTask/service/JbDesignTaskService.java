package net.zlw.cloud.jbDesignTask.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.jbDesignTask.dao.DiameterInfoDao;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;
import net.zlw.cloud.jbDesignTask.domain.vo.*;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class JbDesignTaskService {

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private DesignInfoMapper designInfoMapper;

    @Autowired
    private ProjectExplorationMapper projectExplorationMapper;

    @Autowired
    private DiameterInfoDao diameterInfoDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private BudgetingDao budgetingDao;


    /***
     * 设计报装
     * @param jbDesignVoF
     */
    public void getDesignEngineering(JbDesignVoF jbDesignVoF) {

        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        JbDesignVo designVo = jbDesignVoF.getJbDesignVo();
        if (designVo != null) {
            //项目基本表数据
            BaseProject baseProject = new BaseProject();
            if (designVo.getProject_id() != null) {
                baseProject.setId(designVo.getId());
                baseProject.setProjectId(designVo.getProject_id());
                baseProject.setSite(designVo.getAddress());
                baseProject.setProjectName(designVo.getProject_name());
                baseProject.setCustomerName(designVo.getCustomer_name());
                baseProject.setCustomerAddress(designVo.getCustomer_address());
                baseProject.setDesignCategory(designVo.getDesign_category());
                baseProject.setProjectNature(designVo.getProject_nature());
                baseProject.setContactNumber(designVo.getContact_number());
                baseProject.setCustomerPhone(designVo.getCustomer_phone());
                baseProject.setAgent(designVo.getAgent());
                baseProject.setCeaNum(designVo.getCea_num());
                baseProject.setDesginStatus("4");
                baseProject.setDistrict("4");
                baseProject.setDelFlag("0");
                baseProject.setCreateTime(date);
                baseProject.setUpdateTime(date);
                baseProject.setAmountPaid(designVo.getAmount_paid());
                baseProjectDao.insertSelective(baseProject);
            }

            //设计表信息
            DesignInfo designInfo = new DesignInfo();
            if (designVo.getTake_time() != null) {
                designInfo.setId(designVo.getId());
                designInfo.setBaseProjectId(designVo.getId());
                designInfo.setDesigner(designVo.getDesigner());
                designInfo.setTakeTime(designVo.getTake_time());
                designInfo.setFounderId(designVo.getFounder_id());
                designInfo.setCreateTime(designVo.getCreate_time());
                designInfo.setRemark(designVo.getRemarks());
                designInfo.setStatus("0");
                designInfoMapper.insertSelective(designInfo);
            }

            //勘探表信息
            ProjectExploration projectExploration = new ProjectExploration();
            if (designVo.getExploration_ideal() != null) {
                projectExploration.setId(designVo.getId());
                projectExploration.setBaseProjectId(designInfo.getId());
                projectExploration.setExplorationIdeal(designVo.getExploration_ideal());
                projectExploration.setExplorationTime(designVo.getExploration_time());
                projectExploration.setCreateTime(date);
                projectExploration.setUpdateTime(date);
                projectExploration.setStatus("0");
            projectExplorationMapper.insertSelective(projectExploration);
            }

            //水表信息
            DiameterInfo diameterInfo = new DiameterInfo();
            List<DiameterInfos> diameterInfos = designVo.getDiameterInfo();
            for (DiameterInfos thisInfo : diameterInfos) {
                if (thisInfo.getId() != null) {
                    diameterInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    diameterInfo.setProjectId(thisInfo.getProject_id());
                    diameterInfo.setType(thisInfo.getType());
                    diameterInfo.setDiameterMeter(thisInfo.getDiameter_meter());
                    diameterInfo.setCreateTime(date);
                    diameterInfo.setUpdateTime(date);
                    diameterInfo.setStatus("0");
                    diameterInfoDao.insertSelective(diameterInfo);
                }
            }

            //上传信息集合
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = designVo.getFileInfo();
            for (FileInfos thisFileInfos : fileInfos) {
                if (thisFileInfos.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setPlatCode(thisFileInfos.getProject_id());
                    fileInfo.setFileName(thisFileInfos.getOpinions_file_name());
                    fileInfo.setCreateTime(thisFileInfos.getOpinions_up_time());
                    fileInfo.setUserId(thisFileInfos.getOpinions_up_by());
                    fileInfo.setFilePath(thisFileInfos.getOpinions_link());
                    fileInfo.setType(thisFileInfos.getType());
                    fileInfo.setStatus("0");
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }
        }

    }

    /***
     * 江北造价
     * @param
     * @param
     */
    public void getBudgetEngineering(JbBudgetVoF jbBudgetVoF) {
        JbBudgetVo budgetVo = jbBudgetVoF.getJbBudgetVo();
        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("project_id",budgetVo.getProject_id())
                .andEqualTo("delFlag","0");
        BaseProject baseProject = baseProjectDao.selectOneByExample(example);
        baseProject.setBudgetStatus("4");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        if (budgetVo != null) {
            //预算信息
            Budgeting budgeting = new Budgeting();
            if (budgetVo.getId() != null) {

                budgeting.setId(budgetVo.getId());
                budgeting.setBaseProjectId(budgetVo.getProject_id());
                budgeting.setBudgetingPeople(budgetVo.getBudgeting_people());
                budgeting.setReceiptTime(budgetVo.getReceipt_time());
                budgeting.setFounderId(budgetVo.getFounder_id());
                budgeting.setRemarkes(budgetVo.getRemark());
                budgeting.setCreateTime(budgetVo.getProject_name());
                String amountCost = budgetVo.getAmount_cost();
                BigDecimal cost = new BigDecimal(amountCost);
                budgeting.setAmountCost(cost);
                budgeting.setSureResult(budgetVo.getSure_result());
                budgeting.setSureMan(budgetVo.getSure_man());
                budgeting.setDelFlag("0");
            }
            budgetingDao.insertSelective(budgeting);


            //上传附件信息
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = budgetVo.getFileInfo();
            for (FileInfos thisInfo : fileInfos) {
                if (thisInfo.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setPlatCode(thisInfo.getProject_id());
                    fileInfo.setFileName(thisInfo.getOpinions_file_name());
                    fileInfo.setCreateTime(thisInfo.getOpinions_up_time());
                    fileInfo.setUserId(thisInfo.getOpinions_up_by());
                    fileInfo.setFilePath(thisInfo.getOpinions_link());
                    fileInfo.setStatus("0");
                    fileInfo.setType("jbbzscfjxx");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }
        }
    }

    public void updateBudgetAmount(AmountVo amountVo){
        String budgetAmount = amountVo.getBudget_amount();
        if (amountVo.getProject_id() != null && !"".equals(amountVo.getProject_id())){
            Example example = new Example(Budgeting.class);
            example.createCriteria().andEqualTo("baseProjectId",amountVo.getProject_id())
                                    .andEqualTo("delFlag","0");
            Budgeting budgeting = budgetingDao.selectOneByExample(example);
            if (budgeting != null){
                budgeting.setAmountCost(new BigDecimal(budgetAmount));
                budgetingDao.updateByPrimaryKeySelective(budgeting);
            }

        }
    }

    // 更新cea接口
    public void updateCea(CEAVo ceaVo) {
        if (ceaVo != null){
            Example example = new Example(BaseProject.class);
            example.createCriteria().andEqualTo("project_id",ceaVo.getProject_id())
                                    .andEqualTo("delFlag","0");
            BaseProject baseProject = baseProjectDao.selectOneByExample(example);
            if (baseProject != null){
                baseProject.setCeaNum(ceaVo.getCea());
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }
        }
    }
}
