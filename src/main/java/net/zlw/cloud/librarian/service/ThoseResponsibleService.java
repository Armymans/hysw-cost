package net.zlw.cloud.librarian.service;

import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ThoseResponsibleService  {
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;
    @Resource
    private MemberManageDao memberManageDao;

    public List<ThoseResponsible> findthoseResponsiblAll() {
        List<ThoseResponsible> thoseResponsibles = thoseResponsibleDao.selectAll();
        return thoseResponsibles;
    }

    public void addPerson(String remeberId) {
        ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("1");
        String personnel = thoseResponsible.getPersonnel();
        if (personnel == null){
            String[] split = remeberId.split(",");
            String rId = "";
            for (String s : split) {
                rId += s+",";
            }
            rId = rId.substring(0,rId.length()-1);
            thoseResponsible.setPersonnel(rId);
            thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
        }else{
            String[] split = remeberId.split(",");
            for (String s : split) {
                personnel += s+",";
            }
            personnel = personnel.substring(0,personnel.length()-1);
            thoseResponsible.setPersonnel(personnel);
            thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
        }
    }

    public List<MemberManage> findAllTaskManager() {
      return   memberManageDao.findAllTaskManager();
    }
}
