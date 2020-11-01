package net.zlw.cloud.excelLook.service;


import net.zlw.cloud.excelLook.dao.SettlementCoverDao;
import net.zlw.cloud.excelLook.domain.SettlementCover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * 结算报告封面查看
 */
@Service
@Transactional
public class SettlementCoverService {

    @Autowired
    private SettlementCoverDao settlementCoverDao;


    public List<SettlementCover> settlementCoversList(String id){
        return  settlementCoverDao.settletmentCoverList(id);

    }

}
