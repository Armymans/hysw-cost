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
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public SettlementReportText list(String id) {
        SettlementReportText settlementReportText = settlementReportTextDao.getList(id);

        if (settlementReportText != null) {
            List<SettlementAuditReportTextXontent> auditList = settlementAuditReportTextXontentDao.getAuditList(id);
            settlementReportText.setSetAuditLists(auditList);
            List<SettlementReportTextAttachment> arrachmentList = settlementReportTextAttachmentDao.getArrachmentList(id);
            settlementReportText.setSetReportLists(arrachmentList);
            List<SettlementReportTextReductionReasons> reductionList = settlementReportTextReductionReasonsDao.getReductionList(id);
            settlementReportText.setSettReductionLists(reductionList);
        }
        return settlementReportText;
    }

    //检维修
    public SettlementReportText getSettlement(String id) {
        SettlementReportText settlementReportText = settlementReportTextDao.getSettlementReportText(id);
        if (settlementReportText != null) {
            List<SettlementAuditReportTextXontent> auditList = settlementAuditReportTextXontentDao.getAuditList(id);
            List<SettlementReportTextAttachment> arrachmentList = settlementReportTextAttachmentDao.getArrachmentList(id);
            List<SettlementReportTextReductionReasons> reductionList = settlementReportTextReductionReasonsDao.getReductionList(id);
            settlementReportText.setSetAuditLists(auditList);
            settlementReportText.setSetReportLists(arrachmentList);
            settlementReportText.setSettReductionLists(reductionList);
        }
        return settlementReportText;
    }

    //检维修编辑
    public void update(SettlementReportText settlementReportText) {
        if (settlementReportText.getId() != null && !"".equals(settlementReportText.getId())) {

            SettlementAuditReportTextXontent settlementAuditReportTextXontent = new SettlementAuditReportTextXontent();

            List<SettlementAuditReportTextXontent> setAuditLists = settlementReportText.getSetAuditLists();
            List<SettlementReportTextAttachment> setReportLists = settlementReportText.getSetReportLists();
            List<SettlementReportTextReductionReasons> settReductionLists = settlementReportText.getSettReductionLists();
            //审核
            for (SettlementAuditReportTextXontent thisAuditList : setAuditLists) {
                //如果审核id不为空就修改
                if (thisAuditList.getId() != null) {
                    settlementAuditReportTextXontent.setReviewContent(thisAuditList.getReviewContent());
                    settlementAuditReportTextXontentDao.updateByPrimaryKeySelective(settlementAuditReportTextXontent);
                    //如果id不为空但是有审核内容的话就添加
                }else if (thisAuditList.getId() == null && thisAuditList.getReviewContent() != null){
                    settlementAuditReportTextXontent.setId(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date()));
                    settlementAuditReportTextXontent.setSettlementReportTextId(settlementReportText.getId());
                    settlementAuditReportTextXontent.setDelFlag("0");
                    settlementAuditReportTextXontent.setReviewContent(thisAuditList.getReviewContent());
                    settlementAuditReportTextXontentDao.insertSelective(settlementAuditReportTextXontent);
                }
            }
            //附件
            SettlementReportTextAttachment settlementReportTextAttachment = new SettlementReportTextAttachment();
            for (SettlementReportTextAttachment setReportList : setReportLists) {
                //如果附件id不为空就修改
                if (setReportList.getId() != null) {
                    settlementReportTextAttachment.setNameAttachment(setReportList.getNameAttachment());
                    settlementReportTextAttachmentDao.updateByPrimaryKeySelective(settlementReportTextAttachment);
                    //如果id不为空但是有内容的话就添加
                }else if (setReportList.getId() == null && setReportList.getNameAttachment() != null){
                    settlementReportTextAttachment.setId(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date()));
                    settlementReportTextAttachment.setSettlementReportTextId(settlementReportText.getId());
                    settlementReportTextAttachment.setDelFlag("0");
                    settlementReportTextAttachment.setNameAttachment(setReportList.getNameAttachment());
                    settlementReportTextAttachmentDao.insertSelective(settlementReportTextAttachment);
                }
            }
            //审减
            SettlementReportTextReductionReasons settlementReportTextReductionReasons = new SettlementReportTextReductionReasons();
            for (SettlementReportTextReductionReasons thisReason : settReductionLists) {
                //如果审减id不为空就修改
                if (thisReason.getId() != null) {
                    settlementReportTextReductionReasons.setReductionReason(thisReason.getReductionReason());
                    settlementReportTextReductionReasonsDao.updateByPrimaryKeySelective(settlementReportTextReductionReasons);
                    //如果id不为空但是有审核内容的话就添加
                }else if (thisReason.getId() == null && thisReason.getReductionReason() != null){
                    settlementReportTextReductionReasons.setId(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date()));
                    settlementReportTextReductionReasons.setSettlementReportTextId(settlementReportText.getId());
                    settlementReportTextReductionReasons.setDelFlag("0");
                    settlementReportTextReductionReasons.setReductionReason(thisReason.getReductionReason());
                    settlementReportTextReductionReasonsDao.insertSelective(settlementReportTextReductionReasons);
                }
            }
            settlementReportTextDao.updateByPrimaryKeySelective(settlementReportText);
        } else {
            settlementReportTextDao.insertSelective(settlementReportText);
        }
    }
}
