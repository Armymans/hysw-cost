package net.zlw.cloud.statisticalAnalysis;

import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/statis")
public class ProjectStatistics {
    @Resource
    private BaseProjectService baseProjectService;
    @GetMapping("/numberItems")
    public NumberVo NumberItems(){
       NumberVo numberVo =  baseProjectService.NumberItems();
        return numberVo;
    }

}
