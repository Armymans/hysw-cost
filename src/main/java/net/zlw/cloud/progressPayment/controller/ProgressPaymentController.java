package net.zlw.cloud.progressPayment.controller;

import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/progress")
@RestController
public class ProgressPaymentController {
    @Resource
    private BaseProjectService baseProjectService;

    //进度款新增
    @PostMapping("/addProgress")
    public String addProgress(@RequestBody BaseProjectVo baseProject){
        try {
            baseProjectService.addProgress(baseProject);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }
    @GetMapping("/seachProgressById/{id}")
    public BaseProjectVo seachProgressById(@PathVariable(name = "id") String id){
        try {
            BaseProjectVo baseProjectVo = baseProjectService.seachProgressById(id);
            System.out.println(baseProjectVo);
            return baseProjectVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/updateProgress")
    public String updateProgress(@RequestBody BaseProjectVo baseProjectVo, @RequestParam(name = "page") String page){
        try {
            baseProjectService.updateProgress(baseProjectVo);
            return "编辑成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "编辑失败";
        }


    }
}
