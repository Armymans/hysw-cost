package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.soap.Addressing;

/**
 * @Author Armyman
 * @Description 文件接口
 * @Date 2020/10/9 16:48
 **/
@RestController
public class FileInfoController extends BaseController {

    @Addressing
    private FileInfoService fileInfoService;



}
