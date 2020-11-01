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
    //汇总表-神机 分部分项工程量清单计价表   /    上家结算汇总表 分部分项目工程量清单计价表
    @RequestMapping(value = "/budgetCover/partTableQuantitiesImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> partTableQuantitiesImport(@RequestParam(name = "id") String id){
        budgetCoverService.partTableQuantitiesImport(id);
        return RestUtil.success();
    }
    //物料清单表(安徽)
    @RequestMapping(value = "/budgetCover/bomTableImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> bomTableImport(@RequestParam(name = "id") String id){
        budgetCoverService.bomTableImport(id);
        return RestUtil.success();
    }
    //上家结算汇总表(安徽) 封面
    @RequestMapping(value = "/budgetCover/lastSummaryCoverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> LastSummaryCoverImport(@RequestParam(name = "id") String id){
        budgetCoverService.LastSummaryCoverImport(id);
        return RestUtil.success();
    }
    //上家结算汇总表 单位工程造价汇总表
    @RequestMapping(value = "/budgetCover/unitProjectSummaryImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> UnitProjectSummaryImport(@RequestParam(name = "id") String id){
        budgetCoverService.UnitProjectSummaryImport(id);
        return RestUtil.success();
    }
    //下家结算审核汇总表 -   汇总表
    @RequestMapping(value = "/budgetCover/summaryTableImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> summaryTableImport(@RequestParam(name = "id") String id){
        budgetCoverService.summaryTableImport(id);
        return RestUtil.success();
    }
    //下家结算审核核定单
    @RequestMapping(value = "/budgetCover/verificationSheetImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> verificationSheetImport(@RequestParam(name = "id") String id){
        budgetCoverService.verificationSheetImport(id);
        return RestUtil.success();
    }







}
