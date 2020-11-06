package net.zlw.cloud.followAuditing.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.DateUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        pageVo.setUid(getLoginUser().getId());
        PageInfo<ReturnTrackVo> pageInfo = trackApplicationInfoService.selectTrackList(pageVo);
        return RestUtil.page(pageInfo);
    }

    // 查看，编辑页面回显月报列表
    @RequestMapping(value = "/track/findAllByTrackId",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllByTrackId(@RequestParam(name = "id") String id){
        List<TrackMonthly> allByTrackId = trackApplicationInfoService.findAllByTrackId(id);
        return RestUtil.success(allByTrackId);
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
        trackApplicationInfoService.deleteById(id);
        return RestUtil.success("删除成功");
    }

    // 删除审计月报
    @RequestMapping(value = "/track/deleteByIdTrackMonthly",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteByIdTrackMonthly(@RequestParam(name = "id") String id){
        trackApplicationInfoService.deleteByIdTrackMonthly(id);
        return RestUtil.success("删除成功");
    }
    //批量审核
//    @PostMapping("/track/batchReview")
    @RequestMapping(value = "/track/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        trackApplicationInfoService.batchReview(batchReviewVo);
        return RestUtil.success("审核完毕");
    }
    //添加跟踪审计
//    @PostMapping("/track/addTrack")
    @RequestMapping(value = "/track/addTrack",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addTrack(TrackVo trackVo,@RequestParam(name = "baseId") String baseId){
        System.out.println(baseId);
        trackApplicationInfoService.addTrack(trackVo,getLoginUser(),baseId);
        return RestUtil.success("添加成功");
    }
    //跟踪审计信息回显
//    @GetMapping("/track/selectTrackById/{id}")
    @RequestMapping(value = "/track/selectTrackById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectTrackById(@RequestParam(name = "id") String id){
       TrackVo trackVo =  trackApplicationInfoService.selectTrackById(id);
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
        trackApplicationInfoService.updateTrack(trackVo);
        return RestUtil.success("修改成功");
    }
    //新增月报

    @RequestMapping(value = "/track/addMonthly",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addMaonthly(TrackMonthly monthly){
        System.out.println(monthly);
        trackApplicationInfoService.addTrackMonthly(monthly);
        FileInfo fileInfo = fileInfoService.getByKey(monthly.getFid());
        fileInfo.setName(monthly.getTitle());
        fileInfo.setRemark(monthly.getId());
        fileInfo.setPlatCode(monthly.getId());
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoService.updateFileName(fileInfo);
        return RestUtil.success("新增成功");
    }




}
