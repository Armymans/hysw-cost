package net.zlw.cloud.excelLook.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.SummaryUnits;
import net.zlw.cloud.excelLook.service.SummaryUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SummaryUnitsController {

    @Autowired
    private SummaryUnitsService summaryUnitsService;
//预算单位汇总表
    @RequestMapping(value = "/excel/summaryUnits",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findList(@RequestParam("id") String id){

        List<SummaryUnits> summaryUnits = summaryUnitsService.summaryUnitsList(id);
        return RestUtil.success(summaryUnits);
    }

}
