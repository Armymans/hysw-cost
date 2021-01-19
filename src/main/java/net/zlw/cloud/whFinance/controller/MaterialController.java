package net.zlw.cloud.whFinance.controller;


import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.whFinance.domain.vo.BomTableVo;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import net.zlw.cloud.whFinance.service.BomTableInformationService;
import net.zlw.cloud.whFinance.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

/**
    * @Author sjf
    * @Description // 芜湖物料
    * @Date 18:41 2020/11/26
    * @Param
    * @return
 **/
@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private BomTableInformationService bomTableInformationService;

    /**
     * @Author Armyman
     * @Description //芜湖财务物料编码接口
     * @Date 15:22 2021/1/19
     **/
    @RequestMapping(value = "/api/getMaterielFinance", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getMaterielFinance(@RequestBody MaterieVo materieVo, String account){
        materialService.getMaterialservice(materieVo,account);
        return RestUtil.success();
    }

    /**
     * @Author Armyman
     * @Description //芜湖财务甲供
     * @Date 15:22 2021/1/19
     **/
    @RequestMapping(value = "/api/getProvidedAFinance", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getProvidedAFinance(@RequestBody BomTableVo bomTableVo, String account){
        bomTableInformationService.getBomTable(bomTableVo,account);
        return RestUtil.success();
    }

    /**
     * @Author Armyman
     * @Description //芜湖财务物料编码接口
     * @Date 15:22 2021/1/19
     **/
    @RequestMapping(value = "/api/getMaterielFinanceOfWj", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getMaterielFinanceOfWj(@RequestBody MaterieVo materieVo, String account){
        materialService.getMaterialserviceOfWj(materieVo,account);
        return RestUtil.success();
    }
}
