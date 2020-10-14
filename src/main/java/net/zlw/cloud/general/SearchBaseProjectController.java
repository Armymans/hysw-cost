package net.zlw.cloud.general;

import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.vo.LoginUser;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.*;
import sun.security.action.GetLongAction;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController

public class SearchBaseProjectController {
    @Resource
    private BaseProjectService baseProjectService;

    //模糊搜索项目编号
    @GetMapping("/{name}")
    public List<BaseProject> findBaseProject(@PathVariable(name = "name") String name){
      List<BaseProject> list =  baseProjectService.findBaseProject(name);
      return list;
    }
    //查询所有工程项目
    @RequestMapping(value = "/baseProject/findAll",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    @GetMapping("/findAll")
    public Map<String,Object> findAllBaseProject(){

        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject();
        return RestUtil.success(allBaseProject);
    }

    @RequestMapping(value = "/baseProject/findById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    @GetMapping("/findById/{id}")
    public Map<String,Object> findById(@RequestParam(name = "id") String id){
        BaseProject byId = baseProjectService.findById(id);
        return RestUtil.success(byId);
    }

    /**
     * @Author Armyman
     * @Description //签证变更，进度款支付，跟踪审计
     * @Date 22:17 2020/10/14
     **/
    @RequestMapping(value = "/budgetingProject/findById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findById2(@RequestParam(name = "id") String id){
        BaseProject byId = baseProjectService.findByBuilding(id);
        return RestUtil.success(byId);
    }

}
