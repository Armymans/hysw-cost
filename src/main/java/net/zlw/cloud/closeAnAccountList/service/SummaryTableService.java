package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.SummaryTableDao;
import net.zlw.cloud.excel.model.SummaryTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname SummaryTableService
 * @Description TODO
 * @Date 2020/11/3 10:01
 * @Created by sjf
 */

@Service
@Transactional
public class SummaryTableService {

    @Autowired
    private SummaryTableDao summaryTableDao;


    public List<SummaryTable> summaryTableList(String id){
        return summaryTableDao.selectSummaryTableById(id);
    }
}
