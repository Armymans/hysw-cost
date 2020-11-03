package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.UnitProjectSummaryService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.UnitProjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname 单位工程造价汇总表
 * @Description TODO
 * @Date 2020/11/3 9:47
 * @Created by sjf
 */
@RestController
public class UnitProjectSummaryController {

    @Autowired
    private UnitProjectSummaryService unitProjectSummaryService;

    @RequestMapping(value = "/cover/unitProjectSummaryList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(String id){
        List<UnitProjectSummary> unitProjectSummaries = unitProjectSummaryService.unitProjectSummaryList(id);
        return RestUtil.success(unitProjectSummaries);
    }
}
