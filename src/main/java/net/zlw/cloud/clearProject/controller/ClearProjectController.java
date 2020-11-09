package net.zlw.cloud.clearProject.controller;

import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.service.impl.BudgetingServiceImpl;
import net.zlw.cloud.clearProject.model.Budgeting;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.clearProject.model.ClearProject;
import net.zlw.cloud.clearProject.model.vo.ClearProjectVo;
import net.zlw.cloud.clearProject.model.vo.PageVo;
import net.zlw.cloud.clearProject.service.CallForBidsService;
import net.zlw.cloud.clearProject.service.ClearProjectService;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author dell
 * @Date 2020/9/28 16:37
 * @Version 1.0
 */
@RestController
public class ClearProjectController extends BaseController {

    @Resource
    private ClearProjectService clearProjectService;
    
    @Resource
    private BudgetingServiceImpl budgetingService;

    @Resource
    private CallForBidsService callForBidsService;

    /**
     * 新增--确定
     * @param clearProjectVo
     * @return
     */

    @RequestMapping(value = "/clearProject/addclearProject",method = {RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addClearProject(ClearProjectVo clearProjectVo){

        UserInfo loginUser = getLoginUser();

        clearProjectService.addClearProject(clearProjectVo,loginUser);

        return RestUtil.success("新增成功");
    }

    /**
     * 分页查询所有
     * @param pageRequest
     * @return
     */

    @RequestMapping(value = "/clearProject/findAllClearProject",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllClearProject(PageRequest pageRequest){
        System.out.println("PageRequest:"+pageRequest);
//        List<ClearProject> allClearProject = clearProjectService.findAllClearProject(pageRequest, getLoginUser());
        PageInfo<ClearProject> allClearProject = clearProjectService.findAllClearProject(pageRequest, getLoginUser());
        return RestUtil.page(allClearProject);

    }


    /**
     * 分页查询招标项目
     *
     */
    @RequestMapping(value = "/clearProject/findAllCallForBids",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllCallForBids(PageVo pageVo){

        PageInfo<CallForBids> all = callForBidsService.findAll(pageVo);

        return RestUtil.page(all);
    }

    @RequestMapping(value = "/clearProject/selectCallForBidById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findById(@RequestParam(name = "id")String id){

        CallForBids byId = callForBidsService.findById(id);
        return RestUtil.success(byId);
    }


    /**
     * 新建--项目名称--预算编制回显
     * @return
     */
    @RequestMapping(value = "/clearProject/findAllByFounderId",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllByFounderId(){
//        UserInfo loginUser = getLoginUser();
//        List<Budgeting> allByFounderId = budgetingService.findAllByFounderId(loginUser.getId());
        List<Budgeting> allByFounderId = budgetingService.findAllByFounderId("user282");
        return RestUtil.success(allByFounderId);
    }

    /**
     * 采购项目匹配
     * @return
     */

    @RequestMapping(value = "/clearProject/findBudgetingByBudgetStatus",method = {RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBudgetingByBudgetStatus(){
//        UserInfo loginUser = getLoginUser();
//        List<Budgeting> budgetingByBudgetStatus = budgetingService.findBudgetingByBudgetStatus(loginUser.getId());
        List<Budgeting> budgetingByBudgetStatus = budgetingService.findBudgetingByBudgetStatus("user282");
        return RestUtil.success(budgetingByBudgetStatus);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/clearProject/deleteClearProject",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteClearProject(@RequestParam(name = "id") String id){
        System.err.println(id);
        clearProjectService.deleteClearProject(id);
        return RestUtil.success("删除成功");
    }
    

}
