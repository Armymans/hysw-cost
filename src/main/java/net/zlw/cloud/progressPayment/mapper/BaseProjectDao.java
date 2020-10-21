package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.budgeting.model.vo.PageBVo;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BaseProjectDao extends Mapper<BaseProject> {

    @Select("select\n" +
            "b.id id,\n" +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "(case b.settle_accounts_status\n" +
            "\t\twhen '1' then '待审核'\n" +
            "\t\twhen '2' then '处理中'\n" +
            "\t\twhen '3' then '未通过'\n" +
            "\t\twhen '4' then '待确认'\n" +
            "\t\twhen '5' then '已完成'\n" +
            "\t\tend\n" +
            ") settleAccountsStatus,\n" +
            "(case b.district\n" +
            "\t\twhen '1' then '芜湖'\n" +
            "\t\twhen '2' then '马鞍山'\n" +
            "\t\twhen '3' then '江北'\n" +
            "\t\twhen '4' then '吴江'\n" +
            "\t\tend\n" +
            ") district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "(case b.project_category\n" +
            "\t\twhen '1' then '住宅区配套'\n" +
            "\t\twhen '2' then '商业区配套'\n" +
            "\t\twhen '3' then '工商区配套'\n" +
            "\t\tend\n" +
            ") projectCategory,\n" +
            "(case b.project_nature\n" +
            "\t\twhen '1' then '新建'\n" +
            "\t\twhen '2' then '改造'\n" +
            "\t\tend\n" +
            ") projectNature,\n" +
            "(case b.design_category\n" +
            "\t\twhen '1' then '市政管道'\n" +
            "\t\twhen '2' then '管网改造'\n" +
            "\t\twhen '3' then '新建小区'\n" +
            "\t\twhen '4' then '二次供水项目'\n" +
            "\t\twhen '5' then '工商户'\n" +
            "\t\twhen '6' then '居民装接水'\n" +
            "\t\twhen '7' then '行政事业'\n" +
            "\t\tend\n" +
            ") designCategory,\n" +
            "(case b.water_supply_type\n" +
            "\t\twhen '1' then '直供水'\n" +
            "\t\twhen '2' then '二次供水'\n" +
            "\t\tend\n" +
            ") waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople,\n" +
            "bt.outsourcing outsourcing,\n" +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit,\n" +
            "l.review_number lReviewNumber,\n" +
            "si.sumbit_money sumbitMoney,\n" +
            "s.authorized_number authorizedNumber,\n" +
            "IFNULL(s.take_time,l.take_time) takeTime,\n" +
            "IFNULL(s.compile_time,l.compile_time) compileTime,\n" +
            "(case IFNULL(s.whether_account,l.whether_account)\n" +
            "\t\twhen '0' then '到账'\n" +
            "\t\twhen '1' then '未到账'\n" +
            "\t\tend\n" +
            ") whetherAccount,\n" +
            "a.auditor_id auditorId\n" +
            "from\n" +
            "budgeting bt \n" +
            "LEFT JOIN base_project b on bt.base_project_id = b.id \n" +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN audit_info a on a.base_project_id = IFNULL(s.id,l.id)" +
            "where \n" +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and \n" +
            "(b.district = #{district} or #{district} = '' ) and \n" +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and \n" +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and \n" +
            "(IFNULL(l.take_time,s.take_time) > #{startTime} or #{startTime} = '') and  \n" +
            "(IFNULL(l.take_time,s.take_time) < #{endTime} or #{endTime} = '') and \n" +
            "(IFNULL(l.compile_time,s.compile_time) > #{startTime} or #{startTime} = '') and \n" +
            "(IFNULL(l.compile_time,s.compile_time) < #{endTime} or #{endTime} = '') and  \n" +
            "(b.cea_num like concat('%',#{keyword},'%') or  \n" +
            "b.project_num like concat('%',#{keyword},'%')  or \n" +
            "b.project_name like concat ('%',#{keyword},'%') or  \n" +
            "b.construction_unit like concat ('%',#{keyword},'%') or  \n" +
            "b.customer_name like concat ('%',#{keyword},'%') or  \n" +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and \n" +
            "bt.del_flag = '0' and \n" +
            "b.del_flag = '0' and \n" +
            "si.state = '0' and " +
            "l.del_flag = '0' and " +
            "s.del_flag = '0'  \n")
    List<AccountsVo> findAllAccounts(PageVo pageVo);


    @Select("select * from base_project where id = #{id}")
    net.zlw.cloud.clearProject.model.BaseProject fingById(@Param("id")String id);


    @Select("SELECT \n" +
            "s1.id, \n"+
            "s1.should_be shouldBe,\n" +
            "s1.cea_num ceaNum,\n" +
            "s1.project_num projectNum,\n" +
            "s1.project_name projectName,\n" +
            "s1.district district,\n" +
            "s1.water_address waterAddress,\n" +
            "s1.construction_unit construction,\n" +
            "s1.design_category designCategory,\n" +
            "s1.water_supply_type waterSupplyType,\n" +
            "s1.customer_name customerName,\n" +
            "s2.budgeting_people budgetingPeople,\n" +
            "s3.cost_together costTogether,\n" +
            "s4.pricing_together pricingTogether,\n" +
            "s2.amount_cost amountCost,\n" +
            "s3.cost_total_amount costTotalAmount,\n" +
            "s4.bidding_price_control biddingPriceControl\n" +
            "FROM\n" +
            "base_project s1 \n" +
            "LEFT JOIN budgeting s2 ON s2.base_project_id = s1.id\n" +
            "LEFT JOIN cost_preparation s3 ON s3.base_project_id = s1.id\n" +
            "LEFT JOIN very_establishment s4 ON s4.base_project_id = s1.id\n" +
            "WHERE 1=1\n" +
            "AND\n" +
            "s1.del_flag = 0\n" +
            "AND\n" +
            "s2.del_flag = 0\n" +
            "AND\n" +
            "s3.del_flag = 0\n" +
            "AND \n" +
            "s4.del_flag = 0\n" +
            "AND\n" +
            "(s1.district = #{district} OR #{district} ='')\n" +
            "AND \n" +
            "(s1.design_category = #{designCategory} OR #{designCategory} ='')\n" +
            "AND \n" +
            "(s1.water_supply_type = #{waterSupplyType} OR #{waterSupplyType} = '')\n" +
            "AND(\n" +
            "s1.cea_num LIKE CONCAT('%',#{keyWord},'%') OR\n" +
            "s1.project_num LIKE CONCAT('%',#{keyWord},'%') OR\n" +
            "s1.project_name LIKE CONCAT('%',#{keyWord},'%') \n" +
            ")")
    List<VisaBaseProjectVo> findByBaseProject(VisaBaseProjectVo visaBaseProjectVo);

    @Select("SELECT " +
            "* " +
            "FROM " +
            "base_project b " +
            "WHERE " +
            "b.building_project_id = #{id}")
    List<BaseProject> findByBuildingProject(@Param("id") String id);

    @Select("select\n" +
            "b.id id , " +
            "(case b.should_be\n" +
            "\twhen '0' then '是'\n" +
            "\twhen '1' then '否'\n" +
            "\tend\n" +
            ") shouldBe ,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "(case district \n" +
            "\twhen '1' then '芜湖'\n" +
            "\twhen '2' then '马鞍山'\n" +
            "\twhen '3' then '江北'\n" +
            "\twhen '4' then '吴江'\n" +
            "\tend\n" +
            ") district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "(case b.design_category\n" +
            "\twhen '1' then '市政管道'\n" +
            "\twhen '2' then '管网改造'\n" +
            "\twhen '3' then '新建小区'\n" +
            "\twhen '4' then '二次供水项目'\n" +
            "\twhen '5' then '工商户'\n" +
            "\twhen '6' then '居民装接水'\n" +
            "\twhen '7' then '行政事业'\n" +
            "\tend\n" +
            ") designCategory,\n" +
            "(case b.water_supply_type \n" +
            "\twhen '1' then '直供水'\n" +
            "\twhen '2' then '二次供水'\n" +
            "\tend\n" +
            ") waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "d.designer designer,\n" +
            "d.take_time takeTime,\n" +
            "d.blueprint_start_time blueprintStartTime\n" +
            "from design_info d \n" +
            "LEFT JOIN base_project b on d.base_project_id = b.id\n" +
            "where \n" +
            "(select id from budgeting bt where bt.base_project_id = b.id and bt.del_flag = '0') is null and\n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and \n" +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or\n" +
            "b.project_num like concat('%',#{keyword},'%') or\n" +
            "b.project_name like concat('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat('%',#{keyword},'%') or\n" +
            "b.customer_name like concat ('%',#{keyword},'%')  \n" +
            ")")
    List<DesignInfo> findDesignAll(PageBVo pageBVo);

    @Select("select * from base_project order by create_time desc limit 0,5")
    List<BaseProject> findAllBaseProject();
}
