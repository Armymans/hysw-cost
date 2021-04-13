package net.zlw.cloud.progressPayment.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.*;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.progressPayment.service.ProgressPaymentInformationService;
import net.zlw.cloud.progressPayment.service.ProgressPaymentTotalPaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    private ProgressPaymentInformationService progressPaymentInformationService;

    @Resource
    private ProgressPaymentTotalPaymentService progressPaymentTotalPaymentService;

    //进度款新增
//    @PostMapping("/addProgress")progress/updateProgress
    @RequestMapping(value = "/progress/addProgress",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addProgress(BaseProjectVo baseProject){
        try {
            baseProjectService.addProgress(baseProject,getLoginUser(),request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
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
    //根据id查询编辑进度款
//    @GetMapping("/seachProgressById/{id}")
    @RequestMapping(value = "/progress/editProgressById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editProgressById(@RequestParam(name = "pid") String pid,@RequestParam(name = "visaNum") String visaNum){
        try {
            BaseProjectVo baseProjectVo = baseProjectService.editProgressById(pid,getLoginUser(),visaNum);
            return RestUtil.success(baseProjectVo);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error();
        }
    }

  /**
      * @Author sjf
      * @Description //委外金额
      * @Date 15:44 2020/12/28
      * @Param
      * @return
   **/
    @RequestMapping(value = "/progress/editOutSourceMoney",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editOutSourceMoney(@RequestParam(name = "id") String id,@RequestParam(name = "outSourceMoney") String outSourceMoney){
       baseProjectService.editOutSourceMoney(id,outSourceMoney);
       return RestUtil.success("编辑成功");
    }


    //编辑
//    @PostMapping("/updateProgress")
    @RequestMapping(value = "/progress/updateProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateProgress(BaseProjectVo baseProjectVo){
        try {
            UserInfo userInfo = getLoginUser();
//            userInfo = new UserInfo("user309",null,null,true);
            baseProjectService.updateProgress(baseProjectVo,userInfo,request);
            return RestUtil.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
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
            return RestUtil.error(e.getMessage());
        }
    }
    //查询进度款列表
    @RequestMapping(value = "/progress/searchAllProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> searchAllProgress(PageVo pageVo){

        pageVo.setUid(getLoginUser().getId());
//        pageVo.setUid("user325");
        PageInfo<ProgressListVo> progressListVoPageInfo = baseProjectService.searchAllProgress(pageVo);
        return RestUtil.page(progressListVoPageInfo);
    }

    //模糊搜索
    @RequestMapping(value = "/progress/selectProgressPaymentStatus",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectProgressPaymentStatus(PageVo pageVo){
        pageVo.setUid(getLoginUser().getId());
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
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());

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

    @RequestMapping(value = "/progress/selectProgressPaymentStatus2",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectProgressPaymentStatus2(PageVo pageVo){
        pageVo.setUid(getLoginUser().getId());
        String progressStatus = pageVo.getProgressStatus();
        if (StringUtils.isEmpty(progressStatus)){
            pageVo.setProgressStatus("");
        }
        Page page = new Page();
        PageInfo<ProgressListVo> pageInfo = baseProjectService.searchAllProgress(pageVo);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);

        return RestUtil.success(map);

    }

    //删除进度款
    @RequestMapping(value = "/progress/deleteProgress",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteProgress(@RequestParam(name = "id") String id){
        baseProjectService.deleteProgress(id,getLoginUser(),request);
        return RestUtil.success("删除成功");
    }

    //确认完成功能
    @RequestMapping(value = "/progress/accomplish",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> accomplish(String ids){
        try {
            baseProjectService.accomplish(ids,getLoginUser());
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("删除成功");
    }

    //退回功能
    @RequestMapping(value = "/progress/sendBack",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> sendBack(String id,String auditOpinion){
            baseProjectService.sendBack(id,auditOpinion,getLoginUser());
        return RestUtil.success("退回成功");
    }
    //进度款批量审核
    @RequestMapping(value = "/progress/batchReview",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        baseProjectService.batchReview(batchReviewVo,getLoginUser(),request);
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

    //查询进度款累计列表
    @RequestMapping(value = "/progress/findTotalList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findTotalList(@RequestParam(name = "baseId") String baseId){
      List<ProgressPaymentInformation> list = baseProjectService.findTotalList(baseId);
      return RestUtil.success(list);
    }
    //查询进度款累计
    @RequestMapping(value = "/progress/findTotal",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findTotal(@RequestParam(name = "baseId") String baseId){
        TotalVo list = baseProjectService.findTotal(baseId);
        return RestUtil.success(list);
    }
    //查询审核信息圆球
    @RequestMapping(value = "/progress/findcheckAll",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findcheckAll(@RequestParam(name = "num") String num,@RequestParam(name = "id") String id){
        List<AuditChekedVo> list = baseProjectService.findcheckAll(num,id);
        return RestUtil.success(list);
    }


    //新增-进度款信息
    @RequestMapping(value = "/progress/addPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addPayment(PaymentVo paymentVo){
        progressPaymentInformationService.addPayment(paymentVo);
        return RestUtil.success("新增成功");
    }

    //编辑-进度款信息
    @RequestMapping(value = "/progress/editPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editPayment(@RequestBody PaymentVo paymentVo){
        progressPaymentInformationService.editPayment(paymentVo);
        return RestUtil.success("修改成功");
    }

    //回显-进度款信息
    @RequestMapping(value = "/progress/findPayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPayment(String id){

        PaymentVo findPayment = progressPaymentInformationService.findPayment(id);
        return RestUtil.success(findPayment);
    }

    //删除-进度款信息
    @RequestMapping(value = "/progress/deletePayment",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deletePayment(String id){

        progressPaymentInformationService.deletePayment(id);
        return RestUtil.success("删除成功");
    }

    //查询-进度款信息
    @RequestMapping(value = "/progress/paymentList",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> paymentList(String id){
        List<PaymentListVo> paymentVos = progressPaymentInformationService.paymentList(id);
        return RestUtil.success(paymentVos);
    }
}
