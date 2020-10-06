package net.zlw.cloud.VisaChange.controller;


import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeInfoVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class VisaChangeController extends BaseController {

    @Autowired
    private VisaChangeService vcisService;

    @RequestMapping(value = "/visaChange/findPage",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPage(VisaChangeVo visaChangeVO){
        PageInfo<VisaChangeVo> allPage = vcisService.findAllPage(visaChangeVO, getLoginUser());
        return RestUtil.success(allPage);
    }

    @RequestMapping(value = "/visChange/deleteById",method = {RequestMethod.DELETE},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteById(@RequestParam("id") String id){
        vcisService.delete(id);
        return RestUtil.success("删除成功");
    }

    @RequestMapping(value = "/visChange/selectById",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByid(@RequestParam("id") String id){
        VisaChangeInfoVo visaChangeInfoVo = vcisService.selectById(id);
        return RestUtil.success(visaChangeInfoVo);
    }
    @RequestMapping(value = "/visChange/submitOrSave",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> submitOrSave(VisaChangeInfoVo visaChangeInfoVo){
        vcisService.submitOrSave(visaChangeInfoVo);
        return RestUtil.success("操作成功");
    }




    @RequestMapping(value = "/visChange/approvalProcess",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo) {
        vcisService.approvalProcess(batchReviewVo);
        return RestUtil.success("审核成功");
    }

//    提交
    @RequestMapping(value = "/visChange/add",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(VisaChangeInfoVo visaChangeInfoVo){
        vcisService.addVisChangeVo(visaChangeInfoVo,getLoginUser());
        return RestUtil.success("添加成功");
    }
}
