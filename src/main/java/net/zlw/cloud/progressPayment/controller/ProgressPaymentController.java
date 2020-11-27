package net.zlw.cloud.progressPayment.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
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
import java.util.HashMap;
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
    public Map<String,Object> seachProgressById(@RequestParam(name = "id") String id,@RequestParam(name = "visaNum") String visaNum){
        try {
            BaseProjectVo baseProjectVo = baseProjectService.seachProgressById(id,getLoginUser(),visaNum);
//            BaseProjectVo baseProjectVo = baseProjectService.seachProgressById(id,new UserInfo("user309",null,null,true),visaNum);

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
            UserInfo userInfo = getLoginUser();
//            userInfo = new UserInfo("user309",null,null,true);
            baseProjectService.updateProgress(baseProjectVo,userInfo);
            return RestUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.success("编辑失败");
        }
    }

    //编辑进度款
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

        pageVo.setUid(getLoginUser().getId());
//        pageVo.setUid("user309");
        PageInfo<ProgressListVo> progressListVoPageInfo = baseProjectService.searchAllProgress(pageVo);
        return RestUtil.page(progressListVoPageInfo);
    }

    //模糊搜索
    @RequestMapping(value = "/progress/selectProgressPaymentStatus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectProgressPaymentStatus(PageVo pageVo){
        Page page = new Page();
        pageVo.setProgressStatus("");
        PageInfo<ProgressListVo> pageInfo = baseProjectService.searchAllProgress(pageVo);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        Page page1 = new Page();
        pageVo.setProgressStatus("1");
        PageInfo<ProgressListVo> pageInfo1 = baseProjectService.searchAllProgress(pageVo);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());

        Page page2 = new Page();
        pageVo.setProgressStatus("2");
        PageInfo<ProgressListVo> pageInfo2 = baseProjectService.searchAllProgress(pageVo);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());

        Page page3 = new Page();
        pageVo.setProgressStatus("3");
        PageInfo<ProgressListVo> pageInfo3 = baseProjectService.searchAllProgress(pageVo);
        page3.setData(pageInfo.getList());
        page3.setPageNum(pageInfo.getPageNum());
        page3.setPageSize(pageInfo.getPageSize());
        page3.setTotalCount(pageInfo.getTotal());

        Page page4 = new Page();
        pageVo.setProgressStatus("4");
        PageInfo<ProgressListVo> pageInfo4 = baseProjectService.searchAllProgress(pageVo);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());

        Page page5 = new Page();
        pageVo.setProgressStatus("5");
        PageInfo<ProgressListVo> pageInfo5 = baseProjectService.searchAllProgress(pageVo);
        page5.setData(pageInfo5.getList());
        page5.setPageNum(pageInfo5.getPageNum());
        page5.setPageSize(pageInfo5.getPageSize());
        page5.setTotalCount(pageInfo5.getTotal());

        Page page6 = new Page();
        pageVo.setProgressStatus("6");
        PageInfo<ProgressListVo> pageInfo6 = baseProjectService.searchAllProgress(pageVo);
        page6.setData(pageInfo6.getList());
        page6.setPageNum(pageInfo6.getPageNum());
        page6.setPageSize(pageInfo6.getPageSize());
        page6.setTotalCount(pageInfo6.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);
        map.put("table6",page5);
        map.put("table7",page6);

        return RestUtil.success(map);

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
        baseProjectService.batchReview(batchReviewVo,getLoginUser());
        return RestUtil.success("审核完毕");
    }
//    @RequestMapping(value = "/progress/oneBatchReview",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
//    public Map<String,Object> oneBatchReview(@RequestParam(name = "progressId") String id){
//        baseProjectService.oneBatchReview(id);
//        return RestUtil.success();
//    }

    //查询累计支付金额
    @RequestMapping(value = "/progress/findAllProgressPaymentTotalPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllProgressPaymentTotalPayment(@RequestParam(name = "id") String id){
        List<ProgressPaymentTotalPaymentVo> allProgressPaymentTotalPayment = progressPaymentTotalPaymentService.findAllProgressPaymentTotalPayment(id);
        return RestUtil.success(allProgressPaymentTotalPayment);
    }

    //批量审核
    @RequestMapping(value = "/progress/auditChek",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> auditChek(@RequestParam(name = "id") String id){
        List<AuditChekedVo> auditChekedVos = baseProjectService.auditChek(id);
        return RestUtil.success(auditChekedVos);
    }
}
