package net.zlw.cloud.VisaChange.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.common.RestUtil;
import net.tec.cloud.common.web.MediaTypes;

import net.zlw.cloud.VisaChange.model.vo.VisaChangeStatisticVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 签证变更
 */
@RestController
public class VisaChangeController extends BaseController {

    @Autowired
    private VisaChangeService vcisService;

    @Autowired
    private BaseProjectService baseProjectService;



    //查询所有
    @RequestMapping(value = "/visachange/findAllVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllVisa(PageVo pageVo){
      List<VisaChangeListVo> list =  vcisService.findAllVisa(pageVo);
      return RestUtil.success(list);
    }

    //新增签证变更
    @RequestMapping(value = "/visachange/addVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addVisa(VisaChangeVo visaChangeVo){
        vcisService.addVisa(visaChangeVo,getLoginUser().getId());
        return RestUtil.success();
    }

    //根据id查询签证变更
    @RequestMapping(value = "/visachange/findVisaById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findVisaById(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "visaNum") String visaNum){
       VisaChangeVo visaChangeVo =  vcisService.findVisaById(baseId,visaNum,getLoginUser().getId());
       return RestUtil.success(visaChangeVo);
    }

    //编辑
    @RequestMapping(value = "/visachange/updateVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateVisa(VisaChangeVo visaChangeVo){
        vcisService.updateVisa(visaChangeVo,getLoginUser().getId());
        return RestUtil.success();
    }

    //删除
    @RequestMapping(value = "/visachange/deleteVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteVisa(){

    }
















}
