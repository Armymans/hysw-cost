package net.zlw.cloud.employeePerformance.service;

import net.zlw.cloud.designProject.mapper.EmployeeAchievementsInfoMapper;
import net.zlw.cloud.designProject.model.EmployeeAchievementsInfo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpPageVo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpVo;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Classname employeePerformanceService
 * @Description TODO
 * @Date 2020/12/17 19:50
 * @Created by sjf
 */
@Service
public class EmployeePerformanceService {

    @Autowired
    private EmployeeAchievementsInfoMapper employeeAchievementsInfoMapper;

    @Autowired
    private MemberManageDao memberManageDao;

    // 查询所有
    public List<EmpVo> employeePerformanceFindAll(EmpPageVo empPageVo) {
        List<EmpVo> list = employeeAchievementsInfoMapper.employeePerformanceFindAll(empPageVo);
        return list;
    }

    // 删除
    public void deleteEmp(String id) {
        Example example = new Example(EmployeeAchievementsInfo.class);
        example.createCriteria().andEqualTo("id", id)
                .andEqualTo("delFlag", "0");
        EmployeeAchievementsInfo achievementsInfo = employeeAchievementsInfoMapper.selectOneByExample(example);
        achievementsInfo.setDelFlag("1");
        employeeAchievementsInfoMapper.updateByPrimaryKeySelective(achievementsInfo);
    }

    // 根据id 回显
    public EmployeeAchievementsInfo selectOne(String id) {
        EmployeeAchievementsInfo achievementsInfo = employeeAchievementsInfoMapper.selectEmpById(id);
        return achievementsInfo;
    }

    public void giveOut(String id, String money) {
        String data = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date());
        EmployeeAchievementsInfo achievementsInfo = employeeAchievementsInfoMapper.selectEmpById(id);
        BigDecimal balance = achievementsInfo.getBalance(); //余额
        if ("".equals(money) || money == null) {
            throw new RuntimeException("请输入要发放的金额");
        } else if (new BigDecimal(money).compareTo(balance) == 1) {
            throw new RuntimeException("本次绩效发放金额大于绩效余额，请重新输入");
            //如果发放金额等于余额，修改实际提金额( 实际提 + 本次发放绩效)； 应计提金额归零 ,发放状态修改
        } else if (new BigDecimal(money).compareTo(balance) == 0) {
            BigDecimal actualAmount = achievementsInfo.getActualAmount();
            actualAmount = actualAmount.add(new BigDecimal(money));
            achievementsInfo.setActualAmount(actualAmount);
            achievementsInfo.setBalance(new BigDecimal(0));
            achievementsInfo.setOverFlag("1"); // 发放完结0否 1是
            achievementsInfo.setUpdateTime(data);
            employeeAchievementsInfoMapper.updateByPrimaryKeySelective(achievementsInfo);
            //如果发放金额小于余额,增加实际提金额( 实际提 + 本次发放绩效)；
        } else if (new BigDecimal(money).compareTo(balance) == -1) {
            BigDecimal actualAmount = achievementsInfo.getActualAmount();
            //建议计提金额
            actualAmount = actualAmount.add(new BigDecimal(money));
            achievementsInfo.setActualAmount(actualAmount);
            //余额
            BigDecimal subtract = balance.subtract(new BigDecimal(money));
            achievementsInfo.setBalance(subtract);
            achievementsInfo.setActualAmount(actualAmount);
            achievementsInfo.setUpdateTime(data);
            employeeAchievementsInfoMapper.updateByPrimaryKeySelective(achievementsInfo);
        }
    }

    //统一发放
    public void unifiedIssuing(String ids) {
        String data = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date());
        String[] split = ids.split(",");
        for (String thisId : split) {
            EmployeeAchievementsInfo empInfo = employeeAchievementsInfoMapper.selectByPrimaryKey(thisId);
            if (empInfo != null) {
                empInfo.setBalance(new BigDecimal(0)); //余额
                empInfo.setActualAmount(empInfo.getAccruedAmount()); // 建议计提金额，发放完成要与应计提金额一样
                empInfo.setOverFlag("1"); //发放完成
                empInfo.setUpdateTime(data);
                employeeAchievementsInfoMapper.updateByPrimaryKeySelective(empInfo);
            }
        }
    }
}
