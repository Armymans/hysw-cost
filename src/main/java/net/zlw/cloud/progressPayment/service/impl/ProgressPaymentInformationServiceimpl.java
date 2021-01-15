package net.zlw.cloud.progressPayment.service.impl;

import net.zlw.cloud.designProject.mapper.ProjectMapper;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.PaymentListVo;
import net.zlw.cloud.progressPayment.model.vo.PaymentVo;
import net.zlw.cloud.progressPayment.service.ProgressPaymentInformationService;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProgressPaymentInformationServiceimpl implements ProgressPaymentInformationService {
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

    @Resource
    private MemberManageDao memberManageDao;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;

    @Override
    public void addPayment(PaymentVo paymentVo) {
        String id = paymentVo.getBaseId();
        ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(progressPaymentInformation.getBaseProjectId());
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ProgressPaymentInformation progressPaymentInformation1 = progressPaymentInformationDao.selectProOne(baseProject.getId());
            ProgressPaymentInformation paymentInfo = paymentVo.getPaymentInfo();
            String nameById = memberManageDao.findNameById(paymentInfo.getFounderId());
            paymentInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            paymentInfo.setFounderId(nameById);
            paymentInfo.setBaseProjectId(baseProject.getId());
            paymentInfo.setChangeNum(Integer.parseInt(paymentVo.getPaymentInfo().getCurrentPeriodAccording()));
            paymentInfo.setCreateTime(data);
        if (progressPaymentInformation1 != null) {
            paymentInfo.setDelFlag("3");
        }else {
            paymentInfo.setDelFlag("0");
        }
        progressPaymentInformationDao.insertSelective(paymentInfo);
    }

    @Override
    public void editPayment(PaymentVo paymentVo) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ProgressPaymentInformation paymentInfo = paymentVo.getPaymentInfo();
        String nameById = memberManageDao.findNameById(paymentInfo.getFounderId());
        paymentInfo.setFounderId(nameById);
        paymentInfo.setUpdateTime(data);
        progressPaymentInformationDao.updateByPrimaryKeySelective(paymentInfo);
    }

    @Override
    public PaymentVo findPayment(String id) {
        PaymentVo paymentVo = new PaymentVo();
        ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);
        if (progressPaymentInformation != null){
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(progressPaymentInformation.getFounderId());
            if (memberManage != null){
                progressPaymentInformation.setFounderId(memberManage.getMemberName());
            }
            paymentVo.setPaymentInfo(progressPaymentInformation);
        }
        return paymentVo;


    }

    @Override
    public void deletePayment(String id) {
        ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);
        progressPaymentInformation.setDelFlag("1");
        progressPaymentInformationDao.updateByPrimaryKeySelective(progressPaymentInformation);
    }

    @Override
    public List<PaymentListVo> paymentList(String id) {
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);
        ArrayList<PaymentListVo> paymentListVos = new ArrayList<>();
        if (paymentInformation != null) {
            List<PaymentListVo> paymentVos = progressPaymentInformationDao.paymentInfoList(paymentInformation.getBaseProjectId());
            if (paymentVos.size() > 0) {
                for (PaymentListVo thisVo : paymentVos) {
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(thisVo.getFounderId());
                    if (memberManage != null) {
                        thisVo.setFounderId(memberManage.getMemberName());
                    }
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(thisVo.getConstructionOrganization());
                    if (constructionUnitManagement!=null){
                        thisVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }
                    CostUnitManagement unitManagement = costUnitManagementMapper.selectByPrimaryKey(thisVo.getNameOfCostUnit());
                    if (unitManagement != null){
                        thisVo.setNameOfCostUnit(unitManagement.getCostUnitName());
                    }
                    paymentListVos.add(thisVo);
                }
            }
        }
        return paymentListVos;
    }
}
