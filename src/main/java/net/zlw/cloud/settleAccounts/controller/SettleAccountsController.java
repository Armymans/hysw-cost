package net.zlw.cloud.settleAccounts.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RequestMapping("/accounts")
@RestController
public class SettleAccountsController extends BaseController {
    @Resource
    private SettleAccountsService settleAccountsService;
    //造价单位名称
    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;

    //查询所有结算
//    @PostMapping("/findAllAccounts")
    @RequestMapping(value = "/accounts/findAllAccounts",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllAccounts(PageVo pageVo){
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo,getLoginUser());

        PageInfo<AccountsVo> accountsVoPageInfo = new PageInfo<>(allAccounts);
        return RestUtil.page(accountsVoPageInfo);
    }

    /**
     * @return
     * @Author sjf
     * @Description //结算列表 模糊查找
     * @Date 10:59 2020/11/22
     * @Param
     **/
    @RequestMapping(value = "/accounts/selectAllAccounts", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectAccounts(PageVo pageVo) {
        //全部
        Page page = new Page();
        pageVo.setSettleAccountsStatus("");
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo = new PageInfo<>(allAccounts);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());
        //待审核
        Page page1 = new Page();
        pageVo.setSettleAccountsStatus("1");
        List<AccountsVo> allAccounts1 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo1 = new PageInfo<>(allAccounts1);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());
        //处理中
        Page page2 = new Page();
        pageVo.setSettleAccountsStatus("2");
        List<AccountsVo> allAccounts2 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo2 = new PageInfo<>(allAccounts2);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());
        //未通过
        Page page3 = new Page();
        pageVo.setSettleAccountsStatus("3");
        List<AccountsVo> allAccounts3 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo3 = new PageInfo<>(allAccounts3);
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());
        //待确认
        Page page4 = new Page();
        pageVo.setSettleAccountsStatus("4");
        List<AccountsVo> allAccounts4 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo4 = new PageInfo<>(allAccounts4);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());
        //已完成
        Page page5 = new Page();
        pageVo.setSettleAccountsStatus("5");
        List<AccountsVo> allAccounts5 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo5 = new PageInfo<>(allAccounts5);
        page5.setData(pageInfo5.getList());
        page5.setPageNum(pageInfo5.getPageNum());
        page5.setPageSize(pageInfo5.getPageSize());
        page5.setTotalCount(pageInfo5.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1", page);
        map.put("table2", page1);
        map.put("table3", page2);
        map.put("table4", page3);
        map.put("table5", page4);
        map.put("table6", page5);
        return RestUtil.success(map);
    }


    //结算项目删除
//    @DeleteMapping("/deleteAcmcounts")
    @RequestMapping(value = "/accounts/deleteAcmcounts", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> deleteAcmcounts(@RequestParam(name = "id") String id) {
        try {
            settleAccountsService.deleteAcmcounts(id);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error();
        }
        return RestUtil.success();
    }

    //结算项目到账
//    @PutMapping("/updateAccount")
    @RequestMapping(value = "/accounts/updateAccount", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateAccount(@RequestParam(name = "id") String id,@RequestParam(name = "checkWhether") String checkWhether) {
        String[] split = id.split(",");
        for (String s : split) {
            try {
                settleAccountsService.updateAccount(s,getLoginUser(),checkWhether);
            } catch (Exception e) {
                return RestUtil.error(e.getMessage());
            }
        }
        return RestUtil.success();
    }

    //结算新增
//    @PostMapping("/addAccount")
    @RequestMapping(value = "/accounts/addAccount", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> addAccount(BaseAccountsVo baseAccountsVo) {
        try {
//            System.err.println(baseAccountsVo.getSettlementInfo());
//            System.err.println(baseAccountsVo.getLastSettlementInfo());
//            System.err.println("******************");
//            System.err.println(baseAccountsVo.getSettlementAuditInformation());
//            System.err.println(baseAccountsVo.getLastSettlementReview());
//            System.err.println(baseAccountsVo.getInvestigationOfTheAmount());
            settleAccountsService.addAccount(baseAccountsVo, getLoginUser());
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    //根据ID查询结算
//    @GetMapping("/findAccountById/{id}")
    @RequestMapping(value = "/accounts/findAccountById", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> findAccountById(@RequestParam(name = "id", required = false) String id) {
        if (id == null) {
            return RestUtil.error();
        }
        BaseAccountsVo accountsVo = settleAccountsService.findAccountById(id, getLoginUser());
        String nameOfTheCost = accountsVo.getLastSettlementReview().getNameOfTheCost();
        if (nameOfTheCost!=null && !"".equals(nameOfTheCost)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfTheCost);
            if (costUnitManagement!=null){
                accountsVo.getLastSettlementReview().setNameOfTheCost(costUnitManagement.getCostUnitName());
            }
        }
        String nameOfTheCost1 = accountsVo.getSettlementAuditInformation().getNameOfTheCost();
        if (nameOfTheCost1!=null && !"".equals(nameOfTheCost1)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfTheCost1);
            if (costUnitManagement!=null){
                accountsVo.getSettlementAuditInformation().setNameOfTheCost(costUnitManagement.getCostUnitName());
            }
        }
        String sumbitMoney = accountsVo.getSettlementInfo().getSumbitMoney();
        BigDecimal subtractTheNumber = accountsVo.getSettlementAuditInformation().getSubtractTheNumber();
        BigDecimal bigDecimal = new BigDecimal(sumbitMoney);
        System.err.println(subtractTheNumber);
        System.err.println(bigDecimal);
        if (subtractTheNumber!=null && bigDecimal!=null){
            BigDecimal divide = subtractTheNumber.divide(bigDecimal,4,RoundingMode.HALF_UP);
            BigDecimal multiply = divide.multiply(new BigDecimal(100));
            BigDecimal bigDecimal1 = multiply.setScale(2, RoundingMode.HALF_UP);
            accountsVo.getSettlementAuditInformation().setSubtractRate(bigDecimal1);
        }
        return RestUtil.success(accountsVo);
    }

    //结算编辑
//    @PutMapping("/updateAccountById")
    @RequestMapping(value = "/accounts/updateAccountById", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateAccountById(BaseAccountsVo baseAccountsVo) {
        try {
            settleAccountsService.updateAccountById(baseAccountsVo, getLoginUser());
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    //结算批量审核
    @RequestMapping(value = "/accounts/batchReview", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> batchReview(BatchReviewVo batchReviewVo) {
        settleAccountsService.batchReview(batchReviewVo,getLoginUser());
        return RestUtil.success();
    }

    //其他信息查看
    @RequestMapping(value = "/otherInfo/selectInfoList", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectInfoList(String baseId) {
        List<OtherInfo> otherInfos = settleAccountsService.selectInfoList(baseId);
        return RestUtil.success(otherInfos);
    }

}
