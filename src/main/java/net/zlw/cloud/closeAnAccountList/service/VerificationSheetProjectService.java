package net.zlw.cloud.closeAnAccountList.service;

import net.zlw.cloud.excel.dao.VerificationSheetProjectDao;
import net.zlw.cloud.excel.model.VerificationSheetProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname VerificationSheetProjectService
 * @Description TODO
 * @Date 2020/11/3 10:26
 * @Created by sjf
 */

@Service
@Transactional
public class VerificationSheetProjectService {

    @Autowired
    private VerificationSheetProjectDao verificationSheetProjectDao;

    public List<VerificationSheetProject> getList(String id){
        List<VerificationSheetProject> verificationSheetProjectList = verificationSheetProjectDao.getVerificationSheetProjectList(id);
        return verificationSheetProjectList;
    }
}
