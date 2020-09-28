package net.zlw.cloud.VisaChange.controller;


import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VisaChangeController extends BaseController {

    @Autowired
    private VisaChangeService vcisService;

    @RequestMapping(value = "/visaChange/findPage",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPage(VisaChangeVO visaChangeVO){
        List<VisaChangeVO> allPage = vcisService.findAllPage(visaChangeVO,getLoginUser());

        return RestUtil.success(allPage);
    }

    @RequestMapping(value = "/visChange/deleteById",method = {RequestMethod.DELETE},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteById(@RequestParam("id") String id){
        vcisService.delete(id);
        return RestUtil.success("删除成功");
    }

    @RequestMapping(value = "/visChange/selectById",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByid(@RequestParam("id") String id){
        VisaChange visaChange = vcisService.selectById(id);
        return RestUtil.success(visaChange);
    }

    @RequestMapping(value = "/visChange/approvalProcess",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo) {
        vcisService.approvalProcess(batchReviewVo);
        return RestUtil.success("审核成功");

    }
}
