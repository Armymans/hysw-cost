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


    public List<SettlementDirectory> getList(String id){
        return settlementDirectoryDao.getList(id);
    }
}
