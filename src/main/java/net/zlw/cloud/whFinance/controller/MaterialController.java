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

@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private BomTableInformationService bomTableInformationService;

    @RequestMapping(value = "/api/getMaterielFinance", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getMaterielFinance(@RequestBody MaterieVo materieVo, String account){
        materialService.getMaterialservice(materieVo,account);
        return RestUtil.success();
    }

    @RequestMapping(value = "/api/getProvidedAFinance", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getProvidedAFinance(@RequestBody BomTableVo bomTableVo, String account){
        bomTableInformationService.getBomTable(bomTableVo,account);
        return RestUtil.success();
    }
}
