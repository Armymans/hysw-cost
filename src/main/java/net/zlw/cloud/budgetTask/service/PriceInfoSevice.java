package net.zlw.cloud.budgetTask.service;

import net.zlw.cloud.budgetTask.domain.LabelMeansList;
import net.zlw.cloud.budgetTask.domain.vo.PriceControlVo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
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

    public void getPriceInfo(PriceControlVo priceControlVo,String account){
        //设置时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        FileInfo fileInfo = new FileInfo();
        if (priceControlVo != null){
            //控价编制
            VeryEstablishment veryEstablishment = new VeryEstablishment();
            if (priceControlVo.getApplicationNum() != null){
                veryEstablishment.setId(priceControlVo.getApplicationNum());
                veryEstablishment.setTotalPriceControlLabel(priceControlVo.getTotalPriceControlLabel());
                BigDecimal vatAmount = new BigDecimal(priceControlVo.getLabelVatAmount());
                veryEstablishment.setVatAmount(vatAmount);
                veryEstablishment.setPricingTogether(priceControlVo.getPriceControlCompilers());
                veryEstablishment.setEstablishmentTime(priceControlVo.getPriceControlTime());
                veryEstablishment.setRemarkes(priceControlVo.getPriceControlRemark());
                veryEstablishment.setPriceResult(priceControlVo.getPriceExamineResult());
                veryEstablishment.setPriceOpinion(priceControlVo.getPriceExamineOpinion());
                veryEstablishment.setPriceExaminer(priceControlVo.getPriceExaminer());
                veryEstablishment.setPriceExamineTime(priceControlVo.getPriceExamineTime());
                veryEstablishment.setStatus(priceControlVo.getStatus());
                veryEstablishment.setCreateTime(format);
                veryEstablishment.setUpdateTime(format);
            }
            veryEstablishmentDao.insertSelective(veryEstablishment);

            //控价编制附件资料
            List<LabelMeansList> labelMeansList = priceControlVo.getLabelMeansList();
            for (LabelMeansList thisLable : labelMeansList) {
                if (thisLable.getLabelFileName() != null){
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setName(thisLable.getLabelMeansName());
                    fileInfo.setFileName(thisLable.getLabelFileName());
                    fileInfo.setPlatCode(priceControlVo.getApplicationNum());
                    fileInfo.setFilePath(thisLable.getLableFileDrawing());
                    fileInfo.setCreateTime(format);
                    fileInfo.setUpdateTime(format);
                    fileInfo.setStatus("0");
                    fileInfo.setType("kjbzfjzl");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }

        }
    }
}
