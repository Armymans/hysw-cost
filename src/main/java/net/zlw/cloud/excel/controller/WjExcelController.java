package net.zlw.cloud.excel.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.service.WjExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Classname 吴江模板导入
 * @Description TODO
 * @Date 2020/11/10 9:57
 * @Created by sjf
 */

@RestController
public class WjExcelController {


    @Autowired
    private WjExcelService wjExcelService;


    /**
        * @Author sjf
        * @Description //吴江封面导入
        * @Date 11:12 2020/11/10
        * @Param
        * @return
     **/
    @RequestMapping(value = "/wjExcel/coverImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> coverImport(@RequestParam(name = "id") String id){
        wjExcelService.coverWjExcelImport(id);
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //单位工程投标报价汇总表导入
        * @Date 11:40 2020/11/10
        * @Param 
        * @return 
     **/
    @RequestMapping(value = "/wjExcel/summaryUnitsImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> summaryUnitsImport(){
        wjExcelService.summaryUnitsImport();
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description 分部分项工程量清单计价表-导入
        * @Date 16:21 2020/11/10
        * @Param 
        * @return 
     **/
    @RequestMapping(value = "/wjExcel/partTableQuantitiesImport",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> partTableQuantitiesImport(@RequestParam(name = "id") String id){
//        wjExcelService.partTableQuantitiesImport();
        wjExcelService.partTableQuantitiesImport(id);
        return RestUtil.success();
    }
    
}
