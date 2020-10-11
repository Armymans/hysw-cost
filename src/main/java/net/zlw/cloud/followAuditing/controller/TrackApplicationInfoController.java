package net.zlw.cloud.followAuditing.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/track")
public class TrackApplicationInfoController {
    @Resource
    private TrackApplicationInfoService trackApplicationInfoService;

    //查询所有跟踪审计
//    @PostMapping("/track/selectList")
    @RequestMapping(value = "/track/selectList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectTrackList(PageVo pageVo){
        PageInfo<ReturnTrackVo> pageInfo = trackApplicationInfoService.selectTrackList(pageVo);
        return RestUtil.page(pageInfo);
    }

    // 查看，编辑页面回显列表
    @RequestMapping(value = "/track/findAllByTrackId",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllByTrackId(@RequestParam(name = "id") String id){
        List<TrackMonthly> allByTrackId = trackApplicationInfoService.findAllByTrackId(id);
        return RestUtil.success(allByTrackId);
    }

    //删除跟踪审计
//    @DeleteMapping("/track/deleteById/{id}")
    @RequestMapping(value = "/track/deleteById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public String deleteById(@RequestParam(name = "id") String id){
        trackApplicationInfoService.deleteById(id);
        return "删除成功";
    }
    //批量审核
    @PostMapping("/track/batchReview")
    public String batchReview(BatchReviewVo batchReviewVo){
        trackApplicationInfoService.batchReview(batchReviewVo);
        return "审核完毕";
    }
    //添加跟踪审计
    @PostMapping("/track/addTrack")
    public String addTrack(@RequestBody TrackVo trackVo){
        trackApplicationInfoService.addTrack(trackVo);
        return "添加成功";
    }
    //跟踪审计信息回显
//    @GetMapping("/track/selectTrackById/{id}")
    @RequestMapping(value = "/track/selectTrackById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectTrackById(@RequestParam(name = "id") String id){
       TrackVo trackVo =  trackApplicationInfoService.selectTrackById(id);
        return RestUtil.success(trackVo);
    }
    //修改审计月报
    @PutMapping("/track/updateMonthly")
    public String updateMonthly(@RequestBody TrackMonthly trackMonthly){
        trackApplicationInfoService.updateMonthly(trackMonthly);
        return "修改成功";
    }
    //跟踪审计编辑保存于提交
    @PutMapping("/track/updateTrack")
    public String updateTrack(@RequestBody TrackVo trackVo){
        trackApplicationInfoService.updateTrack(trackVo);
        return "修改成功";
    }

}
