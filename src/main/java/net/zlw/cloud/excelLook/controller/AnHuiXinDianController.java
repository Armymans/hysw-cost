package net.zlw.cloud.excelLook.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.*;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import net.zlw.cloud.excelLook.service.AnHuiXinDianService;
import net.zlw.cloud.excelLook.service.SummaryUnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Classname 安徽新点查看
 * @Description TODO
 * @Date 2020/11/11 17:13
 * @Created by sjf
 */
@RestController
public class AnHuiXinDianController {


    @Autowired
    private AnHuiXinDianService anHuiXinDianService;
    @Resource
    private BudgetCoverService budgetCoverService;
    @Resource
    private SummaryUnitsService summaryUnitsService;


    /**
        * @Author sjf
        * @Description //安徽封面查看
        * @Date 17:13 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDCover",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findSheetOne(@RequestParam("id") String id){
        AnhuiCover one = anHuiXinDianService.findOne(id);
        if (one == null){
            SummaryShenji budgetCoverById = budgetCoverService.findBudgetCoverById(id);
            return RestUtil.success(budgetCoverById);
        }
        return RestUtil.success(one);
    }

    /**
        * @Author sjf
        * @Description //安徽单位汇总查看
        * @Date 9:31 2020/11/12
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDSheetList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findSheetList(@RequestParam("id") String id){
        List<AnhuiSummarySheet> list = anHuiXinDianService.findList(id);
        if (list == null || list.size() == 0){
            List<SummaryUnits> summaryUnits = summaryUnitsService.summaryUnitsList(id);
            return RestUtil.success(summaryUnits);
        }
        return RestUtil.success(list);
    }

    /**
     * @Author sjf
     * @Description //安徽分部分项查看
     * @Date 9:31 2020/11/12
     * @Param
     * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDWorkList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findWorkList(@RequestParam("id") String id){
        List<QuantitiesPartialWorks> list = anHuiXinDianService.findWorksList(id);
        if (list == null || list.size() == 0){
            List<PartTableQuantities> partTableQuantitiesAll = budgetCoverService.findPartTableQuantitiesAll(id);
            return RestUtil.success(partTableQuantitiesAll);
        }
        return RestUtil.success(list);
    }

    /**
        * @Author sjf
        * @Description //安徽不可竞争查看
        * @Date 9:48 2020/11/12
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDCompetitiveItemValuationList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> competitiveItemValuationList(@RequestParam("id") String id){
        List<CompetitiveItemValuation> list = anHuiXinDianService.competitiveItemValuationList(id);
        return RestUtil.success(list);
    }

    /**
        * @Author sjf
        * @Description //安徽新点税金计价表查看
        * @Date 9:53 2020/11/12
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDTaxStatementList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> taxStatementList(@RequestParam("id") String id){
        List<TaxStatement> list = anHuiXinDianService.taxStatementList(id);
        return RestUtil.success(list);
    }

    /**
        * @Author sjf
        * @Description //安徽新点甲供材料查看
        * @Date 9:55 2020/11/12
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDASummaryMaterialsSuppliedList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> MaterialsSuppliedListA(@RequestParam("id") String id){
        List<SummaryMaterialsSupplied> list = anHuiXinDianService.MaterialsSuppliedAList(id);
        return RestUtil.success(list);
    }

    /**
     * @Author sjf
     * @Description //安徽新点乙供材料查看
     * @Date 9:55 2020/11/12
     * @Param
     * @return
     **/
    @RequestMapping(value = "/anHuiExcelLook/XDBSummaryMaterialsSuppliedList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> MaterialsSuppliedListB(@RequestParam("id") String id){
        List<SummaryMaterialsSupplied> list = anHuiXinDianService.MaterialsSuppliedBList(id);
        return RestUtil.success(list);
    }
}
