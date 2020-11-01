package net.zlw.cloud.excelLook.service;


import net.zlw.cloud.excelLook.dao.SettlementReportTextDao;
import net.zlw.cloud.excelLook.domain.SettlementReportText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SettlementReportTextService {

    @Autowired
    private SettlementReportTextDao settlementReportTextDao;

    public SettlementReportText list(String id){
        return settlementReportTextDao.getList(id);
    }
}
