package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excelLook.dao.FinalReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/***
 * 扉页封面查看
 */
@Service
@Transactional
public class FinalReportService {

    @Autowired
    private FinalReportDao finalReportDao;

    //结算查看
    public FinalReport getList(String id) {
        return finalReportDao.getList(id);
    }

//检维修查看
    public FinalReport getFinalReport(String id){
        return finalReportDao.getFinalReport(id);
    }

//编辑检维修
    public void  updateFinalReport(FinalReport finalReport){
        if (finalReport.getId() !=null && !"".equals(finalReport.getId())){
            finalReportDao.updateByPrimaryKeySelective(finalReport);
        }else {
            finalReportDao.insertSelective(finalReport);
        }
    }
}
