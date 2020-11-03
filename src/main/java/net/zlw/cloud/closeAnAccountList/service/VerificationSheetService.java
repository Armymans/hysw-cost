package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.VerificationSheetDao;
import net.zlw.cloud.excel.dao.VerificationSheetProjectDao;
import net.zlw.cloud.excel.model.VerificationSheet;
import net.zlw.cloud.excel.model.VerificationSheetProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname VerificationSheetService
 * @Description TODO
 * @Date 2020/11/3 10:20
 * @Created by sjf
 */

@Service
@Transactional
public class VerificationSheetService {

    @Autowired
    private VerificationSheetDao verificationSheetDao;

    @Autowired
    private VerificationSheetProjectDao verificationSheetProjectDao;

    public VerificationSheet verificationSheetList(String id){
        VerificationSheet verificationSheet = verificationSheetDao.selectVerificationSheetById(id);
        if (verificationSheet != null){
            List<VerificationSheetProject> sheetProjectList = verificationSheetProjectDao.getVerificationSheetProjectList(id);
            verificationSheet.setSheetProjects(sheetProjectList);
        }
        return verificationSheet;
    }
}
