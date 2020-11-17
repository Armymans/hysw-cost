package net.zlw.cloud.general;

import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.*;

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
    public Map<String,Object> findAllBaseProject(pageVo pageVo){

        List<BaseProject> allBaseProject = baseProjectService.findAllBaseProject(pageVo);
        return RestUtil.success(allBaseProject);
    }

    @RequestMapping(value = "/baseProject/findById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    @GetMapping("/findById/{id}")
    public Map<String,Object> findById(@RequestParam(name = "id",required = false) String id){
        BaseProject byId = null;
        try {
            byId = baseProjectService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            RestUtil.error();
        }
        return RestUtil.success(byId);
    }

    @RequestMapping(value = "/baseProject/findBaseProjectById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    @GetMapping("/findById/{id}")
    public Map<String,Object> findBaseProjectById(@RequestParam(name = "id",required = false) String id){
        BaseProject byId = null;
        try {
            byId = baseProjectService.findBaseProjectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            RestUtil.error();
        }
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
    //审核卡片
    @RequestMapping(value = "/review/auditChek",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> auditChek(@RequestParam(name = "id") String id){
        List<AuditChekedVo> list = baseProjectService.auditChek(id);
        return RestUtil.success(list);
    }

    /**
     * 设计审核卡片
     * @param id
     * @return
     */
    @RequestMapping(value = "/review/auditDesginChek",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> auditDesginChek(@RequestParam(name = "id") String id){
        List<AuditChekedVo> list = baseProjectService.auditDesginChek(id);
        return RestUtil.success(list);
    }

    /**
     * 设计变更审核卡片
     * @param id
     * @return
     */
    @RequestMapping(value = "/review/auditChangeDesginChek",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> auditChangeDesginChek(@RequestParam(name = "id") String id){
        List<AuditChekedVo> list = baseProjectService.auditChangeDesginChek(id);
        return RestUtil.success(list);
    }
}
