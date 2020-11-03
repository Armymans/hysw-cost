package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.UnitProjectSummaryDao;
import net.zlw.cloud.excel.model.UnitProjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Classname UnitProjectSummaryService
 * @Description TODO
 * @Date 2020/11/3 9:37
 * @Created by sjf
 */
@Service
@Transactional
public class UnitProjectSummaryService {

    @Autowired
    private UnitProjectSummaryDao unitProjectSummaryDao;

    public List<UnitProjectSummary> unitProjectSummaryList(String id){
        return unitProjectSummaryDao.selectUnitProjectSummaryListById(id);
    }
}
