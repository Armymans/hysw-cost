package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.excelLook.dao.SettlementDirectoryDao;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            settlementDirectory.setId(UUID.randomUUID().toString().replace("-",""));
            settlementDirectory.setCreateTime(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss" ).format(new Date()));
            settlementDirectoryDao.insertSelective(settlementDirectory);

        }

    }


}
