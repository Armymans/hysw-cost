package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BudgetingListVo;
import net.zlw.cloud.budgeting.model.vo.PageBVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BudgetingDao extends Mapper<Budgeting> {

    @Select("select " +
            "bt.id id, " +
            "b.id baseId, " +
            "( case b.should_be " +
            "    when '0' then '是' " +
            "    when '1' then '否' " +
            "    end " +
            ") shouldBe, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( case b.budget_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '已完成' " +
            "    end " +
            ") budgetStatus, " +
            "( case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "( case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "( case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "( case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "bt.budgeting_people budgetingPeople, " +
            "c.cost_together costTogether, " +
            "v.pricing_together pricingTogether, " +
            "( case bt.outsourcing " +
            "    when '1' then '是' " +
            "    when '2' then '否' " +
            "    end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "c.cost_total_amount costTotalAmount, " +
            "v.bidding_price_control biddingPriceControl, " +
            "bt.receipt_time receiptTime, " +
            "bt.budgeting_time budgetingTime, " +
            "c.receiving_time receivingTime, " +
            "c.cost_preparation_time costPreparationTime, " +
            "v.receiving_time veryReceivingTime, " +
            "v.establishment_time establishmentTime, " +
            "a.auditor_id auditorId ,  " +
            "a.audit_result auditResult , " +
            "( case bt.whether_account " +
            "    when '0' then '已到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount , " +
            "bt.amount_outsourcing amountOutsourcing,  " +
            "bt.founder_id founderId  " +
            "from  " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id " +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id " +
            "LEFT JOIN audit_info a on bt.id = a.base_project_id " +
            "where  " +
            "(b.district = #{district} or #{district} = '') and  " +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.should_be = #{shouldBe} or #{shouldBe} = '') and  " +
            "(bt.whether_account = #{whetherAccount} or #{whetherAccount} = '') and  " +
            "(bt.budgeting_time > #{startTime} or #{startTime} = '') and  " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and " +
            "(b.budget_status = #{budgetingStatus} or #{budgetingStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or " +
            "b.project_num like concat('%',#{keyword},'%') or " +
            "b.project_name like concat('%',#{keyword},'%') or  " +
            "b.construction_unit like concat('%',#{keyword},'%') or " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "c.del_flag = '0' and " +
            "v.del_flag = '0' " +
            "order by " +
            "b.should_be asc, " +
            "b.create_time desc")
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


    @Select("select   " +
            "            DISTINCT    " +
            "            bt.id id,   " +
            "            b.id baseId,     " +
            "            b.cea_num ceaNum,   " +
            "            b.project_num projectNum,   " +
            "            b.project_name projectName,   " +
            "            b.budget_status budgetStatus,      " +
            "            b.water_address waterAddress,   " +
            "            b.construction_unit constructionUnit,   " +
            "            b.customer_name customerName,   " +
            "            bt.budgeting_people budgetingPeople,   " +
            "            c.cost_together costTogether,   " +
            "            v.pricing_together pricingTogether,   " +
            "            bt.outsourcing outsourcing,   " +
            "            bt.name_of_cost_unit nameOfCostUnit,   " +
            "            bt.amount_cost amountCost,   " +
            "            c.cost_total_amount costTotalAmount,   " +
            "            v.bidding_price_control biddingPriceControl,   " +
            "            bt.receipt_time receiptTime,   " +
            "            bt.budgeting_time budgetingTime,   " +
            "            c.receiving_time receivingTime,   " +
            "            c.cost_preparation_time costPreparationTime,   " +
            "            v.receiving_time veryReceivingTime,   " +
            "            v.establishment_time establishmentTime,  " +
            "            (CASE b.water_supply_type      " +
            "            WHEN '1' THEN '直供水'  " +
            "            WHEN '2' THEN '二次供水'  " +
            "            END)waterSupplyType,  " +
            "            (CASE b.should_be  " +
            "            WHEN '0' THEN '是'  " +
            "            WHEN '1' THEN '否'  " +
            "            END)shouldBe,  " +
            "            (CASE b.project_nature      " +
            "            WHEN '1' THEN '新建'  " +
            "            WHEN '2' THEN '改造'  " +
            "            END)projectNature,  " +
            "            (CASE b.district   " +
            "            WHEN '1' THEN '芜湖'  " +
            "            WHEN '2' THEN '马鞍山'  " +
            "            WHEN '3' THEN '江北'  " +
            "            WHEN '4' THEN '吴江'  " +
            "            END)district,  " +
            "            (CASE b.design_category      " +
            "            WHEN '1' THEN '市政管道'  " +
            "            WHEN '2' THEN '管网改造'  " +
            "            WHEN '3' THEN '新建小区'  " +
            "            WHEN '4' THEN '二次供水项目'  " +
            "            WHEN '5' THEN '工商户'  " +
            "            WHEN '6' THEN '居民装接水'  " +
            "            WHEN '7' THEN '行政事业'  " +
            "            END)designCategory  " +
            "            from    " +
            "            budgeting bt    " +
            "            LEFT JOIN base_project b on bt.base_project_id = b.id   " +
            "            LEFT JOIN cost_preparation c on bt.id = c.budgeting_id   " +
            "            LEFT JOIN very_establishment v on bt.id = v.budgeting_id   " +
            "            LEFT JOIN audit_info a on bt.id = a.base_project_id   " +
            "            where " +
            "            (b.district = #{district} or #{district} = '') and    " +
            "            (b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and    " +
            "            (b.design_category = #{designCategory} or #{designCategory} = '') and    " +
            "            (   " +
            "            b.cea_num like concat('%',#{keyword},'%') or   " +
            "            b.project_num like concat('%',#{keyword},'%') or   " +
            "            b.project_name like concat('%',#{keyword},'%') or    " +
            "            b.construction_unit like concat('%',#{keyword},'%') or   " +
            "            b.customer_name like concat ('%',#{keyword},'%') or    " +
            "            bt.name_of_cost_unit like concat  ('%',#{keyword},'%')   " +
            "            ) and   " +
            "            b.del_flag = '0' and   " +
            "            b.budget_status = '4' and   " +
            "            bt.del_flag = '0'    " +
            "            order by b.should_be asc")
    List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo);

    @Select("SELECT * FROM budgeting WHERE id = #{id}")
    Budgeting findIdByStatus();




    @Select("select" +
            "             bt.id id," +
            "             b.id baseId," +
            "             b.project_num projectNum," +
            "             b.project_name projectName," +
            "             b.water_address waterAddress," +
            "             b.construction_unit constructionUnit" +
            "             from " +
            "             budgeting bt " +
            "             LEFT JOIN base_project b on bt.base_project_id = b.id" +
            "             where " +
            "             b.del_flag = '0' and   " +
            "             b.budget_status = '4' and (" +
            "             bt.clear_status != '1' or bt.clear_status is null ) and" +
            "             bt.del_flag = '0' and (" +
            "             b.project_name like concat ('%',#{keyword},'%') or " +
            "             b.project_num like concat ('%',#{keyword},'%') " +
            "              )")
    List<BudgetingListVo> findClearProjectAll(PageBVo pageBVo);

    @Select("select" +
            "  bt.id id, " +
            "  b.id baseId, " +
            "  b.project_num projectNum, " +
            "  b.project_name projectName " +
            "  from  " +
            "  budgeting bt  " +
            "  LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "  where " +
            "  bt.id = #{id}  " +
            "  and  " +
            "  b.del_flag = '0' and    " +
            "  b.budget_status = '4' and ( " +
            "  bt.clear_status != '1' or bt.clear_status is null) and" +
            "  bt.del_flag = '0' ")
    Budgeting findBudgeting(@Param("id") String id);

    @Select("select " +
            "distinct " +
            "bt.id id, " +
            "b.id baseId, " +
            "( case b.should_be " +
            "    when '0' then '是' " +
            "    when '1' then '否' " +
            "    end " +
            ") shouldBe, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( case b.budget_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '已完成' " +
            "    end " +
            ") budgetStatus, " +
            "( case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "( case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "( case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "( case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "bt.budgeting_people budgetingPeople, " +
            "c.cost_together costTogether, " +
            "v.pricing_together pricingTogether, " +
            "( case bt.outsourcing " +
            "    when '1' then '是' " +
            "    when '2' then '否' " +
            "    end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "c.cost_total_amount costTotalAmount, " +
            "v.bidding_price_control biddingPriceControl, " +
            "bt.receipt_time receiptTime, " +
            "bt.budgeting_time budgetingTime, " +
            "c.receiving_time receivingTime, " +
            "c.cost_preparation_time costPreparationTime, " +
            "v.receiving_time veryReceivingTime, " +
            "v.establishment_time establishmentTime, " +
            "a.auditor_id auditorId ,  " +
            "a.audit_result auditResult , " +
            "( case bt.whether_account " +
            "    when '0' then '已到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount , " +
            "bt.amount_outsourcing amountOutsourcing,  " +
            "bt.founder_id founderId  " +
            "from  " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id " +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id " +
            "LEFT JOIN audit_info a on bt.id = a.base_project_id " +
            "where  " +
            "(b.district = #{p.district} or #{p.district} = '') and  " +
            "(b.water_supply_type = #{p.waterSupplyType} or #{p.waterSupplyType} = '') and  " +
            "(b.project_nature = #{p.projectNature} or #{p.projectNature} = '') and  " +
            "(b.should_be = #{p.shouldBe} or #{p.shouldBe} = '') and  " +
            "(bt.whether_account = #{p.whetherAccount} or #{p.whetherAccount} = '') and  " +
            "(a.auditor_id = #{p.currentPeople} or #{p.currentPeople} = '' ) and " +
            "(bt.budgeting_time > #{p.startTime} or #{p.startTime} = '') and  " +
            "(bt.budgeting_time < #{p.endTime} or #{p.endTime} = '') and " +
            "(b.budget_status = #{p.budgetingStatus} or #{p.budgetingStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{p.keyword},'%') or " +
            "b.project_num like concat('%',#{p.keyword},'%') or " +
            "b.project_name like concat('%',#{p.keyword},'%') or  " +
            "b.construction_unit like concat('%',#{p.keyword},'%') or " +
            "b.customer_name like concat ('%',#{p.keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{p.keyword},'%') " +
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "c.del_flag = '0' and " +
            "v.del_flag = '0' and (" +
            "bt.founder_id = #{id} or " +
            "a.auditor_id = #{id} ) and " +
            "a.audit_result = '0' " +
            "order by " +
            "b.should_be asc, " +
            "b.create_time desc")
    List<BudgetingListVo> findAllBudgetingCheckStaff(@Param("p")PageBVo pageBVo, @Param("id") String id);

    @Select("select " +
            "distinct " +
            "bt.id id, " +
            "b.id baseId, " +
            "( case b.should_be " +
            "    when '0' then '是' " +
            "    when '1' then '否' " +
            "    end " +
            ") shouldBe, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( case b.budget_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '已完成' " +
            "    end " +
            ") budgetStatus, " +
            "( case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "( case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "( case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "( case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "bt.budgeting_people budgetingPeople, " +
            "c.cost_together costTogether, " +
            "v.pricing_together pricingTogether, " +
            "( case bt.outsourcing " +
            "    when '1' then '是' " +
            "    when '2' then '否' " +
            "    end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "c.cost_total_amount costTotalAmount, " +
            "v.bidding_price_control biddingPriceControl, " +
            "bt.receipt_time receiptTime, " +
            "bt.budgeting_time budgetingTime, " +
            "c.receiving_time receivingTime, " +
            "c.cost_preparation_time costPreparationTime, " +
            "v.receiving_time veryReceivingTime, " +
            "v.establishment_time establishmentTime, " +
            "a.auditor_id auditorId ,  " +
            "a.audit_result auditResult , " +
            "( case bt.whether_account " +
            "    when '0' then '已到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount , " +
            "bt.amount_outsourcing amountOutsourcing,  " +
            "bt.founder_id founderId  " +
            "from  " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id " +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id " +
            "LEFT JOIN audit_info a on bt.id = a.base_project_id " +
            "where  " +
            "(b.district = #{district} or #{district} = '') and  " +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.should_be = #{shouldBe} or #{shouldBe} = '') and  " +
            "(bt.whether_account = #{whetherAccount} or #{whetherAccount} = '') and  " +
            "(bt.budgeting_time > #{startTime} or #{startTime} = '') and  " +
            "(a.auditor_id = #{currentPeople} or #{currentPeople} = '' ) and " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and " +
            "(b.budget_status = #{budgetingStatus} or #{budgetingStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or " +
            "b.project_num like concat('%',#{keyword},'%') or " +
            "b.project_name like concat('%',#{keyword},'%') or  " +
            "b.construction_unit like concat('%',#{keyword},'%') or " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "c.del_flag = '0' and " +
            "v.del_flag = '0' and " +
            "a.audit_result = '0' " +
            "order by " +
            "b.should_be asc, " +
            "b.create_time desc")
    List<BudgetingListVo> findAllBudgetingCheckLeader(PageBVo pageBVo);

    @Select("select " +
            "distinct " +
            "bt.id id, " +
            "b.id baseId, " +
            "( case b.should_be " +
            "    when '0' then '是' " +
            "    when '1' then '否' " +
            "    end " +
            ") shouldBe, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( case b.budget_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '已完成' " +
            "    end " +
            ") budgetStatus, " +
            "( case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "( case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "( case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "( case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "bt.budgeting_people budgetingPeople, " +
            "c.cost_together costTogether, " +
            "v.pricing_together pricingTogether, " +
            "( case bt.outsourcing " +
            "    when '1' then '是' " +
            "    when '2' then '否' " +
            "    end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "c.cost_total_amount costTotalAmount, " +
            "v.bidding_price_control biddingPriceControl, " +
            "bt.receipt_time receiptTime, " +
            "bt.budgeting_time budgetingTime, " +
            "c.receiving_time receivingTime, " +
            "c.cost_preparation_time costPreparationTime, " +
            "v.receiving_time veryReceivingTime, " +
            "v.establishment_time establishmentTime, " +
            "( case bt.whether_account " +
            "    when '0' then '已到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount , " +
            "bt.amount_outsourcing amountOutsourcing,  " +
            "bt.founder_id founderId  " +
            "from  " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id " +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id " +

            "where  " +
            "(b.district = #{p.district} or #{p.district} = '') and  " +
            "(b.water_supply_type = #{p.waterSupplyType} or #{p.waterSupplyType} = '') and  " +
            "(b.project_nature = #{p.projectNature} or #{p.projectNature} = '') and  " +
            "(b.should_be = #{p.shouldBe} or #{p.shouldBe} = '') and  " +
            "(bt.whether_account = #{p.whetherAccount} or #{p.whetherAccount} = '') and  " +
            "(bt.budgeting_time > #{p.startTime} or #{p.startTime} = '') and  " +
            "(bt.budgeting_time < #{p.endTime} or #{p.endTime} = '') and " +
            "(b.budget_status = #{p.budgetingStatus} or #{p.budgetingStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{p.keyword},'%') or " +
            "b.project_num like concat('%',#{p.keyword},'%') or " +
            "b.project_name like concat('%',#{p.keyword},'%') or  " +
            "b.construction_unit like concat('%',#{p.keyword},'%') or " +
            "b.customer_name like concat ('%',#{p.keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{p.keyword},'%') " +
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "c.del_flag = '0' and " +
            "v.del_flag = '0' and " +
            "bt.founder_id = #{id} " +
            "order by " +
            "b.should_be asc, " +
            "b.create_time desc")
    List<BudgetingListVo> findAllBudgetingProcessing(@Param("p")PageBVo pageBVo, @Param("id") String id);

    @Select("select " +
            "distinct " +
            "bt.id id, " +
            "b.id baseId, " +
            "( case b.should_be " +
            "    when '0' then '是' " +
            "    when '1' then '否' " +
            "    end " +
            ") shouldBe, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( case b.budget_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '已完成' " +
            "    end " +
            ") budgetStatus, " +
            "( case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "( case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "( case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "( case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "bt.budgeting_people budgetingPeople, " +
            "c.cost_together costTogether, " +
            "v.pricing_together pricingTogether, " +
            "( case bt.outsourcing " +
            "    when '1' then '是' " +
            "    when '2' then '否' " +
            "    end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "c.cost_total_amount costTotalAmount, " +
            "v.bidding_price_control biddingPriceControl, " +
            "bt.receipt_time receiptTime, " +
            "bt.budgeting_time budgetingTime, " +
            "c.receiving_time receivingTime, " +
            "c.cost_preparation_time costPreparationTime, " +
            "v.receiving_time veryReceivingTime, " +
            "v.establishment_time establishmentTime, " +
            "( case bt.whether_account " +
            "    when '0' then '已到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount , " +
            "bt.amount_outsourcing amountOutsourcing,  " +
            "bt.founder_id founderId  " +
            "from  " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id " +
            "LEFT JOIN cost_preparation c on bt.id = c.budgeting_id " +
            "LEFT JOIN very_establishment v on bt.id = v.budgeting_id " +
            "where  " +
            "(b.district = #{p.district} or #{p.district} = '') and  " +
            "(b.water_supply_type = #{p.waterSupplyType} or #{p.waterSupplyType} = '') and  " +
            "(b.project_nature = #{p.projectNature} or #{p.projectNature} = '') and  " +
            "(b.should_be = #{p.shouldBe} or #{p.shouldBe} = '') and  " +
            "(bt.whether_account = #{p.whetherAccount} or #{p.whetherAccount} = '') and  " +
            "(bt.budgeting_time > #{p.startTime} or #{p.startTime} = '') and  " +
            "(bt.budgeting_time < #{p.endTime} or #{p.endTime} = '') and " +
            "(b.budget_status = #{p.budgetingStatus} or #{p.budgetingStatus} = '') and  " +
            "(bt.attribution_show = #{p.attributionShow} or #{p.attributionShow} = '' ) and " +
            "( " +
            "b.cea_num like concat('%',#{p.keyword},'%') or " +
            "b.project_num like concat('%',#{p.keyword},'%') or " +
            "b.project_name like concat('%',#{p.keyword},'%') or  " +
            "b.construction_unit like concat('%',#{p.keyword},'%') or " +
            "b.customer_name like concat ('%',#{p.keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{p.keyword},'%') " +
            ") and " +
            "b.del_flag = '0' and " +
            "bt.del_flag = '0' and " +
            "c.del_flag = '0' and " +
            "v.del_flag = '0'  " +
            "order by " +
            "b.should_be asc, " +
            "b.create_time desc")
    List<BudgetingListVo> findAllBudgetingCompleted(@Param("p")PageBVo pageBVo, @Param("id") String id);

    @Select("select * from budgeting where base_project_id = #{id}")
    Budgeting findByBaseId(@Param("id")String id);
}
