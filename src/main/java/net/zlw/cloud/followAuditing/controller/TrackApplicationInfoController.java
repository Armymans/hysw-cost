package net.zlw.cloud.followAuditing.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.DateUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/track")
public class TrackApplicationInfoController extends BaseController {
    @Resource
    private TrackApplicationInfoService trackApplicationInfoService;
    @Autowired
    private FileInfoService fileInfoService;

    //查询所有跟踪审计
//    @PostMapping("/track/selectList")
    @RequestMapping(value = "/track/selectList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectTrackList(PageVo pageVo){
        //todo getLoginUser().getId()
        //pageVo.setUid(getLoginUser().getId());
        pageVo.setUid("user312");
        PageInfo<ReturnTrackVo> pageInfo = trackApplicationInfoService.selectTrackList(pageVo);
        return RestUtil.page(pageInfo);
    }

    /**
        * @Author sjf
        * @Description //跟踪审计 模糊查找
        * @Date 11:11 2020/11/22
        * @Param
        * @return
     **/
    @RequestMapping(value = "/track/findTrackList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findTrackList(PageVo pageVo){
        //全部
        Page page = new Page();
        pageVo.setTrackStatus("");
        PageInfo<ReturnTrackVo> pageInfo = trackApplicationInfoService.selectTrackList(pageVo);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());
        //待审核
        Page page1 = new Page();
        pageVo.setTrackStatus("1");
        PageInfo<ReturnTrackVo> pageInfo1 = trackApplicationInfoService.selectTrackList(pageVo);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());
        //未提交
        Page page2 = new Page();
        pageVo.setTrackStatus("2");
        PageInfo<ReturnTrackVo> pageInfo2 = trackApplicationInfoService.selectTrackList(pageVo);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());
        //进行中
        Page page3 = new Page();
        pageVo.setTrackStatus("3");
        PageInfo<ReturnTrackVo> pageInfo3 = trackApplicationInfoService.selectTrackList(pageVo);
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());
        //未通过
        Page page4 = new Page();
        pageVo.setTrackStatus("4");
        PageInfo<ReturnTrackVo> pageInfo4 = trackApplicationInfoService.selectTrackList(pageVo);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());
        //已完成
        Page page5 = new Page();
        pageVo.setTrackStatus("5");
        PageInfo<ReturnTrackVo> pageInfo5 = trackApplicationInfoService.selectTrackList(pageVo);
        page5.setData(pageInfo5.getList());
        page5.setPageNum(pageInfo5.getPageNum());
        page5.setPageSize(pageInfo5.getPageSize());
        page5.setTotalCount(pageInfo5.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);
        map.put("table6",page5);
        return RestUtil.success(map);
    }

    //查看页面回显月报列表
    @RequestMapping(value = "/track/findAllByTrackId",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllByTrackId(@RequestParam(name = "id") String id){
        List<TrackMonthly> allByTrackId = trackApplicationInfoService.findAllByTrackId(id);
        return RestUtil.success(allByTrackId);
    }

    /**
        * @Author sjf
        * @Description //委外金额
        * @Date 15:55 2020/12/28
        * @Param
        * @return
     **/
    @RequestMapping(value = "/track/editOutMoney",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editOutMoney(String id,String outMoney){
        trackApplicationInfoService.editOutMoney(id,outMoney);
        return RestUtil.success("编辑成功");
    }

    //编辑页面回显月报列表
    @RequestMapping(value = "/track/findAllByTrackId3",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllByTrackId3(@RequestParam(name = "id") String id){
        List<TrackMonthly> allByTrackId = trackApplicationInfoService.findAllByTrackId3(id,getLoginUser());
        return RestUtil.success(allByTrackId);
    }

    //新增月报回显填写人
    @RequestMapping(value = "/track/findWritter",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findWritter(){
         TrackMonthly trackMonthly = trackApplicationInfoService.findWritter(getLoginUser().getId());
        return RestUtil.success(trackMonthly);
    }

    // 查看页面回显审核信息
    @RequestMapping(value = "/track/findAllAuditInfosByTrackId",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllAuditInfosByTrackId(@RequestParam(name = "id") String id){
        List<AuditInfoVo> auditInfosByTrackId = trackApplicationInfoService.findAllAuditInfosByTrackId(id);
        return RestUtil.success(auditInfosByTrackId);
    }



    //删除跟踪审计
//    @DeleteMapping("/track/deleteById/{id}")
    @RequestMapping(value = "/track/deleteById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteById(@RequestParam(name = "id") String id){
        trackApplicationInfoService.deleteById(id,getLoginUser(),request);
        return RestUtil.success("删除成功");
    }

    // 删除审计月报
    @RequestMapping(value = "/track/deleteByIdTrackMonthly",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteByIdTrackMonthly(@RequestParam(name = "id") String id){
        trackApplicationInfoService.deleteByIdTrackMonthly(id);
        return RestUtil.success("删除成功");
    }

    // 跟踪审计确认完成
    @RequestMapping(value = "/track/accomplish",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> accomplish(String ids){
        try {
            trackApplicationInfoService.accomplish(ids,getLoginUser());
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("删除成功");
    }

    // 退回功能
    @RequestMapping(value = "/track/sendBack",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> sendBack(String id,String opinion){
        trackApplicationInfoService.sendBack(id,opinion);
        return RestUtil.success("退回成功");
    }
    //批量审核
//    @PostMapping("/track/batchReview")
    @RequestMapping(value = "/track/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            if(s!=null){
                batchReviewVo.setBatchAll(s);
                trackApplicationInfoService.batchReview(batchReviewVo,getLoginUser(),request);
            }
        }
        return RestUtil.success("审核完毕");
    }
    //添加跟踪审计
//    @PostMapping("/track/addTrack")
    @RequestMapping(value = "/track/addTrack",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addTrack(TrackVo trackVo,@RequestParam(name = "baseId") String baseId){
        System.out.println(baseId);
        try {
            trackApplicationInfoService.addTrack(trackVo,getLoginUser(),baseId,request);
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("添加成功");
    }
    //跟踪审计信息回显--查看
//    @GetMapping("/track/selectTrackById/{id}")
    @RequestMapping(value = "/track/selectTrackById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectTrackById(@RequestParam(name = "id") String id){
       TrackVo trackVo =  trackApplicationInfoService.selectTrackById(id,getLoginUser());
        return RestUtil.success(trackVo);
    }
    //跟踪审计信息回显--编辑
//    @GetMapping("/track/selectTrackById/{id}")
    @RequestMapping(value = "/track/editTrackById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editTrackById(@RequestParam(name = "id") String id){
        TrackVo trackVo =  trackApplicationInfoService.editTrackById(id,getLoginUser());
        return RestUtil.success(trackVo);
    }
    //修改审计月报
//    @PutMapping("/track/updateMonthly")
    @RequestMapping(value = "/track/updateMonthly",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateMonthly(TrackMonthly trackMonthly){
        trackApplicationInfoService.updateMonthly(trackMonthly);
        return RestUtil.success("修改成功");
    }
    //跟踪审计编辑保存于提交
//    @PutMapping("/track/updateTrack")
    @RequestMapping(value = "/track/updateTrack",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateTrack(TrackVo trackVo){
        try {
            trackApplicationInfoService.updateTrack(trackVo,getLoginUser(),request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("修改成功");
    }
    //新增月报

    @RequestMapping(value = "/track/addMonthly",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addMaonthly(TrackMonthly monthly){
        trackApplicationInfoService.addTrackMonthly(monthly);
        FileInfo fileInfo = fileInfoService.getByKey(monthly.getFid());
        fileInfo.setName(monthly.getTitle());
        fileInfo.setRemark(monthly.getId());
        fileInfo.setPlatCode(monthly.getId());
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoService.updateFileName(fileInfo);
        return RestUtil.success("新增成功");
    }

//    新增删除审计月报
    @RequestMapping(value = "/track/deleteTrackMonthly",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteMonthly(String key){
        trackApplicationInfoService.deleteMonthly(key);
        return RestUtil.success();
    }
    //归属
    @RequestMapping(value = "/track/addAttribution",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addAttribution(@RequestParam(name = "baseProjectId") String baseId,@RequestParam(name = "district") String district,@RequestParam(name = "designCategory") String designCategory,@RequestParam(name = "prePeople") String prePeople){
        trackApplicationInfoService.addAttribution(baseId,district,designCategory,prePeople);
        return RestUtil.success();
    }


}
