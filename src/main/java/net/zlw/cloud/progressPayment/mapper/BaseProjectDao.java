package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.budgeting.model.vo.PageBVo;
import net.zlw.cloud.buildingProject.model.vo.PageBaseVo;
import net.zlw.cloud.buildingProject.model.vo.ProVo;
import net.zlw.cloud.designProject.model.AnhuiMoneyinfo;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.WujiangMoneyInfo;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.vo.VisaBaseProjectVo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BaseProjectDao extends Mapper<BaseProject> {

    @Select("select " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "bt.outsourcing outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(s.take_time,l.take_time) takeTime, " +
            "IFNULL(s.compile_time,l.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "a.auditor_id auditorId, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            "LEFT JOIN audit_info a on a.base_project_id = IFNULL(s.id,l.id)" +
            "where  " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "l.del_flag = '0' and " +
            "s.del_flag = '0'" +
            "ORDER BY si.create_time DESC   ")
    List<AccountsVo> findAllAccounts(PageVo pageVo);


    @Select("select * from base_project where id = #{id}")
    net.zlw.cloud.clearProject.model.BaseProject fingById(@Param("id")String id);


    @Select("SELECT  " +
            "s1.id,  "+
            "s1.should_be shouldBe, " +
            "s1.cea_num ceaNum, " +
            "s1.project_num projectNum, " +
            "s1.project_name projectName, " +
            "s1.district district, " +
            "s1.water_address waterAddress, " +
            "s1.construction_unit construction, " +
            "s1.design_category designCategory, " +
            "s1.water_supply_type waterSupplyType, " +
            "s1.customer_name customerName, " +
            "s2.budgeting_people budgetingPeople, " +
            "s3.cost_together costTogether, " +
            "s4.pricing_together pricingTogether, " +
            "s2.amount_cost amountCost, " +
            "s3.cost_total_amount costTotalAmount, " +
            "s4.bidding_price_control biddingPriceControl " +
            "FROM " +
            "base_project s1  " +
            "LEFT JOIN budgeting s2 ON s2.base_project_id = s1.id " +
            "LEFT JOIN cost_preparation s3 ON s3.base_project_id = s1.id " +
            "LEFT JOIN very_establishment s4 ON s4.base_project_id = s1.id " +
            "WHERE 1=1 " +
            "AND " +
            "s1.del_flag = 0 " +
            "AND " +
            "s2.del_flag = 0 " +
            "AND " +
            "s3.del_flag = 0 " +
            "AND  " +
            "s4.del_flag = 0 " +
            "AND " +
            "(s1.district = #{district} OR #{district} ='') " +
            "AND  " +
            "(s1.design_category = #{designCategory} OR #{designCategory} ='') " +
            "AND  " +
            "(s1.water_supply_type = #{waterSupplyType} OR #{waterSupplyType} = '') " +
            "AND( " +
            "s1.cea_num LIKE CONCAT('%',#{keyWord},'%') OR " +
            "s1.project_num LIKE CONCAT('%',#{keyWord},'%') OR " +
            "s1.project_name LIKE CONCAT('%',#{keyWord},'%')  " +
            ")")
    List<VisaBaseProjectVo> findByBaseProject(VisaBaseProjectVo visaBaseProjectVo);

    @Select("SELECT " +
            "* " +
            "FROM " +
            "base_project b " +
            "WHERE " +
            "b.building_project_id = #{id}")
    List<BaseProject> findByBuildingProject(@Param("id") String id);

    @Select("select " +
            "b.id id , " +
            "(case b.should_be " +
            "  when '0' then '是' " +
            "  when '1' then '否' " +
            "  end " +
            ") shouldBe , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case district  " +
            "  when '1' then '芜湖' " +
            "  when '2' then '马鞍山' " +
            "  when '3' then '江北' " +
            "  when '4' then '吴江' " +
            "  end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.design_category " +
            "  when '1' then '市政管道' " +
            "  when '2' then '管网改造' " +
            "  when '3' then '新建小区' " +
            "  when '4' then '二次供水项目' " +
            "  when '5' then '工商户' " +
            "  when '6' then '居民装接水' " +
            "  when '7' then '行政事业' " +
            "  end " +
            ") designCategory, " +
            "(case b.water_supply_type  " +
            "  when '1' then '直供水' " +
            "  when '2' then '二次供水' " +
            "  end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "d.designer designer, " +
            "d.take_time takeTime, " +
            "d.blueprint_start_time blueprintStartTime " +
            "from design_info d  " +
            "LEFT JOIN base_project b on d.base_project_id = b.id " +
            "where  " +
            "b.desgin_status = '4' and  " +
            "(select id from budgeting bt where bt.base_project_id = b.id and bt.del_flag = '0') is null and " +
            "(b.district = #{district} or #{district} = '') and  " +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and  " +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or " +
            "b.project_num like concat('%',#{keyword},'%') or " +
            "b.project_name like concat('%',#{keyword},'%') or  " +
            "b.construction_unit like concat('%',#{keyword},'%') or " +
            "b.customer_name like concat ('%',#{keyword},'%')   " +
            ") and " +
            " b.del_flag = '0' ")
    List<DesignInfo> findDesignAll(PageBVo pageBVo);

    @Select("select * from base_project where  " +
            "(district = #{district} or #{district} = '--' or #{district} = '') and  " +
            "(create_time > #{statTime} or #{statTime} = '--' or #{statTime} = '') and  " +
            "(create_time < #{endTime} or #{endTime} = '--' or #{endTime} = '') " +
            "order by create_time desc limit 0,5")
    List<BaseProject> findAllBaseProject(pageVo pageVo);

    @Select("SELECT * FROM base_project WHERE merge_flag = '0' AND id = #{id}")
    BaseProject findBaseProject(@Param("id") String id);

    @Select(
            "SELECT " +
                    "b.id id, " +
                    "b.cea_num ceaNum, " +
                    "b.budget_status budgetStatus, " +
                    "b.project_num projectNum, " +
                    "b.project_name projectName, " +
                    "b.application_num applicationNum, " +
                    "b.water_address waterAddress, " +
                    "b.construction_unit constructionUnit, " +
                    "b.contacts, " +
                    "b.contact_number, " +
                    "b.customer_name customerName, " +
                    "b.customer_phone customerPhone, " +
                    "b.this_declaration thisDeclaration, " +
                    "b.agent, " +
                    "b.agent_phone, " +
                    "b.application_date applicationDate, " +
                    "b.business_location businessLocation, " +
                    "b.business_types businessTypes, " +
                    "b.water_use waterUse, " +
                    "b.fire_table_size fireTableSize, " +
                    "b.classification_caliber classificationCaliber, " +
                    "b.water_meter_diameter waterMeterDiameter, " +
                    "b.site site, " +
                    "b.system_number systemNumber, " +
                    "b.proposer proposer, " +
                    "b.application_number applicationNumber, " +
                    "b.construction_organization constructionOrganization, " +
                    "b.management_table managementTable, " +
                    "(CASE  " +
                    "      b.a_b " +
                    "  WHEN '1' THEN 'A'  " +
                    "  WHEN '2' THEN 'B'  " +
                    "  END  " +
                    "  ) ab, " +
                    " (CASE  " +
                    "      b.subject  " +
                    "  WHEN '1' THEN '居民住户'  " +
                    "  WHEN '2' THEN '开发商'  " +
                    "  WHEN '3' THEN '政府事业'  " +
                    "  WHEN '4' THEN '工商户'  " +
                    "  WHEN '5' THEN '芜湖华衍'  " +
                    "  END  " +
                    "  ) subject, " +
                    " (  " +
                    "  CASE " +
                    "      b.district  " +
                    "  WHEN '1' THEN '芜湖'  " +
                    "  WHEN '2' THEN '马鞍山'  " +
                    "  WHEN '3' THEN '江北'  " +
                    "  WHEN '4' THEN '吴江'  " +
                    "  END " +
                    "  ) district, " +
                    " (  " +
                    " CASE  " +
                    "     b.project_category  " +
                    " WHEN '1' THEN '住宅区配套'  " +
                    " WHEN '2' THEN '商业区配套'  " +
                    " WHEN '3' THEN '工商区配套'  " +
                    " END ) projectCategory, " +
                    " (  " +
                    " CASE  " +
                    "     b.project_nature  " +
                    "   WHEN '1' THEN '新建'  " +
                    " WHEN '2' THEN '改造'  " +
                    " END  " +
                    " ) projectNature, " +
                    " ( " +
                    " CASE " +
                    " b.design_category  " +
                    " WHEN '1' THEN " +
                    " '市政管道'  " +
                    " WHEN '2' THEN " +
                    " '管网改造'  " +
                    " WHEN '3' THEN " +
                    " '新建小区'  " +
                    " WHEN '4' THEN " +
                    " '二次供水项目'  " +
                    " WHEN '5' THEN " +
                    " '工商户'  " +
                    " WHEN '6' THEN " +
                    " '居民装接水'  " +
                    " WHEN '7' THEN " +
                    " '行政事业'  " +
                    " END  " +
                    " ) designCategory, " +
                    " (  " +
                    " CASE  " +
                    " b.water_supply_type  " +
                    " WHEN '1' THEN '直供水'  " +
                    " WHEN '2' THEN '二次供水'  " +
                    " END " +
                    " )waterSupplyType " +
                    " FROM base_project b  " +
                    " LEFT JOIN cost_unit_management c ON c.id = b.construction_organization " +
                    " WHERE b.del_flag = '0' AND b.id = #{id}"
    )
    BaseProject findBaseProjectId(@Param("id") String id);

    @Select("SELECT " +
            "  b.id id, " +
            "  b.cea_num ceaNum, " +
            "  b.budget_status budgetStatus, " +
            "  b.project_num projectNum, " +
            "  b.project_name projectName, " +
            "  b.application_num applicationNum, " +
            "  b.water_address waterAddress, " +
            "  b.construction_unit constructionUnit, " +
            "  b.progress_payment_status progressPaymentStatus, " +
            "  b.contacts, " +
            "  b.contact_number, " +
            "  b.customer_name customerName, " +
            "  b.customer_phone customerPhone, " +
            "  b.this_declaration thisDeclaration, " +
            "  b.agent, " +
            "  b.agent_phone, " +
            "  b.application_date applicationDate, " +
            "  b.business_location businessLocation, " +
            "  b.business_types businessTypes, " +
            "  b.water_use waterUse, " +
            "  b.fire_table_size fireTableSize, " +
            "  b.classification_caliber classificationCaliber, " +
            "  b.water_meter_diameter waterMeterDiameter, " +
            "  b.site site, " +
            "  b.track_status trackStatus, " +
            "  b.system_number systemNumber, " +
            "  b.proposer proposer, " +
            "  b.application_number applicationNumber, " +
            "  b.construction_organization constructionOrganization, " +
            "  b.management_table managementTable, " +
            "  ( CASE b.a_b WHEN '1' THEN 'A' WHEN '2' THEN 'B' END ) ab, " +
            "  ( CASE b.should_be WHEN '0' THEN '是' WHEN '1' THEN '否' END ) shouldBe, " +
            "  ( CASE b.SUBJECT WHEN '1' THEN '居民住户' WHEN '2' THEN '开发商' WHEN '3' THEN '政府事业' WHEN '4' THEN '工商户' WHEN '5' THEN '芜湖华衍' END ) SUBJECT, " +
            "  ( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) district, " +
            "  ( CASE b.project_category WHEN '1' THEN '住宅区配套' WHEN '2' THEN '商业区配套' WHEN '3' THEN '工商区配套' END ) projectCategory, " +
            "  ( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) projectNature, " +
            "  ( " +
            "  CASE " +
            "      b.design_category  " +
            "      WHEN '1' THEN " +
            "      '市政管道'  " +
            "      WHEN '2' THEN " +
            "      '管网改造'  " +
            "      WHEN '3' THEN " +
            "      '新建小区'  " +
            "      WHEN '4' THEN " +
            "      '二次供水项目'  " +
            "      WHEN '5' THEN " +
            "      '工商户'  " +
            "      WHEN '6' THEN " +
            "      '居民装接水'  " +
            "      WHEN '7' THEN " +
            "      '行政事业'  " +
            "    END  " +
            "    ) designCategory, " +
            "    ( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) waterSupplyType  " +
            "  FROM " +
            "    base_project b  " +
            "  WHERE " +
            "  b.del_flag = '0'  " +
            "  AND b.id = #{id}")
    BaseProject findTrackBaseProjectId(@Param("id") String id);


    @Select("SELECT * FROM base_project WHERE id = #{id}")
    BaseProject findIdByStatus(@Param("id")String id);

    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "(case IFNULL(l.outsourcing,s.outsourcing)  " +
            "   when '1' then '是' " +
            "   when '2' then '否' " +
            "end" +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "a.auditor_id auditorId, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            "LEFT JOIN audit_info a on a.base_project_id = IFNULL(s.id,l.id)" +
            "where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(a.auditor_id = #{currentPeople} or #{currentPeople} = '' ) and " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0' and " +
            "a.audit_result = '0'" +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC   ")
    List<AccountsVo> findAllAccountsCheckLeader(PageVo pageVo);

    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            " ( case IFNULL(l.outsourcing,s.outsourcing) " +
            "   when '1' then '是' " +
            "   when '2' then '否' " +
            "   end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "a.auditor_id auditorId, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            "LEFT JOIN audit_info a on a.base_project_id = IFNULL(s.id,l.id)" +
            "where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(a.auditor_id = #{currentPeople} or #{currentPeople} = '' ) and " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0' and " +
            "a.audit_result = '0' and " +
            " ( IFNULL(s.founder_id,l.founder_id) = #{userId} or " +
            "a.auditor_id = #{userId} )" +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC   ")
    List<AccountsVo> findAllAccountsCheckStaff(PageVo pageVo);


    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "(case IFNULL(l.outsourcing,s.outsourcing)  " +
            "        when '1' then '是' " +
            "        when '2' then '否' " +
            "        end" +
            "      ) outsourcing , " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            " where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0' and " +
            "IFNULL(s.founder_id,l.founder_id) = #{userId}" +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC  ")
    List<AccountsVo> findAllAccountsProcessing(PageVo pageVo);

    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "(case IFNULL(l.outsourcing,s.outsourcing)  " +
            "        when '1' then '是' " +
            "        when '2' then '否' " +
            "        end" +
            "      ) outsourcing , " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            " where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = '3' or b.settle_accounts_status = '6' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0' and " +
            "IFNULL(s.founder_id,l.founder_id) = #{userId}" +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC  ")
    List<AccountsVo> findAllAccountsUn(PageVo pageVo);

    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "( case IFNULL(l.outsourcing,s.outsourcing) " +
            "   when '1' then '是' " +
            "   when '2' then '否' " +
            " end " +
            " ) outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            "where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0' " +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC  ")
    List<AccountsVo> findAllAccountsSuccess(PageVo pageVo);


    @Select("select " +
            "distinct " +
            "b.id id, " +
            "IFNULL(s.id,l.id) accountId , " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "(case b.settle_accounts_status " +
            "    when '1' then '待审核' " +
            "    when '2' then '处理中' " +
            "    when '3' then '未通过' " +
            "    when '4' then '待确认' " +
            "    when '5' then '已完成' " +
            "    end " +
            ") settleAccountsStatus, " +
            "(case b.district " +
            "    when '1' then '芜湖' " +
            "    when '2' then '马鞍山' " +
            "    when '3' then '江北' " +
            "    when '4' then '吴江' " +
            "    end " +
            ") district, " +
            "b.water_address waterAddress, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_category " +
            "    when '1' then '住宅区配套' " +
            "    when '2' then '商业区配套' " +
            "    when '3' then '工商区配套' " +
            "    end " +
            ") projectCategory, " +
            "(case b.project_nature " +
            "    when '1' then '新建' " +
            "    when '2' then '改造' " +
            "    end " +
            ") projectNature, " +
            "(case b.design_category " +
            "    when '1' then '市政管道' " +
            "    when '2' then '管网改造' " +
            "    when '3' then '新建小区' " +
            "    when '4' then '二次供水项目' " +
            "    when '5' then '工商户' " +
            "    when '6' then '居民装接水' " +
            "    when '7' then '行政事业' " +
            "    end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            "    when '1' then '直供水' " +
            "    when '2' then '二次供水' " +
            "    end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "IFNULL(s.prepare_people,l.prepare_people) preparePeople, " +
            "( case IFNULL(l.outsourcing,s.outsourcing) " +
            "   when '1' then '是' " +
            "   when '2' then '否' " +
            " end " +
            " ) outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = l.name_of_the_cost ) nameOfCostUnit, " +
            "l.review_number lReviewNumber, " +
            "si.sumbit_money sumbitMoney, " +
            "s.authorized_number authorizedNumber, " +
            "IFNULL(l.take_time,s.take_time) takeTime, " +
            "IFNULL(l.compile_time,s.compile_time) compileTime, " +
            "(case IFNULL(s.whether_account,l.whether_account) " +
            "    when '0' then '到账' " +
            "    when '1' then '未到账' " +
            "    end " +
            ") whetherAccount, " +
            "IFNULL(s.founder_id,l.founder_id) founderId  " +
            "from " +
            "budgeting bt  " +
            "LEFT JOIN base_project b on bt.base_project_id = b.id  " +
            "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id " +
            "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id " +
            "where  " +
            " si.up_and_down = '2' and " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(s.take_time,l.take_time) > #{startTime} or #{startTime} = '') and   " +
            "(IFNULL(s.take_time,l.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(s.compile_time,l.compile_time) < #{endTime} or #{endTime} = '') and   " +
            "(b.cea_num like concat('%',#{keyword},'%') or   " +
            "b.project_num like concat('%',#{keyword},'%')  or  " +
            "b.project_name like concat ('%',#{keyword},'%') or   " +
            "b.construction_unit like concat ('%',#{keyword},'%') or   " +
            "b.customer_name like concat ('%',#{keyword},'%') or   " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')) and  " +
            "bt.del_flag = '0' and  " +
            "b.del_flag = '0' and  " +
            "si.state = '0' and " +
            "IFNULL(l.del_flag,'0') = '0' and " +
            "IFNULL(s.del_flag,'0') = '0'  " +
//            "( IFNULL(s.founder_id,l.founder_id) = #{userId} )" +
            "group by IFNULL(s.id,l.id)" +
            "ORDER BY si.create_time DESC  ")
    List<AccountsVo> findAllAccountsSuccess2(PageVo pageVo);

    @Select(
            "SELECT " +
                    "COUNT(*) " +
                    "FROM " +
                    "base_project " +
                    "WHERE " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Integer selectProjectCount(pageVo pageVo);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(out_money,0)),0) " +
                    "FROM " +
                    "out_source " +
                    "WHERE " +
                    "dept = '2' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectOutMoney(pageVo pageVo);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(accrued_amount,0)),0) " +
                    "FROM " +
                    "employee_achievements_info " +
                    "WHERE " +
                    "dept = '2' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectAchievements(pageVo pageVo);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(in_money,0)),0) " +
                    "FROM " +
                    "in_come " +
                    "WHERE " +
                    "dept = '2' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectIncome(pageVo pageVo);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(in_money,0)),0) " +
                    "FROM " +
                    "in_come " +
                    "WHERE " +
                    "dept = '1' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectIncome2(pageVo pageVo);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(out_money,0)),0) " +
                    "FROM " +
                    "out_source " +
                    "WHERE " +
                    "dept = '1' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectOutMoney2(pageVo pageVo);


    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(accrued_amount,0)),0) " +
                    "FROM " +
                    "employee_achievements_info " +
                    "WHERE " +
                    "dept = '1' " +
                    "AND " +
                    "(district = #{district} or #{district} = '') " +
                    "AND " +
                    "create_time >= #{statTime} " +
                    "AND " +
                    "(create_time <= #{endTime} or #{endTime} = '')"
    )
    Double selectAchievements2(pageVo pageVo);
    @Select("SELECT * FROM base_project WHERE id = #{id}")
    BaseProject findById(@Param("id") String id);

    @Select("SELECT  " +
            "  s1.id id,  " +
            "  s1.cea_num ceaNum," +
            "  s1.district,  " +
            "  s1.project_num projectNum,  " +
            "  s1.project_name projectName,  " +
            "  s1.create_time createTime,  " +
            "  ( CASE project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,  " +
            "  s1.construction_unit constructionUnit,  " +
            "  ( CASE IFNULL( merge_flag, 'xxxx' ) WHEN '1' THEN '未合并' WHEN 'xxxx' THEN '未合并' WHEN '0' THEN '已合并' END ) AS mergeFlag,  " +
            "  (  " +
            "CASE  " +
            "  design_category   " +
            "  WHEN '1' THEN  " +
            "  '市政管道'   " +
            "  WHEN '2' THEN  " +
            "  '管网改造'   " +
            "  WHEN '3' THEN  " +
            "  '新建小区'   " +
            "  WHEN '4' THEN  " +
            "  '二次供水项目'   " +
            "  WHEN '5' THEN  " +
            "  '工商户'   " +
            "  WHEN '6' THEN  " +
            "  '居民装接水'   " +
            "  WHEN '7' THEN  " +
            "  '行政事业'   " +
            "END   " +
            "  ) AS designCategory,  " +
            "  s1.water_supply_type waterSupplyType,  " +
            "  s1.customer_name customerName,  " +
            "  s1.water_address waterAddress,  " +
            "  s2.amount_cost amountCost,  " +
            "  IFNULL( s3.compile_time, s4.compile_time ) compileTime,  " +
            "  project_flow projectFlow   " +
            "FROM  " +
            "  base_project s1  " +
            "  LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id  " +
            "  LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id  " +
            "  LEFT JOIN last_settlement_review s4 ON s1.id = s4.base_project_id   " +
            "WHERE  " +
            "  (  " +
            " IFNULL( s3.compile_time, s4.compile_time ) >= #{startTime}  or #{startTime}  = '')  " +
            "   " +
            " AND (  " +
            "   IFNULL( s3.compile_time, s4.compile_time ) <= #{endTime} or #{endTime}='')  " +
            "     " +
            "   AND (  " +
            " design_category = #{designCategory} or #{designCategory} = '')  " +
            "   " +
            " AND (  " +
            " project_nature = #{projectNature} or #{projectNature} = '')  " +
            "   " +
            " AND (  " +
            " merge_flag = #{mergeFlag} or #{mergeFlag}= '')  " +
            "   " +
            " AND (  " +
            " cea_num LIKE CONCAT(  " +
            " '%',#{keyword},'%') or  " +
            " project_num LIKE CONCAT(  " +
            " '%',#{keyword},'%') or  " +
            " project_name LIKE CONCAT(  " +
            " '%',#{keyword},'%')  or  " +
            " construction_unit LIKE CONCAT(  " +
            " '%',#{keyword},'%')  or  " +
            " water_supply_type LIKE CONCAT(  " +
            " '%',#{keyword},'%')  or  " +
            " customer_name LIKE CONCAT( '%', #{keyword},'%')  " +
            " )  " +
            " AND s1.del_flag = '0'" +
            " ORDER BY  " +
            "s1.create_time DESC")
    List<ProVo> selectBaseProjectFindAll(PageBaseVo pageVo);

    @Select("SELECT revenue FROM anhui_money_info WHERE base_project_id = #{id}")
    AnhuiMoneyinfo selectByAnHuiOfficialReceipts(@Param("id") String id);

    @Select("SELECT revenue FROM wujiang_money_info WHERE base_project_id = #{id}")
    WujiangMoneyInfo selectByWuJiangOfficialReceipts(@Param("id") String id);

    @Select("select * from base_project where application_num = #{applicationNum}")
    BaseProject findByApplicationNum(@Param("applicationNum") String applicationNum);
}
