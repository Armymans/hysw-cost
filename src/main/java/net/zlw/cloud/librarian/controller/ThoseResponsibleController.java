package net.zlw.cloud.librarian.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.librarian.service.ThoseResponsibleService;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ThoseResponsibleController extends BaseController {
    @Resource
    private ThoseResponsibleService thoseResponsibleService;
    @Resource
    private BaseProjectDao baseProjectDao;

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
    //分配任务
    @RequestMapping(value = "/thoseResponsibl/allocatingTask", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> allocatingTask(@RequestParam(name = "missionType") String missionType,@RequestParam(name = "missionPerson") String missionPerson,@RequestParam(name = "baseId") String baseId){
        thoseResponsibleService.missionPerson(missionType,missionPerson,getLoginUser().getId(),baseId);
        return RestUtil.success();
    }
    //添加工程
    @RequestMapping(value = "/baseProject/addBaseproject", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBaseproject(BaseProject baseProject){
        baseProject.setId(UUID.randomUUID().toString().replace("-",""));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        baseProject.setCreateTime(simpleDateFormat.format(new Date()));
        baseProject.setStatus("0");
        baseProject.setFounderId(getLoginUser().getId());
        baseProjectDao.insertSelective(baseProject);
        return RestUtil.success();
    }

}
