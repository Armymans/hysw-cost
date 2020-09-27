package net.zlw.cloud.maintenanceProjectInformation.controller;

import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.service.MaintenanceProjectInformationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Author dell
 * @Date 2020/9/27 11:06
 * @Version 1.0
 */
@RestController
//@RequestMapping("/api/maintenanceProjectInformation")
public class MaintenanceProjectInformationController extends BaseController {
    @Resource
    private MaintenanceProjectInformationService maintenanceProjectInformationService;

    /**
     * 检维修列表数据-全部
     * @param pageRequest
     * @return
     */
//    @PostMapping("/findAllMaintenanceProjectInformation")
    @RequestMapping(value = "/maintenanceProjectInformation/findAllMaintenanceProjectInformation",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllMaintenanceProjectInformation(PageRequest pageRequest){
        System.out.println("PageRequest:"+pageRequest);
        UserInfo loginUser = getLoginUser();
        List<MaintenanceProjectInformation> allMaintenanceProjectInformation = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, loginUser);
        return RestUtil.success(allMaintenanceProjectInformation);
    }


    /**
     * 检维修--删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/deleteMaintenanceProjectInformation",method = {RequestMethod.DELETE},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteMaintenanceProjectInformationController(@RequestParam(name = "id") String id){
        maintenanceProjectInformationService.deleteMaintenanceProjectInformation(id);
        return RestUtil.success("删除成功");
    }

    /**
     * 批量审核
     * @param batchReviewVo
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/batchReview",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        maintenanceProjectInformationService.batchReview(batchReviewVo);
        return RestUtil.success("审核成功");
    }

    /**
     * 新增--提交
     * @param maintenanceProjectInformationVo
     */
    @RequestMapping(value = "/maintenanceProjectInformation/addMaintenanceProjectInformation",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo,String id){
        UserInfo loginUser = getLoginUser();
        System.out.println("user:"+loginUser);
        maintenanceProjectInformationService.addMaintenanceProjectInformation(maintenanceProjectInformationVo,getLoginUser());

    }


    /**
     * 回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/selectMaintenanceProjectInformationById",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectMaintenanceProjectInformationById(@RequestParam(name = "id") String id){
        MaintenanceProjectInformation oneMaintenanceProjectInformation = maintenanceProjectInformationService.selectMaintenanceProjectInformationById(id);
        return RestUtil.success(oneMaintenanceProjectInformation);

    }


    /**
     * 保存
     * @param maintenanceProjectInformationVo
     * @param session
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/saveMaintenanceProjectInformation",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo, HttpSession session){
        maintenanceProjectInformationService.saveMaintenanceProjectInformation(maintenanceProjectInformationVo,session);

//        Object sessionAttribute = session.getAttribute("saveId");
//
//        System.out.println("存储的信息："+sessionAttribute);

        return RestUtil.success("保存成功");
    }

}
