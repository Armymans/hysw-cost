package net.zlw.cloud.excel.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.service.BudgetCoverService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class BudgetCoverController {
    @Resource
    private BudgetCoverService budgetCoverService;

    //汇总表-神机封面导入
    @RequestMapping(value = "/budgetCover/coverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> coverImport(){
        budgetCoverService.coverImport();
        return RestUtil.success();
    }
    //汇总表-神机 单位汇总表导入
    @RequestMapping(value = "/budgetCover/summaryUnitsImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> summaryUnitsImport(){
        budgetCoverService.summaryUnitsImport();
        return RestUtil.success();
    }
    //汇总表-神机 分部分项工程量清单计价表
    @RequestMapping(value = "/budgetCover/partTableQuantitiesImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> partTableQuantitiesImport(){
        budgetCoverService.partTableQuantitiesImport();
        return RestUtil.success();
    }
    //物料清单表(安徽)
    @RequestMapping(value = "/budgetCover/bomTableImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> bomTableImport(@RequestParam(name = "id") String id){
        budgetCoverService.bomTableImport(id);
        return RestUtil.success();
    }
    //上家结算汇总表(安徽)
    @RequestMapping(value = "/budgetCover/LastSummaryCoverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> LastSummaryCoverImport(@RequestParam(name = "id") String id){
        budgetCoverService.LastSummaryCoverImport(id);
        return RestUtil.success();
    }
//    @RequestMapping(value = "/budgetCover/LastSummaryCoverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)




}
