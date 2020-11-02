package net.zlw.cloud.excelLook.service;


import net.zlw.cloud.excelLook.dao.SettlementReportTextDao;
import net.zlw.cloud.excelLook.domain.SettlementReportText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//正文
@Service
@Transactional
public class SettlementReportTextService {

    @Autowired
    private SettlementReportTextDao settlementReportTextDao;

    //结算
    public SettlementReportText list(String id){
        return settlementReportTextDao.getList(id);
    }
    //检维修
    public SettlementReportText getSettlement(String id){
        return settlementReportTextDao.getSettlementReportText(id);
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
