package net.zlw.cloud.excelLook.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Classname AnHuiXinDianController
 * @Description TODO
 * @Date 2020/11/11 17:13
 * @Created by sjf
 */
@RestController
public class AnHuiXinDianController {

    /**
        * @Author sjf
        * @Description //安徽封面查看
        * @Date 17:13 2020/11/11
        * @Param
        * @return
     **/
    @RequestMapping(value = "/excelLook/anhuiCover",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> getList(@RequestParam("id") String id){

        return RestUtil.success();
    }
}
