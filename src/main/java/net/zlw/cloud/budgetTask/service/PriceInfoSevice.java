package net.zlw.cloud.budgetTask.service;

import net.zlw.cloud.budgetTask.domain.LabelMeansList;
import net.zlw.cloud.budgetTask.domain.vo.PriceControlVo;
import net.zlw.cloud.budgetTask.domain.vo.PriceControlVoF;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.whDesignTask.dao.DockLogDao;
import net.zlw.cloud.whDesignTask.model.DockLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Classname PriceInfoSevice
 * @Description TODO
 * @Date 2020/11/6 17:59
 * @Created by sjf
 */

@Service
@Transactional
public class PriceInfoSevice {

    @Autowired
    private AuditInfoDao auditInfoDao;

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
    private MemberService memberService;

    @Autowired
    private DockLogDao dockLogDao;

    public void getPriceInfo(PriceControlVoF priceControlVoF, HttpServletRequest request) {
        PriceControlVo priceControlVo = priceControlVoF.getPriceControl();
        //设置时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (priceControlVo != null) {
            //BaseProject baseProject = baseProjectDao.selectByPrimaryKey(priceControlVo.getApplication_num());
            Example example1 = new Example(BaseProject.class);
            example1.createCriteria().andEqualTo("applicationNum", priceControlVo.getApplication_num());
            BaseProject baseProject = baseProjectDao.selectOneByExample(example1);

            DockLog dockLog = new DockLog();

            if (baseProject != null){
                Example example2 = new Example(VeryEstablishment.class);
                example2.createCriteria().andEqualTo("baseProjectId", baseProject.getId());
                VeryEstablishment establishment = veryEstablishmentDao.selectOneByExample(example2);

                if (establishment == null){
                    Budgeting byBaseId = budgetingDao.findByBaseId(baseProject.getId());
                    if (byBaseId != null) {
                        //控价编制
                        if (priceControlVo.getApplication_num() != null) {
                            VeryEstablishment veryEstablishment = new VeryEstablishment();
                            veryEstablishment.setId(UUID.randomUUID().toString());
                            veryEstablishment.setTotalPriceControlLabel(priceControlVo.getTotal_price_control_label());
                            BigDecimal vatAmount = new BigDecimal(priceControlVo.getLabel_vat_amount());
                            veryEstablishment.setVatAmount(vatAmount);
                            veryEstablishment.setPricingTogether(priceControlVo.getPrice_control_compilers());
                            veryEstablishment.setEstablishmentTime(priceControlVo.getPrice_control_time());
                            veryEstablishment.setBaseProjectId(baseProject.getId());
                            veryEstablishment.setBudgetingId(byBaseId.getId());
                            veryEstablishment.setRemarkes(priceControlVo.getPrice_control_remark());
                            veryEstablishment.setPriceResult(priceControlVo.getPrice_examine_result());
                            veryEstablishment.setPriceOpinion(priceControlVo.getPrice_examine_opinion());
                            veryEstablishment.setPriceExaminer(priceControlVo.getPrice_examiner());
                            veryEstablishment.setPriceExamineTime(priceControlVo.getPrice_examine_time());
                            veryEstablishment.setStatus(priceControlVo.getStatus());
                            veryEstablishment.setDelFlag("0");
                            veryEstablishment.setCreateTime(format);
                            veryEstablishment.setUpdateTime(format);
                            veryEstablishmentDao.insertSelective(veryEstablishment);
                        }

                        //控价编制附件资料
                        List<LabelMeansList> labelMeansList = priceControlVo.getLabelMeansList();
                        if (labelMeansList != null && labelMeansList.size() > 0) {
                            for (LabelMeansList thisLable : labelMeansList) {
                                FileInfo fileInfo = new FileInfo();
                                if (thisLable.getLabel_file_name() != null) {
                                    fileInfo.setId(thisLable.getId());
                                    fileInfo.setName(thisLable.getLabel_file_name());
                                    fileInfo.setFileName(thisLable.getLabel_file_name());
                                    fileInfo.setPlatCode(byBaseId.getId());
                                    fileInfo.setFilePath(thisLable.getLable_file_drawing());
                                    fileInfo.setCreateTime(format);
                                    fileInfo.setUpdateTime(format);
                                    fileInfo.setStatus("0");
                                    fileInfo.setType("kjbzfjzl");
                                    fileInfoMapper.insertSelective(fileInfo);
                                }
                            }
                        }
                    }
                }

                dockLog.setContent(priceControlVoF.toString());

            } else {
                dockLog.setContent("对接过来一个没有基础信息的芜湖控价数据" + priceControlVoF.toString());
            }


            dockLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            dockLog.setName(priceControlVoF.getAccount()); // 操作人
            dockLog.setType("6"); // 控价

            dockLog.setDoTime(format);
            dockLog.setDoObject(priceControlVo.getApplication_num());
            dockLog.setStatus("0");
            String ip = memberService.getIp(request);
            dockLog.setIp(ip);
            dockLogDao.insertSelective(dockLog);

        }
    }


}
