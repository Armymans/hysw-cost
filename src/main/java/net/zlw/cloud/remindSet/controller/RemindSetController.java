package net.zlw.cloud.remindSet.controller;

import net.zlw.cloud.remindSet.model.RemindSet;
import net.zlw.cloud.remindSet.service.RemindSetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xulei on 2020/9/20.
 */
@RestController
@RequestMapping("/api/remindSet")
public class RemindSetController {

  @Resource
  private RemindSetService remindSetService;

   @GetMapping("findAll")
    private List<RemindSet> remindSetFindAll(){
      List<RemindSet>  list = remindSetService.findAll();
       return list;
   }
}
