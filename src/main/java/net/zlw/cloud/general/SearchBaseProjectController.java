package net.zlw.cloud.general;

import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/baseProject")
public class SearchBaseProjectController {
    @Resource
    private BaseProjectService baseProjectService;

    //模糊搜索项目编号
    @GetMapping("/{name}")
    public List<BaseProject> findBaseProject(@PathVariable(name = "name") String name){
      List<BaseProject> list =  baseProjectService.findBaseProject(name);
      return list;
    }
    //查询所有工程项目
    @GetMapping
    public List<BaseProject> findAllBaseProject(){
      return  baseProjectService.findAllBaseProject();
    }

    @GetMapping("findById/{id}")
    public BaseProject findById(@PathVariable(name = "id") String id){
       return baseProjectService.findById(id);
    }

}
