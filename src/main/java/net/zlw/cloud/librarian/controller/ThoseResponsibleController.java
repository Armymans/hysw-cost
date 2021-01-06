package net.zlw.cloud.librarian.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.librarian.service.ThoseResponsibleService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ThoseResponsibleController {
    @Resource
    private ThoseResponsibleService thoseResponsibleService;

    //查询所有
    @RequestMapping(value = "/thoseResponsibl/findthoseResponsiblAll", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findthoseResponsiblAll(){
      List<ThoseResponsible> list =  thoseResponsibleService.findthoseResponsiblAll();
      return RestUtil.success(list);
    }
    //添加人员
    @RequestMapping(value = "/thoseResponsibl/addPerson", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addPerson(@RequestParam(name = "remeberId") String remeberId){
        thoseResponsibleService.addPerson(remeberId);
        return RestUtil.success();
    }
    //查询任务责任人
    @RequestMapping(value = "/thoseResponsibl/findAllTaskManager", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllTaskManager(){
       List<MemberManage> list =  thoseResponsibleService.findAllTaskManager();
       return RestUtil.success(list);
    }

}
