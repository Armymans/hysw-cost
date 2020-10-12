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

    @Select("SELECT\n" +
            "\tb.id id,\n" +
            "\tb.amount_cost amountCost,\n" +
            "\tbp.project_num projectNum,\n" +
            "\tbp.should_be shouldBe,\n" +
            "\tbp.cea_num ceaNum,\n" +
            "\tbp.project_num projectNum,\n" +
            "\tbp.project_name projectName,\n" +
            "\tbp.district district,\n" +
            "\tbp.water_address waterAddress,\n" +
            "\tbp.construction_unit constructionUnit,\n" +
            "\tbp.design_category designCategory,\n" +
            "\tbp.water_supply_type waterSupplyType,\n" +
            "\tbp.customer_name customerName,\n" +
            "\tb.budgeting_people budgetingPeople,\n" +
            "\tcp.cost_together costTogether,\n" +
            "\tve.pricing_together pricingTogether,\n" +
            "\tb.amount_cost amountCost,\n" +
            "\tcp.cost_total_amount costTotalAmount,\n" +
            "\tve.bidding_price_control biddingPriceControl \n" +
            "FROM\n" +
            "\tbudgeting b\n" +
            "\tLEFT JOIN base_project bp ON b.base_project_id = bp.id\n" +
            "\tLEFT JOIN cost_preparation cp ON b.cost_preparation_id = cp.id\n" +
            "\tLEFT JOIN very_establishment ve ON b.very_establishment_id = ve.id")
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
