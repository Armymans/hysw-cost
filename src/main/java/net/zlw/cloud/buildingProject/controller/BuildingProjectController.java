package net.zlw.cloud.buildingProject.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.buildingProject.model.vo.BaseVo;
import net.zlw.cloud.buildingProject.service.BuildingProjectService;
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
    @RequestMapping(value = "/findBuildingProject",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBuildingProject(){
        List<BuildingProject> buildingProject = buildingProjectService.findBuildingProject();
        return RestUtil.success(buildingProject);
    }

    /**
     * @Author Armyman
     * @Description // 工程项目合并
     * @Date 11:56 2020/10/11
     *
     **/
//    hysw/cost/api/buildingProject/findBuildingProject
    @RequestMapping(value = "/buildingProjectMerge",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> buildingProjectMerge(String ids,String id){
        try {
            buildingProjectService.buildingProjectMerge(ids,id);
        }catch (Exception e){
           return RestUtil.error(e.getMessage());
        }
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

    /**
        * @Author sjf
        * @Description // 建设项目删除
        * @Date 14:34 2020/12/2
        * @Param
        * @return
     **/
    @RequestMapping(value = "/deleteBuilding",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteBuilding(String id){
        try {
            buildingProjectService.deleteBuilding(id);
        } catch (Exception e) {
//            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();

    }

    /**
     * @Author sjf
     * @Description // 建设项目查看 - 工程项目
     * @Date 14:34 2020/12/2
     * @Param
     * @return
     **/
    @RequestMapping(value = "/selectBaseProjectList",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBaseProjectList(String id){
      List<BaseVo> findList = buildingProjectService.selectBaseProjectList(id);
      return RestUtil.success(findList);
    }
}
