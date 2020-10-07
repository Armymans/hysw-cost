package net.zlw.cloud.remindSet.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.remindSet.model.RemindSet;
import net.zlw.cloud.remindSet.service.RemindSetService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by xulei on 2020/9/20.
 */
@RestController
public class RemindSetController {

    @Resource
    private RemindSetService remindSetService;

    @RequestMapping(value = "/api/remindSet/findAll", method = {RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    private Map<String,Object> remindSetFindAll() {
        List<RemindSet> list = remindSetService.findAll();
        return RestUtil.success(list);
    }
}
