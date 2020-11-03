package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.MaterialAnalysisService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.MaterialAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname MaterialAnalysisController
 * @Description TODO
 * @Date 2020/11/3 15:29
 * @Created by sjf
 */
@RestController
public class MaterialAnalysisController {

    @Autowired
    private MaterialAnalysisService materialAnalysisService;

    /***
     * 甲供材料分析表-查看(下家结算汇总表)查看
     * @param id
     * @return
     */
    @RequestMapping(value = "/cover/materialAnalysisList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(String id){
        List<MaterialAnalysis> materialAnalysisList = materialAnalysisService.findMaterialAnalysisList(id);
        return RestUtil.success(materialAnalysisList);
    }

}
