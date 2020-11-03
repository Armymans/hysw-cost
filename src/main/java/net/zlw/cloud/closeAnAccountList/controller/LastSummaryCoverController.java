package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.LastSummaryCoverService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.LastSummaryCover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
public class LastSummaryCoverController {

    @Autowired
    private LastSummaryCoverService lastSummaryCoverService;

    /***
     * 上家结算汇总表封面
     * @param id
     * @return
     */
    @RequestMapping(value = "/cover/lastSummaryCoverList",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(String id){
        LastSummaryCover lastSummaryCover = lastSummaryCoverService.getLastSummaryCover(id);
        return RestUtil.success(lastSummaryCover);
    }
}
