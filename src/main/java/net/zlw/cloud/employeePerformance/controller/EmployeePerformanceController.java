package net.zlw.cloud.employeePerformance.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.EmployeeAchievementsInfo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpPageVo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpVo;
import net.zlw.cloud.employeePerformance.service.EmployeePerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname 员工绩效发放
 * @Description TODO
 * @Date 2020/12/17 19:48
 * @Created by sjf
 */
@RestController
public class EmployeePerformanceController {

    @Autowired
    private EmployeePerformanceService employeePerformanceService;
    // 查询所有
    @RequestMapping(value = "/employeePerformance/selectAll",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> employeePerformanceFindAll(EmpPageVo empPageVo){
        List<EmpVo> list = employeePerformanceService.employeePerformanceFindAll(empPageVo);
        PageInfo<EmpVo> empVoPageInfo = new PageInfo<>(list);
        return RestUtil.page(empVoPageInfo);
    }
    // 模糊查询
    @RequestMapping(value = "/employeePerformance/fuzzyLookup",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> fuzzyLookup(EmpPageVo empPageVo){
        List<EmpVo> list = employeePerformanceService.employeePerformanceFindAll(empPageVo);
        PageInfo<EmpVo> empVoPageInfo = new PageInfo<>(list);
        Page page = new Page();
        page.setData(empVoPageInfo.getList());
        page.setTotalCount(empVoPageInfo.getTotal());
        page.setPageNum(empVoPageInfo.getPageNum());
        page.setPageSize(empVoPageInfo.getPageSize());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("table1",page);
        return RestUtil.success(map);
    }
    // 删除
    @RequestMapping(value = "/employeePerformance/deleteEmp",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteEmp(String id){
         employeePerformanceService.deleteEmp(id);
        return RestUtil.success("删除成功");
    }
    // 根据id查找回显
    @RequestMapping(value = "/employeePerformance/selectOne",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectOne(String id){
        EmployeeAchievementsInfo achievementsInfo = employeePerformanceService.selectOne(id);
        return RestUtil.success(achievementsInfo);
    }

    // 发放绩效
    @RequestMapping(value = "/employeePerformance/giveOut",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> giveOut(String id,String money){
        try {
            employeePerformanceService.giveOut(id,money);
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("发放完成");
    }

    // 统一发放
    @RequestMapping(value = "/employeePerformance/unifiedIssuing",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> unifiedIssuing(String ids){
        employeePerformanceService.unifiedIssuing(ids);
        return RestUtil.success("发放完成");
    }

}
