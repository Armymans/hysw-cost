package net.zlw.cloud.demo;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.dao.BomTable1Dao;
import net.zlw.cloud.excel.dao.BomTableInfomationDao;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/30 14:44
 **/
@RestController
public class DemoController {

    @Autowired
    private BomTableInfomationDao bomTableInfomationDao;

    @Autowired
    private BomTable1Dao bomTable1Dao;

    @Autowired
    private FinalReportMapper finalReportMapper;

    /**
     * @Author Armyman
     * @Description //测试清单查看数据
     * @Date 14:49 2020/10/30
     **/
    @RequestMapping(value = "/demo/getBomTableInfomation", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> getBomTableInfomation(@RequestParam("id") String id){
        BomTableInfomation bomTableInfomation = bomTableInfomationDao.selectByBudeingId(id);
        if(bomTableInfomation != null){
            List<BomTable> bomTable = bomTable1Dao.selectByBomTableInfomationId(bomTableInfomation.getId());
            bomTableInfomation.setBomTableList(bomTable);
        }else{
            return RestUtil.error("暂无清单");
        }
        return RestUtil.success(bomTableInfomation);
    }

    /**
     * @Author Armyman
     * @Description //测试报告编辑-查询
     * @Date 14:49 2020/10/30
     **/
    @RequestMapping(value = "/demo/getFinalReport", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> getfinalReport(@RequestParam("id") String id){
        FinalReport finalReport = finalReportMapper.selectByProjectId(id);
        return RestUtil.success(finalReport);
    }

    /**
     * @Author Armyman
     * @Description //测试报告编辑-存储
     * @Date 14:49 2020/10/30
     **/
    @RequestMapping(value = "/demo/saveFinalReport", method = {RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> getfinalReport(FinalReport finalReport){
        if(!"".equals(finalReport.getId()) && finalReport.getId() != null){
            finalReportMapper.updateByPrimaryKeySelective(finalReport);
        }else{
            finalReport.setId(UUID.randomUUID().toString());
            finalReportMapper.insertSelective(finalReport);
        }
        return RestUtil.success();
    }



}
