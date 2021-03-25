package net.zlw.cloud.buildingProject.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.buildingProject.model.vo.BaseVo;
import net.zlw.cloud.buildingProject.model.vo.MemberManageVo;
import net.zlw.cloud.buildingProject.model.vo.PageBaseVo;
import net.zlw.cloud.buildingProject.model.vo.ProVo;
import net.zlw.cloud.buildingProject.service.BuildingProjectService;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
            buildingProjectService.deleteBuilding(id,getLoginUser(),request);
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

    /**
        * @Author sjf
        * @Description // 工程项目列表
        * @Date 10:17 2020/12/17
        * @Param
        * @return
     **/
    @RequestMapping(value = "/selectBaseProjectFindAll",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBaseProjectFindAll(PageBaseVo pageBaseVo){
        List<ProVo> findList = buildingProjectService.selectBaseProjectFindAll(pageBaseVo,getLoginUser().getId());
        PageInfo<ProVo> proVoPageInfo = new PageInfo<>(findList);
        return RestUtil.page(proVoPageInfo);
    }
    /**
        * @Author sjf
        * @Description //工程列表模糊查询
        * @Date 10:51 2020/12/17
        * @Param
        * @return
     **/
    @RequestMapping(value = "/selectLikeBaseProject",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectLikeBaseProject(PageBaseVo pageBaseVo){

        Page page = new Page();
        List<ProVo> proVos = buildingProjectService.selectBaseProjectFindAll(pageBaseVo, getLoginUser().getId());
        PageInfo<ProVo> proVoPageInfo = new PageInfo<>(proVos);
        page.setPageNum(proVoPageInfo.getPageNum());
        page.setPageSize(proVoPageInfo.getPageSize());
        page.setTotalCount(proVoPageInfo.getTotal());
        page.setData(proVoPageInfo.getList());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("table1",page);
        return RestUtil.success(map);
    }

    /**
     *
     * @Description //工程项目删除
     * @Date 10:40 2021/2/23
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteBaseProject", method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> deleteBaseProject(String id){
        try {
            buildingProjectService.deleteBaseProject(id, getLoginUser().getId(), request);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    /**
     * 查询设计业务员
     * @return
     */
    @RequestMapping(value = "/memberManage/memberManagefindUnAdmin2", method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> memberManagefindUnAdmin2(){
        List<MemberManageVo> memberManageVoList = buildingProjectService.memberManagefindUnAdmin2();
        return RestUtil.success(memberManageVoList);
    }
}
