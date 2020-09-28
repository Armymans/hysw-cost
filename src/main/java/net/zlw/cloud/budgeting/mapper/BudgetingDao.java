package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BudgetingVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BudgetingDao extends Mapper<Budgeting> {

    @Select("select b.id id,b.amount_cost amountCost,bp.project_num projectNum from budgeting b \n" +
            "LEFT JOIN base_project bp on b.base_project_id = bp.id")
    List<BudgetingVo> findAllBudgeting(PageBVo pageBVo);

    /**
     * 清标--新建--项目名称下拉列表数据查询
     * @param founderId
     * @return
     */
    @Select("select * from budgeting where del_flag = '0' and founder_id = #{founderId}")
    List<net.zlw.cloud.clearProject.model.Budgeting> findBudgetingByFounderId(@Param("founderId") String founderId);



    @Select("select * from budgeting where del_flag = '0' and founder_id = #{founderId} and base_project_id =(select base_project.id from base_project where budget_status = 4)")
    List<net.zlw.cloud.clearProject.model.Budgeting> findBudgetingByBudgetStatus(@Param("founderId") String founderId);


    @Select("select * from budgeting where id = #{id}")
    net.zlw.cloud.clearProject.model.Budgeting findById(@Param("id") String id);
}
