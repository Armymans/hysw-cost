package net.zlw.cloud.designProject.controller;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.service.ProjectSumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProjectSumController {

    @Resource
    private ProjectSumService projectSumService;

    @RequestMapping(value = "/api/projectCount/AllprojectCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer AllprojectCount(){
        Integer integer = projectSumService.AllprojectCount();
        return integer;
    }

    @RequestMapping(value = "/api/projectCount/withAuditCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer withAuditCount(){
        return  projectSumService.withAuditCount();
    }

    @RequestMapping(value = "/api/projectCount/conductCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer conductCount(){
        return  projectSumService.conductCount();
    }

    @RequestMapping(value = "/api/projectCount/completeCount",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Integer completeCount(){
        return  projectSumService.completeCount();
    }
}
