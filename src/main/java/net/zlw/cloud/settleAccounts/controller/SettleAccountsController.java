package net.zlw.cloud.settleAccounts.controller;

import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/accounts")
@RestController
public class SettleAccountsController {
    @Resource
    private SettleAccountsService settleAccountsService;
    //查询所有结算
    @PostMapping("/findAllAccounts")
    public List<AccountsVo> findAllAccounts(@RequestBody PageVo pageVo){
      return  settleAccountsService.findAllAccounts(pageVo);
    }
    //结算项目删除
    @DeleteMapping("/deleteAcmcounts/{id}")
    public String deleteAcmcounts(@PathVariable(name = "id") String id){
        try {
            settleAccountsService.deleteAcmcounts(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }
    //结算项目到账
    @PutMapping("/updateAccount")
    public String updateAccount(@RequestBody String id){
        String[] split = id.split(",");
        for (String s : split) {
            settleAccountsService.updateAccount(s);
        }
        return "到账成功";
    }
    //结算新增
    @PostMapping("/addAccount")
    public String addAccount(@RequestBody BaseAccountsVo baseAccountsVo){
        try {
            settleAccountsService.addAccount(baseAccountsVo);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }
    @GetMapping("/findAccountById/{id}")
    public BaseAccountsVo findAccountById(@PathVariable(name = "id") String id){
        BaseAccountsVo accountsVo = settleAccountsService.findAccountById(id);
        return accountsVo;
    }
    @PutMapping("/updateAccountById")
    public String updateAccountById(@RequestBody BaseAccountsVo baseAccountsVo){
        settleAccountsService.updateAccountById(baseAccountsVo);
        return "修改成功";
    }
}
