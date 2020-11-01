package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.excel.dao.SummaryUnitsDao;
import net.zlw.cloud.excel.model.SummaryUnits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SummaryUnitsService {



    @Autowired
    private SummaryUnitsDao summaryUnitsDao;

    public List<SummaryUnits> summaryUnitsList(String id){
       return summaryUnitsDao.findSumMaryUnitsList(id);
    }


}
