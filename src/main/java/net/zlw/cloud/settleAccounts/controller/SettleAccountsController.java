package net.zlw.cloud.settleAccounts.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//@RequestMapping("/accounts")
@RestController
public class SettleAccountsController {
    @Resource
    private SettleAccountsService settleAccountsService;
    //查询所有结算
//    @PostMapping("/findAllAccounts")
    @RequestMapping(value = "/accounts/findAllAccounts",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllAccounts(PageVo pageVo){
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo);
        PageInfo<AccountsVo> accountsVoPageInfo = new PageInfo<>(allAccounts);
        return RestUtil.page(accountsVoPageInfo);
    }
    //结算项目删除
//    @DeleteMapping("/deleteAcmcounts")
    @RequestMapping(value = "/accounts/deleteAcmcounts",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteAcmcounts(@RequestParam(name = "id") String id){
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
    @RequestMapping(value = "/accounts/updateAccount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateAccount(@RequestParam(name = "id") String id){
        String[] split = id.split(",");
        for (String s : split) {
            settleAccountsService.updateAccount(s);
        }
        return RestUtil.success();
    }
    //结算新增
//    @PostMapping("/addAccount")
    @RequestMapping(value = "/accounts/addAccount",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addAccount( BaseAccountsVo baseAccountsVo){
        try {
            settleAccountsService.addAccount(baseAccountsVo);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error();
        }
        return RestUtil.success();
    }
//    @GetMapping("/findAccountById/{id}")
    @RequestMapping(value = "/accounts/findAccountById",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAccountById(@RequestParam(name = "id") String id){
        BaseAccountsVo accountsVo = settleAccountsService.findAccountById(id);
        return RestUtil.success(accountsVo);
    }
    @PutMapping("/updateAccountById")
    public String updateAccountById(@RequestBody BaseAccountsVo baseAccountsVo){
        settleAccountsService.updateAccountById(baseAccountsVo);
        return "修改成功";
    }
    //结算批量审核
    @RequestMapping(value = "/accounts/batchReview",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        settleAccountsService.batchReview(batchReviewVo);
        return RestUtil.success();
    }

}
