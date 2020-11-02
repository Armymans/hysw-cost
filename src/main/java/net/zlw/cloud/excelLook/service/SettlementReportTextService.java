package net.zlw.cloud.excelLook.service;


import net.zlw.cloud.excelLook.dao.SettlementAuditReportTextXontentDao;
import net.zlw.cloud.excelLook.dao.SettlementReportTextAttachmentDao;
import net.zlw.cloud.excelLook.dao.SettlementReportTextDao;
import net.zlw.cloud.excelLook.dao.SettlementReportTextReductionReasonsDao;
import net.zlw.cloud.excelLook.domain.SettlementAuditReportTextXontent;
import net.zlw.cloud.excelLook.domain.SettlementReportText;
import net.zlw.cloud.excelLook.domain.SettlementReportTextAttachment;
import net.zlw.cloud.excelLook.domain.SettlementReportTextReductionReasons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

//正文
@Service
@Transactional
public class SettlementReportTextService {

    @Autowired
    private SettlementReportTextDao settlementReportTextDao;

    @Autowired
    private SettlementAuditReportTextXontentDao settlementAuditReportTextXontentDao;

    @Autowired
    private SettlementReportTextAttachmentDao settlementReportTextAttachmentDao;

    @Autowired
    private SettlementReportTextReductionReasonsDao settlementReportTextReductionReasonsDao;

    //结算
    public SettlementReportText list(String id){
        return settlementReportTextDao.getList(id);
    }
    //检维修
    public SettlementReportText getSettlement(String id){

        List<SettlementAuditReportTextXontent> auditList = settlementAuditReportTextXontentDao.getAuditList(id);
        List<SettlementReportTextAttachment> arrachmentList = settlementReportTextAttachmentDao.getArrachmentList(id);
        List<SettlementReportTextReductionReasons> reductionList = settlementReportTextReductionReasonsDao.getReductionList(id);
        SettlementReportText settlementReportText = settlementReportTextDao.getSettlementReportText(id);
        settlementReportText.setSetAuditLists(auditList);
        settlementReportText.setSetReportLists(arrachmentList);
        settlementReportText.setSettReductionLists(reductionList);

        return settlementReportText;

    }
    //检维修编辑
    public void update(SettlementReportText settlementReportText){
        if (settlementReportText.getId() !=null && !"".equals(settlementReportText.getId())){
            settlementReportTextDao.updateByPrimaryKeySelective(settlementReportText);
        }else {
            settlementReportTextDao.insertSelective(settlementReportText);
        }
    }
}
