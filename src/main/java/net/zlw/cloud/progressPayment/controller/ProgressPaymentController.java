package net.zlw.cloud.progressPayment.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressPaymentTotalPaymentVo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.progressPayment.service.ProgressPaymentTotalPaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//@RequestMapping("/progress")
@RestController
public class ProgressPaymentController  extends BaseController {
    @Resource
    private BaseProjectService baseProjectService;

    @Resource
    private ProgressPaymentTotalPaymentService progressPaymentTotalPaymentService;

    //进度款新增
//    @PostMapping("/addProgress")
    @RequestMapping(value = "/progress/addProgress",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addProgress(BaseProjectVo baseProject){
        try {
            baseProjectService.addProgress(baseProject,getLoginUser());
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error("失败");
        }
        return RestUtil.success("成功");
    }
    //根据id查询进度款
//    @GetMapping("/seachProgressById/{id}")
    @RequestMapping(value = "/progress/seachProgressById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> seachProgressById(@RequestParam(name = "id") String id){
        try {
            BaseProjectVo baseProjectVo = baseProjectService.seachProgressById(id);
            return RestUtil.success(baseProjectVo);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error();
        }
    }



    //编辑
//    @PostMapping("/updateProgress")
    @RequestMapping(value = "/progress/updateProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateProgress(BaseProjectVo baseProjectVo){
        try {
            baseProjectService.updateProgress(baseProjectVo);
            return RestUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.success("编辑失败");
        }
    }

    @RequestMapping(value = "/progress/updateProgressPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateProgressPayment(BaseProjectVo baseProjectVo){
        try {
            baseProjectService.updateProgressPayment(baseProjectVo);
            return RestUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.success("编辑失败");
        }
    }
    //查询进度款列表
    @RequestMapping(value = "/progress/searchAllProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> searchAllProgress(PageVo pageVo){
        System.out.println(pageVo);
        pageVo.setUid(getLoginUser().getId());
        PageInfo<ProgressListVo> progressListVoPageInfo = baseProjectService.searchAllProgress(pageVo);
        return RestUtil.page(progressListVoPageInfo);
    }

    //删除进度款
    @RequestMapping(value = "/progress/deleteProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteProgress(@RequestParam(name = "id") String id){
        baseProjectService.deleteProgress(id);
        return RestUtil.success("删除成功");
    }
    //进度款批量审核
    @RequestMapping(value = "/progress/batchReview",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        baseProjectService.batchReview(batchReviewVo);
        return RestUtil.success("审核完毕");
    }
//    @RequestMapping(value = "/progress/oneBatchReview",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> oneBatchReview(@RequestParam(name = "progressId") String id){
//        baseProjectService.oneBatchReview(id);
//        return RestUtil.success();
//    }

    @RequestMapping(value = "/progress/findAllProgressPaymentTotalPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllProgressPaymentTotalPayment(@RequestParam(name = "id") String id){
        List<ProgressPaymentTotalPaymentVo> allProgressPaymentTotalPayment = progressPaymentTotalPaymentService.findAllProgressPaymentTotalPayment(id);
        return RestUtil.success(allProgressPaymentTotalPayment);
    }

    @RequestMapping(value = "/progress/auditChek",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> auditChek(@RequestParam(name = "id") String id){
        List<AuditChekedVo> auditChekedVos = baseProjectService.auditChek(id);
        return RestUtil.success(auditChekedVos);
    }
}
