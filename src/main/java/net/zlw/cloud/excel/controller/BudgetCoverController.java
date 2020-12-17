package net.zlw.cloud.excel.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.demo.FinalReport;
import net.zlw.cloud.excel.model.ReportTextVo;
import net.zlw.cloud.excel.model.SummaryShenji;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.excelLook.domain.SettlementDirectory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class BudgetCoverController {
    @Resource
    private BudgetCoverService budgetCoverService;

    //汇总表-神机封面导入
    @RequestMapping(value = "/budgetCover/coverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> coverImport(@RequestParam(name = "id") String id){
        budgetCoverService.coverImport(id,null);
        return RestUtil.success();
    }
    //汇总表-神机 单位汇总表导入
    @RequestMapping(value = "/budgetCover/summaryUnitsImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> summaryUnitsImport(@RequestParam(name = "id") String id){
        budgetCoverService.summaryUnitsImport(id,null);
        return RestUtil.success();
    }
    //汇总表-神机 分部分项工程量清单计价表   /    上家结算汇总表 分部分项目工程量清单计价表
    @RequestMapping(value = "/budgetCover/partTableQuantitiesImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> partTableQuantitiesImport(@RequestParam(name = "id") String id){
        budgetCoverService.partTableQuantitiesImport(id,null);
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
    //预算封面查看
    @RequestMapping(value = "/budgetCover/findSummaryShenjiById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBudgetCoverById(@RequestParam (name = "id") String id){
        SummaryShenji budgetCover =  budgetCoverService.findBudgetCoverById(id);
      return RestUtil.success(budgetCover);
    }
    //下家结算审核 甲供材料分析表
    @RequestMapping(value = "/budgetCover/materialAnalysisImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> materialAnalysisImport(@RequestParam(name = "id") String id){
        budgetCoverService.materialAnalysisImport(id);
        return RestUtil.success();
    }
    //结算报告 封面扉页编辑
    @RequestMapping(value = "/budgetCover/updateFinalReport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateFinalReport(FinalReport finalReport){
        budgetCoverService.updateFinalReport(finalReport);
        return RestUtil.success();
    }
    //结算报告目录编辑
    @RequestMapping(value = "/budgetCover/updateDirectory",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateDirectory(SettlementDirectory settlementDirectory){
        budgetCoverService.updateDirectory(settlementDirectory);
        return RestUtil.success();
    }
    //结算报告正文编辑
    @RequestMapping(value = "/budgetCover/updateReportContent",method = {RequestMethod.POST,RequestMethod.GET})
    public Map<String,Object> updateReportContent(@RequestBody ReportTextVo reportTextVo){
        budgetCoverService.updateReportContent(reportTextVo);
        return RestUtil.success();
    }

    //预算汇总表
    @RequestMapping(value = "/budgetCover/addbudgetAll",method = {RequestMethod.POST,RequestMethod.GET})
    public Map<String,Object> addbudgetAll(@RequestParam(name = "id") String id){
        budgetCoverService.addbudgetAll(id, null, null, null);
        return RestUtil.success();
    }
}
