package net.zlw.cloud.buildingProject.controller;

import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.buildingProject.service.BuildingProjectService;
import net.zlw.cloud.clearProject.model.vo.ClearProjectVo;
import net.zlw.cloud.common.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author Armyman
 * @Description 建设项目
 * @Date 2020/10/11 11:50
 **/
@RestController
@RequestMapping(value="/buildingProject")
public class BuildingProjectController extends BaseController {

    @Autowired
    private BuildingProjectService buildingProjectService;

    /**
     * @Author Armyman
     * @Description //查询可被选为合并项目的建设项目
     * @Date 11:56 2020/10/11
     **/
    @RequestMapping(value = "/findBuildingProject",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBuildingProject(){
        List<BuildingProject> buildingProject = buildingProjectService.findBuildingProject();
        return RestUtil.success(buildingProject);
    }

    /**
     * @Author Armyman
     * @Description // 工程项目合并
     * @Date 11:56 2020/10/11
     **/
//    hysw/cost/api/buildingProject/findBuildingProject
    @RequestMapping(value = "/buildingProjectMerge",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildingProjectMerge(String ids,String id,String code){
       buildingProjectService.buildingProjectMerge(ids,id,code);
        return RestUtil.success();
    }

    /**
     * @Author Armyman
     * @Description // 建设项目还原
     * @Date 11:56 2020/10/11
     **/
    @RequestMapping(value = "/buildingProjectReduction",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildingProjectReduction(String id){
       buildingProjectService.buildingProjectReduction(id);
        return RestUtil.success();
    }




}
