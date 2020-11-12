package net.zlw.cloud.excel.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.service.AhExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Classname 新点安徽导入
 * @Description TODO
 * @Date 2020/11/10 19:06
 * @Created by sjf
 */
@RestController
public class AhExcelController {


    @Autowired
    private AhExcelService ahExcelService;

    /**
     * @return
     * @Author sjf
     * @Description //安徽封面表
     * @Date 10:00 2020/11/11
     * @Param
     **/
    @RequestMapping(value = "/anHuiExcel/aHCoverImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> aHCoverImport(@RequestParam(name = "id") String id) {
        ahExcelService.coverImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description // 安徽单位汇总表导入
        * @Date 10:51 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/summarySheetImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> summarySheetImport(@RequestParam(name = "id") String id) {
        ahExcelService.summarySheetImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //安徽分部分项清单计价表导入
        * @Date 11:27 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/quantitiesPartialWorksImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> quantitiesPartialWorksImport(@RequestParam(name = "id") String id) {
        ahExcelService.quantitiesPartialWorksImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //安徽不可竞争项目清单与计价表导入
        * @Date 14:03 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/competitiveItemValuationImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> competitiveItemValuationImport(@RequestParam(name = "id") String id) {
        ahExcelService.competitiveItemValuationImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //安徽税金计价表导入
        * @Date 15:27 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/taxStatementImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> taxStatementImport(@RequestParam(name = "id") String id) {
        ahExcelService.taxStatementImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //安徽甲供材料表导入
        * @Date 15:54 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/summaryMaterialsSuppliedAImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> summaryMaterialsSuppliedAImport(@RequestParam(name = "id") String id) {
        ahExcelService.summaryMaterialsSuppliedAImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //安徽乙供材料表导入
        * @Date 16:03 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/anHuiExcel/summaryMaterialsSuppliedBImport", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> summaryMaterialsSuppliedBImport(@RequestParam(name = "id") String id) {
        ahExcelService.summaryMaterialsSuppliedBImport(id);
        return RestUtil.success();
    }

    @RequestMapping(value = "/anHuiExcel/excelRead", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> allExcel(MultipartFile file,String id) {
        ahExcelService.excelRead(file,id);
        return RestUtil.success();
    }
}
