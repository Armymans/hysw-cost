package net.zlw.cloud.maintenanceProjectInformation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.OneCensus;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.StatisticalFigureVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.StatisticalNumberVo;
import net.zlw.cloud.maintenanceProjectInformation.service.MaintenanceProjectInformationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    @RequestMapping(value = "/maintenanceProjectInformationSum/list",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllMaintenanceProjectInformation(PageRequest pageRequest){
        PageInfo<MaintenanceProjectInformation> allMaintenanceProjectInformation = maintenanceProjectInformationService.list(pageRequest, getLoginUser());
        return RestUtil.page(allMaintenanceProjectInformation);
    }

    /**
     * @Author Armyman
     * @Description //本月任务
     * @Date 20:18 2020/10/8
     **/
    @RequestMapping(value = "/maintenanceProjectInformationSum/month",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> month(){
        Integer month = maintenanceProjectInformationService.month();
        Integer month2 = maintenanceProjectInformationService.month2();
        Integer baifen = prjectCensusRast(month,month2);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("month",month.toString());
        map.put("baifen",baifen.toString());
        return RestUtil.success(map);
    }

    /**
     * @Author Armyman
     * @Description // 本年任务
     * @Date 20:18 2020/10/8
     **/
    @RequestMapping(value = "/maintenanceProjectInformationSum/year",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> year(){
        Integer year = maintenanceProjectInformationService.year();
        Integer year2 = maintenanceProjectInformationService.year2();
        Integer baifen = prjectCensusRast(year, year2);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("year",year.toString());
        map.put("baifen",baifen.toString());
        return RestUtil.success(map);
    }

    public Integer prjectCensusRast(Integer A,Integer B){
        if(A == 0){
            A = 1;
        }
        return (A-B)/A*100;
    }

    /**
     * @Author Armyman
     * @Description // 统计图
     * @Date 19:35 2020/10/8
     **/
    @RequestMapping(value = "/maintenanceProjectInformationSum/statisticalFigure",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> statisticalFigure(String projectAddress,String startDate,String endDate){
        List<StatisticalFigureVo> allMaintenanceProjectInformation = maintenanceProjectInformationService.statisticalFigure(projectAddress,startDate,endDate, getLoginUser());

        String censusList = "[{\"companyName\":\"道路恢复工程\"," +
                "\"imageAmmount\": [";
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList +=
                        "{\"time\": \"" + oneCensus.getYeartime() + "-" + oneCensus.getMonthtime() + "\"," +
                                "\"truckAmmount\": \"" + oneCensus.getParam1()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"表位改造\"," +
                        "\"imageAmmount\": [" ;
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam2()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"故障换表\"," +
                        "\"imageAmmount\": [" ;
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList +=
                        "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                                "\"truckAmmount\": \"" + oneCensus.getParam3()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"水表周检换表\"," +
                        "\"imageAmmount\": [" ;
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList +=
                        "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                                "\"truckAmmount\": \"" + oneCensus.getParam4()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"DN300以上管道抢维修\"," +
                        "\"imageAmmount\": [" ;
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam5()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\":\"DN300以下管道抢维修\"," +
                        "\"imageAmmount\": [" ;
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam6()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else {
            censusList += "{}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"设备维修购置\"," +
                        "\"imageAmmount\": [";
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam7()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else {
            censusList += "{}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"房屋修缮\"," +
                        "\"imageAmmount\": [";
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam8()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }

        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"绿化种植\"," +
                        "\"imageAmmount\": [";
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam9()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }


        censusList +=
                "]" +
                        "}, {" +
                        "\"companyName\": \"装饰及装修\"," +
                        "\"imageAmmount\": [";
        if(allMaintenanceProjectInformation.size()>0){
            for (StatisticalFigureVo oneCensus : allMaintenanceProjectInformation) {
                censusList += "{\"time\": \""+oneCensus.getYeartime()+"-"+oneCensus.getMonthtime()+"\"," +
                        "\"truckAmmount\": \"" + oneCensus.getParam10()+"\"},";
            }
            censusList = censusList.substring(0,censusList.length() -1);
        }else{
            censusList += "{}";
        }
        censusList +=  "]}]";

        JSONArray json = JSON.parseArray(censusList);
        return RestUtil.success(json);
    }

}
