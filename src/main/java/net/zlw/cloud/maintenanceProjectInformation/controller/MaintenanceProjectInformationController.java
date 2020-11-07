package net.zlw.cloud.maintenanceProjectInformation.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationReturnVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.service.MaintenanceProjectInformationService;
import org.springframework.validation.BindingResult;
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
     *
     * @param pageRequest
     * @return
     */
//    @PostMapping("/findAllMaintenanceProjectInformation")
//
    @RequestMapping(value = "/maintenanceProjectInformation/findAllMaintenanceProjectInformation", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> findAllMaintenanceProjectInformation(PageRequest pageRequest) {
//        pageRequest.setUid(getLoginUser().getId());
        System.out.println("PageRequest:" + pageRequest);
        PageInfo<MaintenanceProjectInformationReturnVo> allMaintenanceProjectInformation = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        return net.zlw.cloud.common.RestUtil.page(allMaintenanceProjectInformation);
    }


    /**
     * 检维修--删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/deleteMaintenanceProjectInformation", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> deleteMaintenanceProjectInformationController(@RequestParam(name = "id") String id) {
        maintenanceProjectInformationService.deleteMaintenanceProjectInformation(id);
        return RestUtil.success("删除成功");
    }

    /**
     * 批量审核
     *
     * @param batchReviewVo
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/batchReview", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> batchReview(BatchReviewVo batchReviewVo) {
        maintenanceProjectInformationService.batchReview(batchReviewVo);
        return RestUtil.success("审核成功");
    }

    /**
     * 新增--提交
     * 提交：把数据保存到数据库，得到一个审核人 id
     *
     * @param maintenanceProjectInformationVo
     */
    @RequestMapping(value = "/maintenanceProjectInformation/addMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
//        UserInfo loginUser = getLoginUser();
//        System.out.println("user:"+loginUser);
        maintenanceProjectInformationService.addMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("新增提交成功");
    }


    /**
     * 根据id 回显数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/selectMaintenanceProjectInformationById", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectMaintenanceProjectInformationById(String id,String userId) {
        MaintenanceVo maintenanceVo = maintenanceProjectInformationService.selectMaintenanceProjectInformationById(id,userId,getLoginUser());
        return RestUtil.success(maintenanceVo);
    }


    /**
     * 新增-保存
     *
     * @param maintenanceProjectInformationVo
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/saveMaintenanceProjectInformation", method = {RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
//        UserInfo loginUser = getLoginUser();
        maintenanceProjectInformationService.saveMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());

        return RestUtil.success("保存成功");
    }

    /**
     * 编辑--提交
     * 提交：把数据保存到数据库，得到一个审核人 id
     *
     * @param maintenanceProjectInformationVo
     */
    @RequestMapping(value = "/maintenanceProjectInformation/updateMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
        UserInfo loginUser = getLoginUser();
        System.out.println("user:" + loginUser);
        maintenanceProjectInformationService.updateMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("编辑成功");
    }


    @RequestMapping(value = "/maintenanceProjectInformation/updateSaveMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateSaveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
        UserInfo loginUser = getLoginUser();
        System.out.println("user:" + loginUser);
        maintenanceProjectInformationService.updateSaveMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("编辑成功");
    }


}
