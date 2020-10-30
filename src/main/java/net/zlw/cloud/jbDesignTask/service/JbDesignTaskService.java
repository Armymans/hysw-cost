package net.zlw.cloud.jbDesignTask.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.jbDesignTask.dao.DiameterInfoDao;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;
import net.zlw.cloud.jbDesignTask.domain.FileInfos;
import net.zlw.cloud.jbDesignTask.domain.vo.JbBudgetVo;
import net.zlw.cloud.jbDesignTask.domain.vo.JbDesignVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 江北设计
     * @param designVo
     * @param account
     */
    public void getDesignEngineering(JbDesignVo designVo, String account) {

        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());

        if (designVo != null) {
            //项目基本表数据
            BaseProject baseProject = new BaseProject();
            if (designVo.getProjectId() != null) {
                baseProject.setId(UUID.randomUUID().toString().replace("-", ""));
                baseProject.setApplicationNum(designVo.getProjectId());
                baseProject.setDistrict(designVo.getAddress());
                baseProject.setArea(designVo.getArea());
                baseProject.setCustomerName(designVo.getCustomerName());
                baseProject.setCustomerAddress(designVo.getCustomerAddress());
                baseProject.setDesignCategory(designVo.getDesignCategory());
                baseProject.setProjectNature(designVo.getProjectNature());
                baseProject.setContactNumber(designVo.getContactNumber());
                baseProject.setCustomerPhone(designVo.getCustomerPhone());
                baseProject.setCustomerEmail(designVo.getCustomerEmail());
                baseProject.setAgent(designVo.getAgent());
                baseProject.setCeaNum(designVo.getCeaNum());
                baseProject.setDelFlag("0");
                baseProject.setCreateTime(date);
                baseProject.setUpdateTime(date);
                baseProject.setAmountPaid(designVo.getAmountPaid());
            }
            baseProjectDao.insertSelective(baseProject);

            //设计表信息
            DesignInfo designInfo = new DesignInfo();
            if (designVo.getTakeTime() != null) {
                designInfo.setId(designVo.getId());
                designInfo.setDesigner(designVo.getDesigner());
                designInfo.setTakeTime(designVo.getTakeTime());
                designInfo.setFounderId(designVo.getFounderId());
                designInfo.setCreateTime(designVo.getCreateTime());
                designInfo.setRemark(designVo.getRemarks());
                designInfo.setStatus("0");
            }
            designInfoMapper.insertSelective(designInfo);

            //勘探表信息
            ProjectExploration projectExploration = new ProjectExploration();
            if (designVo.getExplorationIdeal() != null) {
                projectExploration.setId(UUID.randomUUID().toString().replace("-", ""));
                projectExploration.setExplorationIdeal(designVo.getExplorationIdeal());
                projectExploration.setExplorationTime(designVo.getExplorationTime());
                projectExploration.setCreateTime(date);
                projectExploration.setUpdateTime(date);
                projectExploration.setStatus("0");
            }
            projectExplorationMapper.insertSelective(projectExploration);

            //水表信息
            DiameterInfo diameterInfo = new DiameterInfo();
            List<DiameterInfo> diameterInfos = designVo.getDiameterInfos();
            for (DiameterInfo thisInfo : diameterInfos) {
                if (thisInfo.getId() != null) {
                    diameterInfo.setId(thisInfo.getId());
                    diameterInfo.setProjectId(thisInfo.getProjectId());
                    diameterInfo.setDiameterMeter(thisInfo.getDiameterMeter());
                    diameterInfo.setCreateTime(date);
                    diameterInfo.setUpdateTime(date);
                    diameterInfo.setStatus("0");
                }
                diameterInfoDao.insertSelective(diameterInfo);
            }

            //上传信息集合
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = designVo.getFileInfos();
            for (FileInfos thisFileInfos : fileInfos) {
                if (thisFileInfos.getId() != null) {

                    fileInfo.setId(thisFileInfos.getId());
                    fileInfo.setPlatCode(thisFileInfos.getProjectId());
                    fileInfo.setFileName(thisFileInfos.getOpinionsFileName());
                    fileInfo.setCreateTime(thisFileInfos.getOpinionsUpTime());
                    fileInfo.setUserId(thisFileInfos.getOpinionsUpBy());
                    fileInfo.setFilePath(thisFileInfos.getOpinionsLink());
                    fileInfo.setType("scxxjh");
                    fileInfo.setStatus("0");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }
        }

    }

    /***
     * 江北造价
     * @param budgetVo
     * @param account
     */
    public void getBudgetEngineering(JbBudgetVo budgetVo, String account) {
//        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        if (budgetVo != null) {
            //预算信息
            Budgeting budgeting = new Budgeting();
            if (budgetVo.getId() != null) {

                budgeting.setId(budgetVo.getId());
                budgeting.setBaseProjectId(budgetVo.getProjectId());
                budgeting.setBudgetingPeople(budgetVo.getBudgetingPeople());
                budgeting.setReceiptTime(budgetVo.getReceiptTime());
                budgeting.setFounderId(account);
                budgeting.setCreateTime(budgetVo.getProjectName());
                budgeting.setRemarkes(budgetVo.getRemark());
                String amountCost = budgetVo.getAmountCost();
                BigDecimal cost = new BigDecimal(amountCost);
                budgeting.setAmountCost(cost);
                budgeting.setSureResult(budgetVo.getSureResult());
                budgeting.setSureMan(budgetVo.getSureMan());
                budgeting.setDelFlag("0");
            }
            budgetingDao.insertSelective(budgeting);


            //上传附件信息
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = budgetVo.getFileInfos();
            for (FileInfos thisInfo : fileInfos) {
                if (thisInfo.getId() != null) {
                    fileInfo.setId(thisInfo.getId());
                    fileInfo.setPlatCode(thisInfo.getProjectId());
                    fileInfo.setFileName(thisInfo.getOpinionsFileName());
                    fileInfo.setCreateTime(thisInfo.getOpinionsUpTime());
                    fileInfo.setUserId(thisInfo.getOpinionsUpBy());
                    fileInfo.setFilePath(thisInfo.getOpinionsLink());
                    fileInfo.setStatus("0");
                    fileInfo.setType("jbbzscfjxx");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }
        }
    }
}
