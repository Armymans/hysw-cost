package net.zlw.cloud.depManage.controller;

import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.service.DepManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**   部门管理列表数据  事物层
 * Created by xulei on 2020/9/18.
 */
@RestController
@RequestMapping("/api/depManage")
public class DepManageController {
    @Resource
    private DepManageService depManageService;

    @GetMapping("/depManagefindAll")
    public List<DepManage> findAll(){
        List<DepManage> list =  depManageService.findAll();
        return list;
    }

}
