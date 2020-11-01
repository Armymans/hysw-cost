package net.zlw.cloud.excelLook.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excelLook.domain.SettlementReportText;
import net.zlw.cloud.excelLook.service.SettlementReportTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SettlementReportTextController {

    @Autowired
    private SettlementReportTextService settlementReportTextService;

    @RequestMapping(value = "/excel/settlementReportText",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){
        SettlementReportText list = settlementReportTextService.list(id);
        return RestUtil.success(list);
    }
}
