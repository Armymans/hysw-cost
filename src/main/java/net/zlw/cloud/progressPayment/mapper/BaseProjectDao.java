package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BaseProjectDao extends Mapper<BaseProject> {

    @Select("select * from base_project b  " +
            "LEFT JOIN budgeting bb on  b.id = bb.base_project_id " +
            "LEFT JOIN settlement_audit_information sai on b.id = sai.base_project_id " +
            "LEFT JOIN last_settlement_review l on b.id = l.base_project_id " +
            "LEFT JOIN audit_info au on b.id = au.base_project_id  " +
            "where  " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(l.take_time,sai.take_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(l.take_time,sai.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(l.compile_time,sai.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(l.compile_time,sai.compile_time) < #{endTime} or #{endTime} = '') and  " +
            "(b.cea_num like concat('%',#{keyword},'%') or  " +
            "b.project_num like concat('%',#{keyword},'%')  or " +
            "b.project_name like concat ('%',#{keyword},'%') or  " +
            "b.construction_unit like concat ('%',#{keyword},'%') or  " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "bb.name_of_cost_unit like concat  ('%',#{keyword},'%'))")
    List<AccountsVo> findAllAccounts(PageVo PageVo);


    @Select("select * from base_project where id = #{id}")
    net.zlw.cloud.clearProject.model.BaseProject fingById(@Param("id")String id);


    @Select("SELECT \n" +
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
}
