package net.zlw.cloud.snsEmailFile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.snsEmailFile.util.WHJdbc;
import net.zlw.cloud.whFinance.domain.vo.BomTablesVo;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import net.zlw.cloud.whFinance.service.BomTableInformationService;
import net.zlw.cloud.whFinance.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author: hjc
 * @date: 2021-03-04 17:47
 * @desc: 获取芜湖财务视图信息
 */

@RestController
public class GetWHViewDateController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private BomTableInformationService bomTableInformationService;

    /**
     * 获取芜湖财务物料订单数据v2.1
     */
    @RequestMapping(value = "/api/getWhBomTableInfo", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWhBomTableInfo(){
        try {
            // 物料清单
            ResultSet materialInfo = WHJdbc.getWhSaleOrderInfo();

            while (materialInfo.next()){
                String pushMsg = materialInfo.getString("PUSHMSG");
                // 转换数据格式
                JSONObject jsonObject = JSONObject.parseObject(pushMsg);
                BomTablesVo bomTablesVo = JSON.toJavaObject(jsonObject, BomTablesVo.class);
                if (bomTablesVo != null){
                    bomTableInformationService.getBomTable(bomTablesVo, "");
                }
            }

        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    /**
     * 获取芜湖财务物料清单数据v2.1
     */
    @RequestMapping(value = "/api/getWhMaterialInfo", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getWhMaterialInfo(){
        try {
            // 物料清单
            ResultSet materialInfo = WHJdbc.getWhMaterialInfo();

            while (materialInfo.next()){
                String pushMsg = materialInfo.getString("PUSHMSG");
                // 转换数据格式
                JSONObject jsonObject = JSONObject.parseObject(pushMsg);
                MaterieVo materieVo = JSON.toJavaObject(jsonObject, MaterieVo.class);
                if (materieVo != null){
                    materialService.getMaterialservice(materieVo);
                }
            }

        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

}
