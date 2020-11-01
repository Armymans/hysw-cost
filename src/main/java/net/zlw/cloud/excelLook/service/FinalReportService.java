package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excelLook.dao.FinalReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/***
 * 扉页查看
 */
@Service
@Transactional
public class FinalReportService {

    @Autowired
    private FinalReportDao finalReportDao;

    public FinalReport getList(String id) {
        return finalReportDao.getList(id);
    }
}
