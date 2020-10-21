package net.zlw.cloud.VisaChange.controller;


import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeInfoVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeStatisticVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /***
     * 分页查询
     * @param visaChangeVO
     * @return
     */
    @RequestMapping(value = "/visaChange/findPage",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPage(VisaChangeVo visaChangeVO){
        PageInfo<VisaChangeVo> allPage = vcisService.findAllPage(visaChangeVO, getLoginUser());

        return net.zlw.cloud.common.RestUtil.page(allPage);
    }

    /***
     * 删除
     * @param id  base id
     * @return
     */
    @RequestMapping(value = "/visaChange/deleteById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteById(@RequestParam("id") String id){
        vcisService.delete(id);
        return RestUtil.success("删除成功");
    }

    /***
     * 查看回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/visaChange/selectById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByid(@RequestParam("id") String id){
        VisaChangeInfoVo visaChangeInfoVo = vcisService.selectById(id);
        return RestUtil.success(visaChangeInfoVo);
    }

    /***
     * 查看回显统计列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/visaChange/selectListById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectListById(@RequestParam("id") String id){
        List<VisaChangeStatisticVo> visaChangeInfoVo = vcisService.selectListById(id);
        return RestUtil.success(visaChangeInfoVo);
    }

    /***
     * 编辑提交保存
     * @param visaChangeInfoVo
     * @return
     */
    @RequestMapping(value = "/visaChange/submitOrSave",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> submitOrSave(VisaChangeInfoVo visaChangeInfoVo){
        System.err.println("第一"+visaChangeInfoVo);
        vcisService.submitOrSave(visaChangeInfoVo);
        return RestUtil.success("操作成功");
    }


//     /hysw/cost/api/visChange/selectById
    /***
     * 批量审核
     * @param batchReviewVo
     * @return
     */
    @RequestMapping(value = "/visaChange/approvalProcess",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo) {
        vcisService.approvalProcess(batchReviewVo,getLoginUser());
        return RestUtil.success("审核成功");
    }

    /***
     * 添加提交保存
     * @param visaChangeInfoVo
     * @return
     */
    @RequestMapping(value = "/visaChange/add",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(VisaChangeInfoVo visaChangeInfoVo){
        vcisService.addVisChangeVo(visaChangeInfoVo,getLoginUser());
        return RestUtil.success("添加成功");
    }
//    /hysw/cost/api/visaChange/findPage
    /***
     * 签证变更所选项目接口
     * @param visaBaseProjectVo
     * @return
     */
    @RequestMapping(value = "/visaChange/selectByBaseProjectFindAll",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBaseProjectAll(VisaBaseProjectVo visaBaseProjectVo){
        List<VisaBaseProjectVo> voList = baseProjectService.selectByBaseProjectId(visaBaseProjectVo);
        return RestUtil.success(voList);
    }


    @RequestMapping(value = "/visaChange/selectByBaseProjectId",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBybaseProjectId(VisaBaseProjectVo visaBaseProjectVo){
        BaseProject baseProject = baseProjectService.findByBaseProjectId(visaBaseProjectVo);
        return RestUtil.success(baseProject);
    }
}
