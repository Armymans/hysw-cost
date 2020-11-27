package net.zlw.cloud.whFinance.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.service.MaterialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Classname 物料清单表
 * @Description
 * @Date 2020/11/26 18:55
 * @Created by sjf
 */
@RestController
public class MaterialInfoController {

    @Resource
    private MaterialService materialService;

    //查询所有
    @RequestMapping(value = "/materIal/selectAll", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectAll(PageRequest pageRequest){
         List<Materie> materieList = materialService.selectAll(pageRequest);
        return RestUtil.success(materieList);
    }
    //添加物料
    @RequestMapping(value = "/materIal/addMaterIal", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addMaterIal(Materie materie){
        materialService.addMaterie(materie);
        return RestUtil.success("添加成功");
    }

    // 根据id查询
    @RequestMapping(value = "/materIal/findOneMaterie", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findOneMaterie(@RequestParam(name = "id") String id){
        Materie oneMaterie = materialService.findOneMaterie(id);
        return RestUtil.success(oneMaterie);
    }

    // 修改物料
    @RequestMapping(value = "/materIal/updateMaterie", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectAll(Materie materie){
        materialService.updateMaterie(materie);
        return RestUtil.success("修改成功");
    }

    //删除物料
    @RequestMapping(value = "/materIal/deleteMaterie", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteMaterie(String id){
        materialService.deleteMaterie(id);
        return RestUtil.success("删除成功");
    }
}
