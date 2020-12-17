package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProgressPaymentInformationDao extends Mapper<ProgressPaymentInformation> {

    @Select(
  "SELECT  " +
          "current_payment_Information " +
          "FROM  " +
          "progress_payment_information " +
          "WHERE " +
          "base_project_id = #{id} " +
          "GROUP BY " +
          "base_project_id " +
          "ORDER BY " +
          "create_time desc"
    )
    List<String> NewcurrentPaymentInformation(@Param("id") String id);

    @Select(
  "SELECT " +
          "SUM(current_payment_Information) " +
          "FROM " +
          "progress_payment_information " +
          "GROUP BY " +
          "base_project_id " +
          "HAVING " +
          "base_project_id = #{id}"
    )
    List<String> SumcurrentPaymentInformation(@Param("id") String id);

    @Select(
  "SELECT " +
          "SUM(current_payment_ratio) " +
          "FROM " +
          "progress_payment_information " +
          "GROUP BY " +
          "base_project_id " +
          "HAVING " +
          "base_project_id = #{id}"
    )
    String currentPaymentRatio(@Param("id") String id);

    @Select(
  "SELECT " +
          "SUM(cumulative_payment_times) " +
          "FROM " +
          "progress_payment_information " +
          "GROUP BY " +
          "base_project_id " +
          "HAVING " +
          "base_project_id = #{id}"
    )
    String cumulativePaymentTimes(@Param("id") String id);

    @Select("SELECT " +
  "p.id id, " +
  "b.id baseId, " +
  "b.cea_num ceaNum, " +
  "b.project_num projectNum, " +
  "b.project_name projectName, " +
  "( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  "b.water_address waterAddress, " +
  "b.construction_unit constructionUnit, " +
  "b.project_category projectCategory, " +
  "( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
  "( " +
  "CASE " +
  "b.design_category  " +
  "WHEN '1' THEN " +
  "'市政管道'  " +
  "WHEN '2' THEN " +
  "'管网改造'  " +
  "WHEN '3' THEN " +
  "'新建小区'  " +
  "WHEN '4' THEN " +
  "'二次供水项目'  " +
  "WHEN '5' THEN " +
  "'工商户'  " +
  "WHEN '6' THEN " +
  "'居民装接水'  " +
  "WHEN '7' THEN " +
  "'行政事业'  " +
  "END  " +
  ") AS designCategory, " +
  "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  "b.customer_name customerName, " +
  "(select member_name username from member_manage where id = p.founder_id) username, " +
  "( CASE bt.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  "bt.name_of_cost_unit nameOfCostUnit, " +
  "bt.amount_cost amountCost, " +
  "p.contract_amount contractAmount, " +
  "p.current_payment_Information currentPaymentInformation, " +
  "p.current_payment_ratio currentPaymentRatio, " +
  "p.receiving_time receivingTime, " +
  "p.compile_time compileTime " +
  "from progress_payment_information p  " +
  "LEFT JOIN base_project b on p.base_project_id = b.id  " +
  "LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  "LEFT JOIN audit_info a on p.id = a.base_project_id " +
  "where " +
  "(p.del_flag = '0') and  " +
  //审核人
  "(a.auditor_id = #{uid} or #{uid} = '') and " +
  "(b.district = #{district} or #{district} = '') and  " +
  "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  "(p.project_type = #{projectType} or #{projectType} = '') and " +
  "(p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  "(p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  "(p.compile_time > #{startTime} or #{startTime} = '') and  " +
  "(p.compile_time < #{endTime} or #{endTime} = '') and  " +
  "( " +
  "b.cea_num like concat('%',#{keyword},'%') or  " +
  "b.project_num like concat('%',#{keyword},'%') or  " +
  "b.project_name like concat ('%',#{keyword},'%') or  " +
  "b.construction_unit like concat ('%',#{keyword},'%') or  " +
  "b.customer_name like concat ('%',#{keyword},'%') or  " +
  "bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  ") and  " +
  "(b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')" +
  "ORDER BY " +
  "p.create_time desc")
    List<ProgressListVo> searchAllProgress(PageVo pageVo);


    @Select("SELECT " +
  "p.id id, " +
  "b.id baseId, " +
  "b.cea_num ceaNum, " +
  "b.project_num projectNum, " +
  "b.project_name projectName, " +
  "( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  "b.water_address waterAddress, " +
  "b.construction_unit constructionUnit, " +
  "b.project_category projectCategory, " +
  "( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
  "( " +
  "CASE " +
  "b.design_category  " +
  "WHEN '1' THEN " +
  "'市政管道'  " +
  "WHEN '2' THEN " +
  "'管网改造'  " +
  "WHEN '3' THEN " +
  "'新建小区'  " +
  "WHEN '4' THEN " +
  "'二次供水项目'  " +
  "WHEN '5' THEN " +
  "'工商户'  " +
  "WHEN '6' THEN " +
  "'居民装接水'  " +
  "WHEN '7' THEN " +
  "'行政事业'  " +
  "END  " +
  ") AS designCategory, " +
  "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  "b.customer_name customerName, " +
  "(select member_name username from member_manage where id = p.founder_id) username, " +
  "( CASE bt.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  "bt.name_of_cost_unit nameOfCostUnit, " +
  "bt.amount_cost amountCost, " +
  "p.contract_amount contractAmount, " +
  "pt.total_payment_amount totalPaymentAmount, " +
  "pt.accumulative_payment_proportion accumulativePaymentProportion, " +
  "p.current_payment_Information currentPaymentInformation, " +
  "p.current_payment_ratio currentPaymentRatio, " +
  "pt.cumulative_number_payment cumulativeNumberPayment, " +
  "p.receiving_time receivingTime, " +
  "p.compile_time compileTime " +
  "from progress_payment_information p  " +
  "LEFT JOIN base_project b on p.base_project_id = b.id  " +
  "LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  "LEFT JOIN progress_payment_total_payment pt on p.id = pt.progress_payment_id " +
  "where " +
  "(p.del_flag = '0') and  " +
  "(b.district = #{district} or #{district} = '') and  " +
  "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  "(p.project_type = #{projectType} or #{projectType} = '') and " +
  "(p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  "(p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  "(p.compile_time > #{startTime} or #{startTime} = '') and  " +
  "(p.compile_time < #{endTime} or #{endTime} = '') and  " +
  "( " +
  "b.cea_num like concat('%',#{keyword},'%') or  " +
  "b.project_num like concat('%',#{keyword},'%') or  " +
  "b.project_name like concat ('%',#{keyword},'%') or  " +
  "b.construction_unit like concat ('%',#{keyword},'%') or  " +
  "b.customer_name like concat ('%',#{keyword},'%') or  " +
  "bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  ") and  " +
  "(b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')" +
  "ORDER BY " +
  "p.create_time desc")
    List<ProgressListVo> searchAllProgress1(PageVo pageVo);

    @Select(
  "SELECT  " +
          "amount_outsourcing " +
          "FROM  " +
          "progress_payment_information s1, " +
          "base_project s2 " +
          "where " +
          "s1.base_project_id = s2.id " +
          "and " +
          "(s2.district=#{district} or  #{district}  = '') " +
          "and " +
          "s1.create_time>=#{startTime} " +
          "and " +
          "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<ProgressPaymentInformation> totalexpenditure(CostVo2 costVo2);

    @Select("SELECT current_payment_Information currentPaymentInformation FROM progress_payment_information WHERE base_project_id = #{id}")
    List<ProgressPaymentInformation> findAmount(@Param("id") String id);

    @Select("SELECT " +
  " p.id id, " +
  " b.id baseId, " +
  " b.cea_num ceaNum, " +
  " b.project_num projectNum, " +
  " b.project_name projectName, " +
  " ( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  " ( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  " b.water_address waterAddress, " +
  " b.construction_unit constructionUnit, " +
  " b.project_category projectCategory, " +
  " ( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  " ( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
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
  " ) AS designCategory, " +
  " ( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  " b.customer_name customerName, " +
  " (select member_name username from member_manage where id = p.founder_id) username, " +
  " ( CASE p.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  " bt.name_of_cost_unit nameOfCostUnit, " +
  " bt.amount_cost amountCost, " +
  " p.contract_amount contractAmount, " +
  " p.current_payment_Information currentPaymentInformation, " +
  " p.current_payment_ratio currentPaymentRatio, " +
  " p.receiving_time receivingTime, " +
  " p.compile_time compileTime " +
  " from progress_payment_information p  " +
  " LEFT JOIN base_project b on p.base_project_id = b.id  " +
  " LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  " LEFT JOIN audit_info a on p.id = a.base_project_id " +
  " where " +
  " (p.del_flag = '0')  " +
  "      and  " +
  " (a.auditor_id = #{currentPeople} or #{currentPeople} = '') and " +
  " (b.district = #{district} or #{district} = '') and  " +
  " (b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  " (p.project_type = #{projectType} or #{projectType} = '') and " +
  " (p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  " (p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  " (p.compile_time > #{startTime} or #{startTime} = '') and  " +
  " (p.compile_time < #{endTime} or #{endTime} = '') and  " +
  "      a.audit_result = '0' and  " +
  " ( " +
  " b.cea_num like concat('%',#{keyword},'%') or  " +
  " b.project_num like concat('%',#{keyword},'%') or  " +
  " b.project_name like concat ('%',#{keyword},'%') or  " +
  " b.construction_unit like concat ('%',#{keyword},'%') or  " +
  " b.customer_name like concat ('%',#{keyword},'%') or  " +
  " bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  " ) and  " +
  " (b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')   " +
  "       " +
  " ORDER BY " +
  " p.create_time desc")
    List<ProgressListVo> searchAllProgressLeader(PageVo pageVo);

    @Select("SELECT " +
  " p.id id, " +
  " b.id baseId, " +
  " b.cea_num ceaNum, " +
  " p.founder_id founderId," +
  " b.project_num projectNum, " +
  " b.project_name projectName, " +
  " ( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  " ( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  " b.water_address waterAddress, " +
  " b.construction_unit constructionUnit, " +
  " b.project_category projectCategory, " +
  " ( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  " ( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
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
  " ) AS designCategory, " +
  " ( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  " b.customer_name customerName, " +
  " (select member_name username from member_manage where id = p.founder_id) username, " +
  " ( CASE p.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  " bt.name_of_cost_unit nameOfCostUnit, " +
  " bt.amount_cost amountCost, " +
  " p.contract_amount contractAmount, " +
  " p.current_payment_Information currentPaymentInformation, " +
  " p.current_payment_ratio currentPaymentRatio, " +
  " p.receiving_time receivingTime, " +
  " p.compile_time compileTime " +
  " from progress_payment_information p  " +
  " LEFT JOIN base_project b on p.base_project_id = b.id  " +
  " LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  " LEFT JOIN audit_info a on p.id = a.base_project_id " +
  " where " +
  " (p.del_flag = '0')  " +
  "      and  " +
  " (a.auditor_id = #{currentPeople} or #{currentPeople} = '') and " +
  " (b.district = #{district} or #{district} = '') and  " +
  " (b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  " (p.project_type = #{projectType} or #{projectType} = '') and " +
  " (p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  " (p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  " (p.compile_time > #{startTime} or #{startTime} = '') and  " +
  " (p.compile_time < #{endTime} or #{endTime} = '') and  " +
  "      a.audit_result = '0' and (  " +
  "      p.founder_id = #{uid} or  " +
  "      a.auditor_id = #{uid} ) and " +
  " ( " +
  " b.cea_num like concat('%',#{keyword},'%') or  " +
  " b.project_num like concat('%',#{keyword},'%') or  " +
  " b.project_name like concat ('%',#{keyword},'%') or  " +
  " b.construction_unit like concat ('%',#{keyword},'%') or  " +
  " b.customer_name like concat ('%',#{keyword},'%') or  " +
  " bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  " ) and  " +
  " (b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')  " +
  "       " +
  " ORDER BY " +
  " p.create_time desc")
    List<ProgressListVo> searchAllProgressStaff(PageVo pageVo);

    @Select("SELECT " +
  " p.id id, " +
  " b.id baseId, " +
  " p.founder_id founderId," +
  " b.cea_num ceaNum, " +
  " b.project_num projectNum, " +
  " b.project_name projectName, " +
  " ( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  " ( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  " b.water_address waterAddress, " +
  " b.construction_unit constructionUnit, " +
  " b.project_category projectCategory, " +
  " ( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  " ( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
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
  " ) AS designCategory, " +
  " ( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  " b.customer_name customerName, " +
  " (select member_name username from member_manage where id = p.founder_id) username, " +
  " ( CASE p.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  " bt.name_of_cost_unit nameOfCostUnit, " +
  " bt.amount_cost amountCost, " +
  " p.contract_amount contractAmount, " +
  " p.current_payment_Information currentPaymentInformation, " +
  " p.current_payment_ratio currentPaymentRatio, " +
  " p.receiving_time receivingTime, " +
  " p.compile_time compileTime " +
  " from progress_payment_information p  " +
  " LEFT JOIN base_project b on p.base_project_id = b.id  " +
  " LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  " where " +
  " (p.del_flag = '0')  " +
  "      and  " +
  " (b.district = #{district} or #{district} = '') and  " +
  " (b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  " (p.project_type = #{projectType} or #{projectType} = '') and " +
  " (p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  " (p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  " (p.compile_time > #{startTime} or #{startTime} = '') and  " +
  " (p.compile_time < #{endTime} or #{endTime} = '') and  " +
  "      p.founder_id = #{uid} and  " +
  " ( " +
  " b.cea_num like concat('%',#{keyword},'%') or  " +
  " b.project_num like concat('%',#{keyword},'%') or  " +
  " b.project_name like concat ('%',#{keyword},'%') or  " +
  " b.construction_unit like concat ('%',#{keyword},'%') or  " +
  " b.customer_name like concat ('%',#{keyword},'%') or  " +
  " bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  " ) and  " +
  " (b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')  " +
  "       " +
  " ORDER BY " +
  " p.create_time desc")
    List<ProgressListVo> searchAllProgressProcessed(PageVo pageVo);

    @Select("SELECT " +
  " p.id id, " +
  " b.id baseId, " +
  " b.cea_num ceaNum, " +
  " p.founder_id founderId," +
  " b.project_num projectNum, " +
  " b.project_name projectName, " +
  " ( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus, " +
  " ( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
  " b.water_address waterAddress, " +
  " b.construction_unit constructionUnit, " +
  " b.project_category projectCategory, " +
  " ( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType, " +
  " ( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
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
  " ) AS designCategory, " +
  " ( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
  " b.customer_name customerName, " +
  " (select member_name username from member_manage where id = p.founder_id) username, " +
  " ( CASE p.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing, " +
  " bt.name_of_cost_unit nameOfCostUnit, " +
  " bt.amount_cost amountCost, " +
  " p.contract_amount contractAmount, " +
  " p.current_payment_Information currentPaymentInformation, " +
  " p.current_payment_ratio currentPaymentRatio, " +
  " p.receiving_time receivingTime, " +
  " p.compile_time compileTime " +
  " from progress_payment_information p  " +
  " LEFT JOIN base_project b on p.base_project_id = b.id  " +
  " LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id " +
  " where " +
  " (p.del_flag = '0')  " +
  "      and  " +
  " (b.district = #{district} or #{district} = '') and  " +
  " (b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
  " (p.project_type = #{projectType} or #{projectType} = '') and " +
  " (p.receiving_time > #{startTime} or #{startTime} = '') and  " +
  " (p.receiving_time < #{endTime} or #{endTime} = '') and  " +
  " (p.compile_time > #{startTime} or #{startTime} = '') and  " +
  " (p.compile_time < #{endTime} or #{endTime} = '') and  " +
  " ( " +
  " b.cea_num like concat('%',#{keyword},'%') or  " +
  " b.project_num like concat('%',#{keyword},'%') or  " +
  " b.project_name like concat ('%',#{keyword},'%') or  " +
  " b.construction_unit like concat ('%',#{keyword},'%') or  " +
  " b.customer_name like concat ('%',#{keyword},'%') or  " +
  " bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
  " ) and  " +
  " (b.progress_payment_status = #{progressStatus} or #{progressStatus} = '') " +
  " ORDER BY " +
  " p.create_time desc")
    List<ProgressListVo> searchAllProgressSueecss(PageVo pageVo);
}
