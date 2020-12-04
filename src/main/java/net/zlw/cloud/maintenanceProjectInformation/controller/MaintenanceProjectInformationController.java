package net.zlw.cloud.maintenanceProjectInformation.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.RestUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationReturnVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.service.MaintenanceProjectInformationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author dell
 * @Date 2020/9/27 11:06
 * @Version 1.0
 */
@RestController
//@RequestMapping("/api/maintenanceProjectInformation")
public class MaintenanceProjectInformationController extends BaseController {
    @Resource
    private MaintenanceProjectInformationService maintenanceProjectInformationService;

    /**
     * @Author sjf
     * @Description //检维修 模糊查找
     * @Date 11:20 2020/11/22
     * @Param
     * @return
     **/
    @RequestMapping(value = "/maintenanceProjectInformation/selectAllMaintenance", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectAllMaintenance(PageRequest pageRequest) {
        //全部
        Page page = new Page();
        pageRequest.setType("0");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        //全部
        Page page1 = new Page();
        pageRequest.setType("1");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo1 = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());

        //全部
        Page page2 = new Page();
        pageRequest.setType("2");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo2 = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());

        //全部
        Page page3 = new Page();
        pageRequest.setType("3");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo3 = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());

        //全部
        Page page4 = new Page();
        pageRequest.setType("4");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo4 = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());

        //全部
        Page page5 = new Page();
        pageRequest.setType("5");
        PageInfo<MaintenanceProjectInformationReturnVo> pageInfo5 = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        page5.setData(pageInfo5.getList());
        page5.setPageNum(pageInfo5.getPageNum());
        page5.setPageSize(pageInfo5.getPageSize());
        page5.setTotalCount(pageInfo5.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);
        map.put("table6",page5);

        return RestUtil.success(map);

    }

    /**
     * 检维修列表数据-全部
     *
     * @param pageRequest
     * @return
     */
//    @PostMapping("/findAllMaintenanceProjectInformation")
//
    @RequestMapping(value = "/maintenanceProjectInformation/findAllMaintenanceProjectInformation", method = {RequestMethod.POST,RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> findAllMaintenanceProjectInformation(PageRequest pageRequest) {
        System.out.println("PageRequest:" + pageRequest);
        PageInfo<MaintenanceProjectInformationReturnVo> allMaintenanceProjectInformation = maintenanceProjectInformationService.findAllMaintenanceProjectInformation(pageRequest, getLoginUser());
        return net.zlw.cloud.common.RestUtil.page(allMaintenanceProjectInformation);
    }


    /**
     * 检维修--删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/deleteMaintenanceProjectInformation", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> deleteMaintenanceProjectInformationController(@RequestParam(name = "id") String id) {
        maintenanceProjectInformationService.deleteMaintenanceProjectInformation(id);
        return RestUtil.success("删除成功");
    }

    /**
     * 批量审核
     *
     * @param batchReviewVo
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/batchReview", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> batchReview(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        //切割字符串id
        if (split.length > 0) {
            for (String s : split) {
                if (StringUtil.isNotEmpty(s)) {
                    batchReviewVo.setBatchAll(s);
                    maintenanceProjectInformationService.batchReview(batchReviewVo,getLoginUser());
                }
            }
        }else{
            return RestUtil.error("审核有误请联系管理员");
        }
        return RestUtil.success("审核成功");
    }

    /**
     * 新增--提交
     * 提交：把数据保存到数据库，得到一个审核人 id
     *
     * @param maintenanceProjectInformationVo
     */
    @RequestMapping(value = "/maintenanceProjectInformation/addMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
//        UserInfo loginUser = getLoginUser();
//        System.out.println("user:"+loginUser);
        maintenanceProjectInformationService.addMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("新增提交成功");
    }


    /**
     * 根据id 回显数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/selectMaintenanceProjectInformationById", method = {RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectMaintenanceProjectInformationById(String id,String userId) {
        MaintenanceVo maintenanceVo = maintenanceProjectInformationService.selectMaintenanceProjectInformationById(id,userId,getLoginUser());
        return RestUtil.success(maintenanceVo);
    }


    /**
     * 新增-保存
     *
     * @param maintenanceProjectInformationVo
     * @return
     */
    @RequestMapping(value = "/maintenanceProjectInformation/saveMaintenanceProjectInformation", method = {RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
//        UserInfo loginUser = getLoginUser();
        maintenanceProjectInformationService.saveMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());

        return RestUtil.success("保存成功");
    }

    /**
     * 修改 - 提交
     *
     * @param maintenanceProjectInformationVo
     */
    @RequestMapping(value = "/maintenanceProjectInformation/updateMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
        UserInfo loginUser = getLoginUser();
        System.out.println("user:" + loginUser);
        maintenanceProjectInformationService.updateMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("编辑成功");
    }

    // 修改-保存
    @RequestMapping(value = "/maintenanceProjectInformation/updateSaveMaintenanceProjectInformation", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateSaveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo) {
        UserInfo loginUser = getLoginUser();
        System.out.println("user:" + loginUser);
        maintenanceProjectInformationService.updateSaveMaintenanceProjectInformation(maintenanceProjectInformationVo, getLoginUser());
        return RestUtil.success("编辑成功");
    }


}
