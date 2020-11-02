package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.excelLook.dao.SettlementDirectoryDao;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * 目录查看
 */
@Service
public class SettlementDirectoryService {

    @Autowired
    private SettlementDirectoryDao settlementDirectoryDao;

//结算查看
    public List<SettlementDirectory> getList(String id){

        return settlementDirectoryDao.getList(id);
    }
//    检维修查看
    public List<SettlementDirectory> getMantenceList(String id){
        return settlementDirectoryDao.getMantenceList(id);
    }

    public void updateSettlementDirectory(SettlementDirectory settlementDirectory){
        if (settlementDirectory.getId() != null && !"".equals(settlementDirectory.getId())){
            settlementDirectoryDao.updateByPrimaryKeySelective(settlementDirectory);
        }else {
            settlementDirectoryDao.insertSelective(settlementDirectory);

        }

    }


}
