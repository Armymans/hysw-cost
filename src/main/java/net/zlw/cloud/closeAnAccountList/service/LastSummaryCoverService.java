package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.LastSummaryCoverDao;
import net.zlw.cloud.excel.model.LastSummaryCover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname LastSummaryCoverService
 * @Description TODO
 * @Date 2020/11/3 9:15
 * @Created by sjf
 */

@Service
public class LastSummaryCoverService {

    @Autowired
    private LastSummaryCoverDao lastSummaryCoverDao;

    public LastSummaryCover getLastSummaryCover(String id){
         return lastSummaryCoverDao.selectLastSummaryCoverById(id);
    }


}
