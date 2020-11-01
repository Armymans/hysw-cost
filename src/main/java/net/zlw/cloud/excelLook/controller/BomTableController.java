package net.zlw.cloud.excelLook.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.dao.BomTable1Dao;
import net.zlw.cloud.excel.dao.BomTableInfomationDao;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BomTableController {

    @Autowired
    private BomTable1Dao bomTable1Dao;

    @Autowired
    private BomTableInfomationDao bomTableInfomationDao;

    @RequestMapping(value = "/excel/bomTable",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){
        BomTableInfomation bomTableInfomation = bomTableInfomationDao.selectByBudeingId(id);
        if (bomTableInfomation!=null){
            List<BomTable> bomTables = bomTable1Dao.selectByBomTableInfomationId(bomTableInfomation.getId());
            bomTableInfomation.setBomTableList(bomTables);
        }

        return RestUtil.success(bomTableInfomation);
    }
}
