package net.zlw.cloud.warningDetails.controller;

import net.zlw.cloud.warningDetails.model.WarningDetails;
import net.zlw.cloud.warningDetails.service.WarningDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**  风险预警说明回显和编辑
 * Created by xulei on 2020/9/21.
 */
@RestController
@RequestMapping("/api/warningDetails")
public class WarningDetailsController {

     @Resource
     private WarningDetailsService warningDetailsService;


    /**
     * 更新预警信息(说明)/保存审核信息
     * @param id
     * @return
     */
   @GetMapping("/warningFindById")
    public WarningDetails warningFindById(String id, String instructions, String userId , String companyId){
       return warningDetailsService.warningFindById(id,instructions,userId,companyId);
   }

    /**
     * 查询详情信息
     * @param id
     * @param userId
     * @return
     */
    @GetMapping("/detailById")
    public WarningDetails detailById(String id,String userId) {
        //获取预警信息  获取审核信息
        return warningDetailsService.detailById(id,userId);
    }


}







