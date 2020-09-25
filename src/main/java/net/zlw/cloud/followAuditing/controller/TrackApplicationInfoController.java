package net.zlw.cloud.followAuditing.controller;

import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackApplicationInfoController {
    @Resource
    private TrackApplicationInfoService trackApplicationInfoService;

    //查询所有跟踪审计
    @PostMapping("/selectList")
    public List<ReturnTrackVo> selectTrackList(@RequestBody PageVo pageVo){
        List<ReturnTrackVo> returnTrackVos = trackApplicationInfoService.selectTrackList(pageVo);
        System.out.println(returnTrackVos);
        return returnTrackVos;
    }

    //删除跟踪审计
    @DeleteMapping("/deleteById/{id}")
    public String deleteById(@PathVariable(name = "id") String id){
        trackApplicationInfoService.deleteById(id);
        return "删除成功";
    }
    //批量审核
    @PostMapping("/batchReview")
    public String batchReview(BatchReviewVo batchReviewVo){
        trackApplicationInfoService.batchReview(batchReviewVo);
        return "审核完毕";
    }
    //添加跟踪审计
    @PostMapping("/addTrack")
    public String addTrack(@RequestBody TrackVo trackVo){
        trackApplicationInfoService.addTrack(trackVo);
        return "添加成功";
    }
    //跟踪审计信息回显
    @GetMapping("/selectTrackById/{id}")
    public TrackVo selectTrackById(@PathVariable(name = "id") String id){
       TrackVo trackVo =  trackApplicationInfoService.selectTrackById(id);
        return trackVo;
    }
    //修改审计月报
    @PutMapping("/updateMonthly")
    public String updateMonthly(@RequestBody TrackMonthly trackMonthly){
        trackApplicationInfoService.updateMonthly(trackMonthly);
        return "修改成功";
    }
    //跟踪审计编辑保存于提交
    @PutMapping("/updateTrack")
    public String updateTrack(@RequestBody TrackVo trackVo){
        trackApplicationInfoService.updateTrack(trackVo);
        return "修改成功";
    }

}
