package net.zlw.cloud.buildingProject.service;

import net.zlw.cloud.buildingProject.mapper.BuildingProjectMapper;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/11 11:48
 **/
@Service
public class BuildingProjectService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BuildingProjectMapper buildingProjectMapper;

    @Autowired
    private BaseProjectService baseProjectService;

    @Autowired
    private BaseProjectDao baseProjectDao;


    /**
     * @Author Armyman
     * @Description //查询可被选为合并项目的建设项目
     * @Date 11:56 2020/10/11
     **/
    public List<BuildingProject> findBuildingProject() {
        return buildingProjectMapper.findBuildingProject();
    }

    /**
     * @Author Armyman
     * @Description //建设项目合并
     * @Date 14:23 2020/10/11
     **/
    public void buildingProjectMerge(String ids, String id) {
        //建设项目id
        BuildingProject buildingProject = buildingProjectMapper.selectById(id);
        if (buildingProject != null) {
            buildingProject.setMergeFlag("1");
            buildingProjectMapper.updateByPrimaryKeySelective(buildingProject);
        }
        //工程项目ids
        String[] split = ids.split(",");
        for (String s : split) {
            BaseProject baseProject = baseProjectService.findById(s);
            BaseProject baseProject1 = baseProjectDao.findBaseProject(s);
            if (baseProject1 != null){
                 throw new RuntimeException("合并项目已存在,请重新选择");
            }else if (baseProject != null ) {
                baseProject.setBuildingProjectId(id);
                baseProject.setMergeFlag("0");
                baseProject.setUpdateTime(sdf.format(new Date()));
                baseProjectService.updateProject(baseProject);
            }
//            else{
//                new RuntimeException("项目信息错误，请确认后合并");
//            }
        }
    }

    /**
     * @Author Armyman
     * @Description // 建设项目还原
     * @Date 11:56 2020/10/11
     **/
    public void buildingProjectReduction(String id) {
        BuildingProject buildingProject = buildingProjectMapper.selectById(id);
        if (buildingProject != null) {
            buildingProject.setMergeFlag("2");
            buildingProjectMapper.updateByPrimaryKeySelective(buildingProject);
        }
        //工程项目ids
        List<BaseProject> baseProject = baseProjectService.findByBuildingProject(id);
        if (baseProject.size() > 0) {
            for (BaseProject project : baseProject) {
//                project.setId(UUID.randomUUID().toString().replace("-",""));
                project.setBuildingProjectId(null);
                project.setMergeFlag("1");
                project.setUpdateTime(sdf.format(new Date()));
//                baseProjectService.updateProject(project);
//                baseProjectDao.updateByPrimaryKey(project);
                baseProjectDao.updateByPrimaryKeySelective(project);
            }
        }
    }


}
