package net.zlw.cloud.closeAnAccountList.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.closeAnAccountList.service.VerificationSheetService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.excel.model.VerificationSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Classname 下家结算汇总核定单查看
 * @Description TODO
 * @Date 2020/11/3 10:20
 * @Created by sjf
 */
@RestController
public class VerificationSheetController {


    @Autowired
    private VerificationSheetService verificationSheetService;

    @RequestMapping(value = "/cover/VerificationSheet",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(String id){

        VerificationSheet verificationSheet = verificationSheetService.verificationSheetList(id);
        return RestUtil.success(verificationSheet);
    }
}
