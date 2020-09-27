package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BudgetingVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BudgetingDao extends Mapper<Budgeting> {

    @Select("select id id,amount_cost amountCost from budgeting")
    List<BudgetingVo> findAllBudgeting(PageBVo pageBVo);
}
