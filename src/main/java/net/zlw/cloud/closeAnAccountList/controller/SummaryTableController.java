package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.SummaryTableService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.SummaryTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname SummaryTableController
 * @Description TODO
 * @Date 2020/11/3 10:07
 * @Created by sjf
 */
@RestController
public class SummaryTableController {

    @Autowired
    private SummaryTableService summaryTableService;

    /***
     * 下家结算送审汇总表查看
     * @param id
     * @return
     */
    @RequestMapping(value = "/cover/summaryTableList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectSummaryTableServiceList(String id){

        List<SummaryTable> summaryTables = summaryTableService.summaryTableList(id);


        return RestUtil.success(summaryTables);
    }
}
