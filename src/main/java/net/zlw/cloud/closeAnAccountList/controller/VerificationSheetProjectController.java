package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.VerificationSheetProjectService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.VerificationSheetProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname 下家结算汇总-核定单工程列表
 * @Description TODO
 * @Date 2020/11/3 10:29
 * @Created by sjf
 */
@RestController
public class VerificationSheetProjectController {

    @Autowired
    private VerificationSheetProjectService verificationSheetProjectService;

    @RequestMapping(value = "/cover/VerificationSheetProject",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(String id){
        List<VerificationSheetProject> list = verificationSheetProjectService.getList(id);
        return RestUtil.success(list);
    }

}
