package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BudgetingListVo;
import net.zlw.cloud.budgeting.model.vo.BudgetingVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BudgetingDao extends Mapper<Budgeting> {

    @Select("select\n" +
            "bt.id id,\n" +
            "b.id baseId,\n" +
            "b.should_be shouldBe,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "b.budget_status budgetStatus,\n" +
            "b.district district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_nature projectNature,\n" +
            "b.design_category designCategory,\n" +
            "b.water_supply_type waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "bt.budgeting_people budgetingPeople,\n" +
            "c.cost_together costTogether,\n" +
            "v.pricing_together pricingTogether,\n" +
            "bt.outsourcing outsourcing,\n" +
            "bt.name_of_cost_unit nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "c.cost_total_amount costTotalAmount,\n" +
            "v.bidding_price_control biddingPriceControl,\n" +
            "bt.receipt_time receiptTime,\n" +
            "bt.budgeting_time budgetingTime,\n" +
            "c.receiving_time receivingTime,\n" +
            "c.cost_preparation_time costPreparationTime,\n" +
            "v.receiving_time veryReceivingTime,\n" +
            "v.establishment_time establishmentTime\n" +
            "from \n" +
            "budgeting bt \n" +
            "LEFT JOIN base_project b on bt.base_project_id = b.id\n" +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id\n" +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id\n" +
            "LEFT JOIN audit_info a on bt.id = a.base_project_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and \n" +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and \n" +
            "(b.should_be = #{shouldBe} or #{shouldBe} = '') and \n" +
            "(bt.whether_account = #{whetherAccount} or #{whetherAccount} = '') and \n" +
            "(bt.budgeting_time > #{startTime} or #{startTime} = '') and \n" +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and\n" +
            "(b.budget_status = #{budgetingStatus} or #{budgetingStatus} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or\n" +
            "b.project_num like concat('%',#{keyword},'%') or\n" +
            "b.project_name like concat('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat('%',#{keyword},'%') or\n" +
            "b.customer_name like concat ('%',#{keyword},'%') or \n" +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')\n" +
            ")")
    List<BudgetingListVo> findAllBudgeting(PageBVo pageBVo);

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

    @Select("select\n" +
            "bt.id id,\n" +
            "b.id baseId,\n" +
            "b.should_be shouldBe,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "b.budget_status budgetStatus,\n" +
            "b.district district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_nature projectNature,\n" +
            "b.design_category designCategory,\n" +
            "b.water_supply_type waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "bt.budgeting_people budgetingPeople,\n" +
            "c.cost_together costTogether,\n" +
            "v.pricing_together pricingTogether,\n" +
            "bt.outsourcing outsourcing,\n" +
            "bt.name_of_cost_unit nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "c.cost_total_amount costTotalAmount,\n" +
            "v.bidding_price_control biddingPriceControl,\n" +
            "bt.receipt_time receiptTime,\n" +
            "bt.budgeting_time budgetingTime,\n" +
            "c.receiving_time receivingTime,\n" +
            "c.cost_preparation_time costPreparationTime,\n" +
            "v.receiving_time veryReceivingTime,\n" +
            "v.establishment_time establishmentTime\n" +
            "from \n" +
            "budgeting bt \n" +
            "LEFT JOIN base_project b on bt.base_project_id = b.id\n" +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id\n" +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id\n" +
            "LEFT JOIN audit_info a on bt.id = a.base_project_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and \n" +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or\n" +
            "b.project_num like concat('%',#{keyword},'%') or\n" +
            "b.project_name like concat('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat('%',#{keyword},'%') or\n" +
            "b.customer_name like concat ('%',#{keyword},'%') or \n" +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')\n" +
            ")")
    List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo);
}
