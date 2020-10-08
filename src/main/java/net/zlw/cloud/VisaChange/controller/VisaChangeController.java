package net.zlw.cloud.VisaChange.controller;


import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
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

/***
 * 签证变更
 */
@RestController
public class VisaChangeController extends BaseController {

    @Autowired
    private VisaChangeService vcisService;

    /***
     * 分页查询
     * @param visaChangeVO
     * @return
     */
    @RequestMapping(value = "/visaChange/findPage",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPage(VisaChangeVo visaChangeVO){
        PageInfo<VisaChangeVo> allPage = vcisService.findAllPage(visaChangeVO, getLoginUser());
        return RestUtil.success(allPage);
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/visChange/deleteById",method = {RequestMethod.DELETE},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteById(@RequestParam("id") String id){
        vcisService.delete(id);
        return RestUtil.success("删除成功");
    }

    /***
     * 查看回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/visChange/selectById",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByid(@RequestParam("id") String id){
        VisaChangeInfoVo visaChangeInfoVo = vcisService.selectById(id);
        return RestUtil.success(visaChangeInfoVo);
    }

    /***
     * 编辑提交保存
     * @param visaChangeInfoVo
     * @return
     */
    @RequestMapping(value = "/visChange/submitOrSave",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> submitOrSave(VisaChangeInfoVo visaChangeInfoVo){
        vcisService.submitOrSave(visaChangeInfoVo);
        return RestUtil.success("操作成功");
    }

    /***
     * 批量审核
     * @param batchReviewVo
     * @return
     */
    @RequestMapping(value = "/visChange/approvalProcess",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo) {
        vcisService.approvalProcess(batchReviewVo,getLoginUser());
        return RestUtil.success("审核成功");
    }

    /***
     * 添加提交保存
     * @param visaChangeInfoVo
     * @return
     */
    @RequestMapping(value = "/visChange/add",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(VisaChangeInfoVo visaChangeInfoVo){
        vcisService.addVisChangeVo(visaChangeInfoVo,getLoginUser());
        return RestUtil.success("添加成功");
    }
}
