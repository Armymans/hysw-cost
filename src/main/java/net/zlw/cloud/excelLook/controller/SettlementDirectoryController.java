package net.zlw.cloud.excelLook.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;
import net.zlw.cloud.excelLook.service.SettlementDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SettlementDirectoryController {

    @Autowired
    private SettlementDirectoryService settlementDirectoryService;

//结算目录查看
    @RequestMapping(value = "/excel/settlementDirectory",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){
        List<SettlementDirectory> list = settlementDirectoryService.getList(id);
        return RestUtil.success(list);
    }
//检维修目录查看
    @RequestMapping(value = "/maintenance/settlementDirectory",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getMaintenance(@RequestParam("id") String id){
        List<SettlementDirectory> list = settlementDirectoryService.getMantenceList(id);
        return RestUtil.success(list);
    }
//检维修修改
    @RequestMapping(value = "/update/settlementDirectory",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getMaintenance(@RequestBody SettlementDirectory settlementDirectory){
        settlementDirectoryService.updateSettlementDirectory(settlementDirectory);
        return RestUtil.success();
    }
}
