package net.zlw.cloud.librarian.service;

import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.PackageCameMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.PackageCame;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ThoseResponsibleService  {
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private ProjectExplorationMapper projectExplorationMapper;
    @Resource
    private PackageCameMapper packageCameMapper;
    @Resource
    private DesignInfoMapper designInfoMapper;



    public List<ThoseResponsible> findthoseResponsiblAll() {
        List<ThoseResponsible> thoseResponsibles = thoseResponsibleDao.selectAll();
        for (ThoseResponsible thoseResponsible : thoseResponsibles) {
            String personnel = thoseResponsible.getPersonnel();
            String a = "";
            if (personnel!=null){
                String[] split = personnel.split(",");
                for (String s : split) {
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(s);
                    a += memberManage.getMemberName()+",";
                }
                a = a.substring(0,a.length()-1);
                thoseResponsible.setPersonnel(a);
            }
        }
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
                personnel += ","+s+",";
                personnel = personnel.substring(0,personnel.length()-1);
            }
            thoseResponsible.setPersonnel(personnel);
            thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
        }
    }

    public List<MemberManage> findAllTaskManager() {
      return   memberManageDao.findAllTaskManager();
    }

    private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void missionPerson(String missionType, String missionPerson, String id,String baseId) {
        //设计
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if ("1".equals(missionType)){

            baseProject.setDesginStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            Example example = new Example(DesignInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseProject.getId());
            c.andEqualTo("status","0");
            DesignInfo designInfo1 = designInfoMapper.selectOneByExample(example);

            if (designInfo1 == null){
                ProjectExploration projectExploration = new ProjectExploration();
                projectExploration.setId(UUID.randomUUID().toString().replace("-",""));
                projectExploration.setFounderId(missionPerson);
                projectExploration.setStatus("0");
                projectExploration.setCreateTime(s.format(new Date()));
                projectExploration.setBaseProjectId(baseProject.getId());
                projectExplorationMapper.insertSelective(projectExploration);

                PackageCame packageCame = new PackageCame();
                packageCame.setId(UUID.randomUUID().toString().replace("-",""));
                packageCame.setBassProjectId(baseProject.getId());
                packageCame.setFounderId(missionPerson);
                packageCame.setStatus("0");
                packageCame.setCreateTime(s.format(new Date()));
                packageCameMapper.insertSelective(packageCame);

                DesignInfo designInfo = new DesignInfo();
                designInfo.setId(UUID.randomUUID().toString().replace("-",""));
                designInfo.setBaseProjectId(baseProject.getId());
                designInfo.setFounderId(missionPerson);
                designInfo.setStatus("0");
                designInfo.setDesigner(missionPerson);
                designInfo.setOutsource("1");
                designInfo.setCreateTime(s.format(new Date()));
                designInfoMapper.insertSelective(designInfo);
            }else{
                throw new RuntimeException("当前工程设计任务已存在");
            }

        }
    }
}
