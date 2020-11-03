package net.zlw.cloud.excelLook.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import net.zlw.cloud.excelLook.service.QuantitiesPartialWorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class QuantitiesPartialWorksController {

    @Autowired
    private QuantitiesPartialWorksService quantitiesPartialWorksService;
//预算分部分项查看
    @RequestMapping(value = "/excel/quantitiesPartialWorks",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){
        List<QuantitiesPartialWorks> quantitiesPartialWorks = quantitiesPartialWorksService.quantitiesPartialWorksList(id);
        return RestUtil.success(quantitiesPartialWorks);
    }

    //上家结算分部分项查看
    @RequestMapping(value = "/cover/quantitiesPartialWorks",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getQuantitiesPartialWorks (@RequestParam("id") String id){
        List<QuantitiesPartialWorks> quantitiesPartialWorks = quantitiesPartialWorksService.quantitiesPartialWorksLists(id);
        return RestUtil.success(quantitiesPartialWorks);
    }
}
