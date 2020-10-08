package net.zlw.cloud.maintenanceProjectInformation.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.StatisticalNumberVo;
import net.zlw.cloud.maintenanceProjectInformation.service.MaintenanceProjectInformationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author Armyman
 * @Description 检维修统计
 * @Date 2020/10/8 11:18
 **/
@RestController
public class MaintenanceProjectInformationSumController extends BaseController {

    @Resource
    private MaintenanceProjectInformationService maintenanceProjectInformationService;

    /**
     * @Author Armyman
     * @Description //检维修任务统计数量
     * @Date 11:26 2020/10/8
     **/
    @RequestMapping(value = "/maintenanceProjectInformationSum/statisticalNumber",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> statisticalNumber(@RequestParam("projectAddress") String projectAddress){
        StatisticalNumberVo statisticalNumberVo = maintenanceProjectInformationService.statisticalNumber(projectAddress);
        return RestUtil.success(statisticalNumberVo);
    }

    /**
     * @Author Armyman
     * @Description //统计分析 列表
     * @Date 17:53 2020/10/8
     **/
    @RequestMapping(value = "/maintenanceProjectInformationSum/list",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllMaintenanceProjectInformation(PageRequest pageRequest){
        PageInfo<MaintenanceProjectInformation> allMaintenanceProjectInformation = maintenanceProjectInformationService.list(pageRequest, getLoginUser());
        return RestUtil.success(allMaintenanceProjectInformation);
    }
}
