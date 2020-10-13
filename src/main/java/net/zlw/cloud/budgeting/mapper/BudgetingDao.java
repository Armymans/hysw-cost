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
            "( case b.should_be\n" +
            "\t\twhen '0' then '是'\n" +
            "\t\twhen '1' then '否'\n" +
            "\t\tend\n" +
            ") shouldBe,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "( case b.budget_status\n" +
            "    when '1' then '待审核'\n" +
            "\t\twhen '2' then '处理中'\n" +
            "\t\twhen '3' then '未通过'\n" +
            "\t\twhen '4' then '已完成'\n" +
            "\t\tend\n" +
            ") budgetStatus,\n" +
            "( case b.district\n" +
            "    when '1' then '芜湖'\n" +
            "\t\twhen '2' then '马鞍山'\n" +
            "\t\twhen '3' then '江北'\n" +
            "\t\twhen '4' then '吴江'\n" +
            "\t\tend\n" +
            ") district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "( case b.project_nature\n" +
            "    when '1' then '新建'\n" +
            "\t\twhen '2' then '改造'\n" +
            "\t\tend\n" +
            ") projectNature,\n" +
            "( case b.design_category\n" +
            "    when '1' then '市政管道'\n" +
            "\t\twhen '2' then '管网改造'\n" +
            "\t\twhen '3' then '新建小区'\n" +
            "\t\twhen '4' then '二次供水项目'\n" +
            "\t\twhen '5' then '工商户'\n" +
            "\t\twhen '6' then '居民装接水'\n" +
            "\t\twhen '7' then '行政事业'\n" +
            "\t\tend\n" +
            ") designCategory,\n" +
            "( case b.water_supply_type\n" +
            "    when '1' then '直供水'\n" +
            "\t\twhen '2' then '二次供水'\n" +
            "\t\tend\n" +
            ") waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "bt.budgeting_people budgetingPeople,\n" +
            "c.cost_together costTogether,\n" +
            "v.pricing_together pricingTogether,\n" +
            "( case bt.outsourcing\n" +
            "    when '1' then '是'\n" +
            "\t\twhen '2' then '否'\n" +
            "\t\tend\n" +
            ") outsourcing,\n" +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "c.cost_total_amount costTotalAmount,\n" +
            "v.bidding_price_control biddingPriceControl,\n" +
            "bt.receipt_time receiptTime,\n" +
            "bt.budgeting_time budgetingTime,\n" +
            "c.receiving_time receivingTime,\n" +
            "c.cost_preparation_time costPreparationTime,\n" +
            "v.receiving_time veryReceivingTime,\n" +
            "v.establishment_time establishmentTime,\n" +
            "a.auditor_id auditorId , \n" +
            "a.audit_result auditResult ,\n" +
            "( case bt.whether_account\n" +
            "    when '0' then '已到账'\n" +
            "\t\twhen '1' then '未到账'\n" +
            "\t\tend\n" +
            ") whetherAccount ,\n" +
            "bt.amount_outsourcing amountOutsourcing \n" +
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
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "bt.founder_id = #{founderId}" +
            "order by " +
            "b.should_be asc")
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
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0'  " +
            "order by b.should_be asc")
    List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo);
}
