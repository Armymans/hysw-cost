package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.snsEmailFile.service.EmailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Armyman
 * @Description 邮件接口
 * @Date 2020/10/9 16:48
 **/
@RestController
public class EmailInfoController extends BaseController {

    @Autowired
    private EmailInfoService emailInfoService;



}
