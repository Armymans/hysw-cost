package net.zlw.cloud.excelLook.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excelLook.service.FinalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FinalReportController {

    @Autowired
    private FinalReportService finalReportService;

    /***
     * 结算封面扉页查看
     * @param id
     * @return
     */
    @RequestMapping(value = "/excel/finalReport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){
        FinalReport list = finalReportService.getList(id);
        return RestUtil.success(list);
    }
    //检维修封面扉页查看
    @RequestMapping(value = "/maintenance/finalReport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getmaintenance(@RequestParam("id") String id){
        FinalReport list = finalReportService.getFinalReport(id);
        return RestUtil.success(list);
    }
//检维修封面扉页修改
    @RequestMapping(value = "/update/finalReport",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> update(FinalReport finalReport){
         finalReportService.updateFinalReport(finalReport);
        return RestUtil.success();
    }
}
