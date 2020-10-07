package net.zlw.cloud.warningDetails.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.warningDetails.model.WarningDetailAndAuditInfoVo;
import net.zlw.cloud.warningDetails.model.WarningDetails;
import net.zlw.cloud.warningDetails.service.WarningDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**  风险预警说明回显和编辑
 * Created by xulei on 2020/9/21.  /hysw/cost/api/api/warningDetails/updateWarningDetails
 *
 */
@RestController
@RequestMapping("/api/warningDetails")
public class WarningDetailsController extends BaseController {

     @Resource
     private WarningDetailsService warningDetailsService;


    /**
     * 更新预警信息(说明)/保存审核信息
     * @param id
     * @return
     */
   @RequestMapping(value = "/warningFindById", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> warningFindById(String id, String instructions){
       warningDetailsService.warningFindById(id,instructions,getLoginUser());
       return RestUtil.success("保存成功");
   }

    /**
     * 查询详情信息
     * @param id
     * @param userId
     * @return
     */
    @RequestMapping(value = "/detailById", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public  Map<String,Object> detailById(String id,String userId) {
        //获取预警信息  获取审核信息
        WarningDetails warningDetails = warningDetailsService.detailById(id, userId);
        return RestUtil.success(warningDetails);
    }

    @RequestMapping(value = "/updateWarningDetails", method = {RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public  Map<String,Object> updateWarningDetails(WarningDetailAndAuditInfoVo warningDetailAndAuditInfoVo) {
        warningDetailsService.updateWarningDetails(warningDetailAndAuditInfoVo);
        return RestUtil.success();
    }


}







