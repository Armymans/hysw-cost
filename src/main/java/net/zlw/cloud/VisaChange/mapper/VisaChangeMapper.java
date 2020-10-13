package net.zlw.cloud.VisaChange.mapper;


import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface VisaChangeMapper extends Mapper<VisaChange> {


    @Select("SELECT" +
            "    s1.id," +
            "    s3.amount_cost," +
            "    s1.contract_amount contractAmount," +
            "    s2.cea_num," +
            "    s2.project_num," +
            "    (" +
            "    CASE" +
            "    s2.district " +
            "    WHEN '1' THEN" +
            "    '芜湖' " +
            "    WHEN '2' THEN" +
            "    '马鞍山' " +
            "    WHEN '3' THEN" +
            "    '江北' " +
            "    WHEN '4' THEN" +
            "    '吴江' " +
            "    END " +
            "    ) as district," +
            "    s1.amount_visa_change amountVisaChange," +
            "    s1.proportion_contract proportionContract," +
            "    s5.construction_unit_name constructionUnit," +
            "    (" +
            "    CASE" +
            "    s2.project_nature " +
            "    WHEN '1' THEN" +
            "    '新建' " +
            "    WHEN '2' THEN" +
            "    '改造' " +
            "    END " +
            "    ) as projectNature," +
            "    (" +
            "    CASE" +
            "    s2.design_category " +
            "    WHEN '1' THEN" +
            "    '市政管道' " +
            "    WHEN '2' THEN" +
            "    '管网改造' " +
            "    WHEN '3' THEN" +
            "    '新建小区' " +
            "    WHEN '4' THEN" +
            "    '二次供水项目' " +
            "    WHEN '5' THEN" +
            "    '工商户' " +
            "    WHEN '6' THEN" +
            "    '居民装接水' " +
            "    WHEN '7' THEN" +
            "    '行政事业' " +
            "    END " +
            "    ) as designCategory," +
            "    (" +
            "    CASE" +
            "    s2.water_supply_type " +
            "    WHEN '1' THEN" +
            "    '直供水' " +
            "    WHEN '2' THEN" +
            "    '二次供水' " +
            "    END " +
            "    ) as waterSupplyType," +
            "    s2.customer_name," +
            "    s2.water_address," +
            "    s1.base_project_id baseProjectId," +
            "    s2.proposer," +
            "    (" +
            "    CASE" +
            "    s1.outsourcing " +
            "    WHEN '1' THEN" +
            "    '是' " +
            "    WHEN '2' THEN" +
            "    '否' " +
            "    END " +
            "    ) as outsourcing," +
            "    s4.cost_unit_name as nameOfCostUnit," +
            "    (" +
            "    CASE" +
            "    s1.status " +
            "    WHEN '1' THEN" +
            "    '待审核' " +
            "    WHEN '2' THEN" +
            "    '处理中' " +
            "    WHEN '3' THEN" +
            "    '未通过' " +
            "    WHEN '4' THEN" +
            "    '待确认' " +
            "    WHEN '5' THEN" +
            "    '进行中' " +
            "    WHEN '6' THEN" +
            "    '已完成' " +
            "    END " +
            "    ) as status," +
            "    s1.create_time," +
            "    s1.compile_time," +
            "    s2.project_name " +
            "    FROM" +
            "    visa_change_information s1" +
            "    LEFT JOIN base_project s2 ON s1.base_project_id = s2.id" +
            "    LEFT JOIN budgeting s3 ON s2.id = s3.base_project_id " +
            "    LEFT JOIN cost_unit_management s4 ON s4.id = s3.name_of_cost_unit " +
            "    LEFT JOIN construction_unit_management s5 ON s5.id = s2.construction_unit " +
            "    WHERE" +
            "    1 = 1 " +
            "    AND s1.state = 1 " +
            "    AND (" +
            "    s2.district = #{district} or #{district} = '' )" +
            "    AND (" +
            "    s2.project_nature = #{projectNature} or #{projectNature} = '')" +
            "    AND (" +
            "    s1.status = #{status} or #{status} = '')" +
            "    AND s1.create_time >= #{createStartTime}" +
            "    AND (" +
            "    s1.create_time <= #{createEndTime} or #{createEndTime}  ='')" +
            "    AND ("+
            "    s2.cea_num LIKE CONCAT(" +
            "    '%',#{keyword},'%') or" +
            "    s2.project_num LIKE CONCAT(" +
            "    '%',#{keyword},'%') or" +
            "    s2.project_name LIKE CONCAT( '%', #{keyword},'%')" +
            "    )")
    List<VisaChangeVo> findAll(VisaChangeVo visaChangeVO);

    /***
     * 删除
     * @param id
     */
    @Update("update  visa_change_information set state = '1' where id = #{id}")
    void deleteById(@Param("id") String id);

    @Select("select * from visa_change_information where id = #{id}")
    VisaChange selectById(@Param("id") String id);

    @Select("SELECT * FROM visa_change_information where base_project_id =#{id} ")
    List<VisaChange> findByBaseProjectId(@Param("id") String id);

    @Select("SELECT" +
            "                   s1.id," +
            "                    s3.amount_cost," +
            "                    s1.contract_amount contractAmount," +
            "                    s2.cea_num," +
            "    s2.project_num," +
            "                    s2.project_name," +
            "    (" +
            "    CASE" +
            "    s2.district " +
            "    WHEN '1' THEN" +
            "    '芜湖' " +
            "    WHEN '2' THEN" +
            "    '马鞍山' " +
            "    WHEN '3' THEN" +
            "    '江北' " +
            "    WHEN '4' THEN" +
            "    '吴江' " +
            "    END " +
            "    ) as district," +
            "                    s1.amount_visa_change amountVisaChange," +
            "                    s1.proportion_contract proportionContract," +
            "    s6.construction_unit_name constructionUnit," +
            "    (" +
            "    CASE" +
            "    s2.project_nature " +
            "    WHEN '1' THEN" +
            "    '新建' " +
            "    WHEN '2' THEN" +
            "    '改造' " +
            "    END " +
            "    ) as projectNature," +
            "    (" +
            "    CASE" +
            "    s2.design_category " +
            "    WHEN '1' THEN" +
            "    '市政管道' " +
            "    WHEN '2' THEN" +
            "    '管网改造' " +
            "    WHEN '3' THEN" +
            "    '新建小区' " +
            "    WHEN '4' THEN" +
            "    '二次供水项目' " +
            "    WHEN '5' THEN" +
            "    '工商户' " +
            "    WHEN '6' THEN" +
            "    '居民装接水' " +
            "    WHEN '7' THEN" +
            "    '行政事业' " +
            "    END " +
            "    ) as designCategory," +
            "    (" +
            "    CASE" +
            "    s2.water_supply_type " +
            "    WHEN '1' THEN" +
            "    '直供水' " +
            "    WHEN '2' THEN" +
            "    '二次供水' " +
            "    END " +
            "    ) as waterSupplyType," +
            "                    s2.customer_name," +
            "                    s2.water_address," +
            "                    s1.base_project_id baseProjectId," +
            "                    s2.proposer," +
            "    (" +
            "    CASE" +
            "    s1.outsourcing " +
            "    WHEN '1' THEN" +
            "    '是' " +
            "    WHEN '2' THEN" +
            "    '否' " +
            "    END " +
            "    ) as outsourcing," +
            "    s5.cost_unit_name as nameOfCostUnit," +
            "    (" +
            "    CASE" +
            "    s1.status " +
            "    WHEN '1' THEN" +
            "    '待审核' " +
            "    WHEN '2' THEN" +
            "    '处理中' " +
            "    WHEN '3' THEN" +
            "    '未通过' " +
            "    WHEN '4' THEN" +
            "    '待确认' " +
            "    WHEN '5' THEN" +
            "    '进行中' " +
            "    WHEN '6' THEN" +
            "    '已完成' " +
            "    END " +
            "    ) as status," +
            "                    s1.create_time," +
            "                    s1.completion_time," +
            "                    s2.project_name " +
            "                    FROM" +
            "                    visa_change_information s1 " +
            "                    LEFT JOIN base_project s2 on s1.base_project_id = s2.id" +
            "                    left join budgeting s3 on s2.id = s3.base_project_id" +
            "                    left join audit_info as s4 on s1.id = s4.base_project_id " +
            "    LEFT JOIN cost_unit_management s5 ON s5.id = s3.name_of_cost_unit " +
            "    LEFT JOIN construction_unit_management s6 ON s6.id = s2.construction_unit " +
            "                    where 1=1  and " +
            "                    s4.audit_result = 0 and" +
            "                    s4.auditor_id = #{loginUserId} and "+
            "                    s4.status = 0 and" +
            "                     (s2.district = #{district} or #{district} = '' )" +
            "                    and " +
            "                     (s2.project_nature = #{projectNature} or #{projectNature} = '')" +
            "                    and " +
            "                     (s1.`status` =#{status} or #{status} = '')" +
            "                   and" +
            "                    s1.create_time>= #{createStartTime}" +
            "                    and " +
            "                    (s1.create_time<=#{createEndTime} or #{createEndTime}  ='')" +
            "                    and (" +
            "                    s2.cea_num like CONCAT('%',#{keyword},'%') or" +
            "                    s2.project_num like CONCAT('%',#{keyword},'%') or" +
            "                    s2.project_name like  CONCAT('%',#{keyword},'%') " +
            "            ) ")
    List<VisaChangeVo> findByNoExamine(VisaChangeVo visaChangeVO);


    @Select("SELECT" +
            "                   s1.id," +
            "                    s3.amount_cost," +
            "                    s1.contract_amount contractAmount," +
            "                    s2.cea_num," +
            "    s2.project_num," +
            "                    s2.project_name," +
            "    (" +
            "    CASE" +
            "    s2.district " +
            "    WHEN '1' THEN" +
            "    '芜湖' " +
            "    WHEN '2' THEN" +
            "    '马鞍山' " +
            "    WHEN '3' THEN" +
            "    '江北' " +
            "    WHEN '4' THEN" +
            "    '吴江' " +
            "    END " +
            "    ) as district," +
            "                    s1.amount_visa_change amountVisaChange," +
            "                    s1.proportion_contract proportionContract," +
            "    s6.construction_unit_name constructionUnit," +
            "    (" +
            "    CASE" +
            "    s2.project_nature " +
            "    WHEN '1' THEN" +
            "    '新建' " +
            "    WHEN '2' THEN" +
            "    '改造' " +
            "    END " +
            "    ) as projectNature," +
            "    (" +
            "    CASE" +
            "    s2.design_category " +
            "    WHEN '1' THEN" +
            "    '市政管道' " +
            "    WHEN '2' THEN" +
            "    '管网改造' " +
            "    WHEN '3' THEN" +
            "    '新建小区' " +
            "    WHEN '4' THEN" +
            "    '二次供水项目' " +
            "    WHEN '5' THEN" +
            "    '工商户' " +
            "    WHEN '6' THEN" +
            "    '居民装接水' " +
            "    WHEN '7' THEN" +
            "    '行政事业' " +
            "    END " +
            "    ) as designCategory," +
            "    (" +
            "    CASE" +
            "    s2.water_supply_type " +
            "    WHEN '1' THEN" +
            "    '直供水' " +
            "    WHEN '2' THEN" +
            "    '二次供水' " +
            "    END " +
            "    ) as waterSupplyType," +
            "                    s2.customer_name," +
            "                    s2.water_address," +
            "                    s1.base_project_id baseProjectId," +
            "                    s2.proposer," +
            "    (" +
            "    CASE" +
            "    s1.outsourcing " +
            "    WHEN '1' THEN" +
            "    '是' " +
            "    WHEN '2' THEN" +
            "    '否' " +
            "    END " +
            "    ) as outsourcing," +
            "    s5.cost_unit_name as nameOfCostUnit," +
            "    (" +
            "    CASE" +
            "    s1.status " +
            "    WHEN '1' THEN" +
            "    '待审核' " +
            "    WHEN '2' THEN" +
            "    '处理中' " +
            "    WHEN '3' THEN" +
            "    '未通过' " +
            "    WHEN '4' THEN" +
            "    '待确认' " +
            "    WHEN '5' THEN" +
            "    '进行中' " +
            "    WHEN '6' THEN" +
            "    '已完成' " +
            "    END " +
            "    ) as status," +
            "                    s1.create_time," +
            "                    s1.completion_time," +
            "                    s2.project_name " +
            "                    FROM" +
            "                    visa_change_information s1 " +
            "                    LEFT JOIN base_project s2 on s1.base_project_id = s2.id" +
            "                    left join budgeting s3 on s2.id = s3.base_project_id" +
            "                    left join audit_info as s4 on s1.id = s4.base_project_id " +
            "    LEFT JOIN cost_unit_management s5 ON s5.id = s3.name_of_cost_unit " +
            "    LEFT JOIN construction_unit_management s6 ON s6.id = s2.construction_unit " +
            "                    where 1=1  and " +
            "                    s4.audit_result = 2 and" +
            "                    s4.status = 0 and" +
            "                    s4.founder_id = #{loginUserId} and" +
            "                     (s2.district = #{district} or #{district} = '' )" +
            "                    and " +
            "                     (s2.project_nature = #{projectNature} or #{projectNature} = '')" +
            "                    and " +
            "                     (s1.`status` =#{status} or #{status} = '')" +
            "                   and" +
            "                    s1.create_time>= #{createStartTime}" +
            "                    and " +
            "                    (s1.create_time<=#{createEndTime} or #{createEndTime}  ='')" +
            "                    and (" +
            "                    s2.cea_num like CONCAT('%',#{keyword},'%') or" +
            "                    s2.project_num like CONCAT('%',#{keyword},'%') or" +
            "                    s2.project_name like  CONCAT('%',#{keyword},'%') " +
            "            ) ")
    List<VisaChangeVo> findByNotPass(VisaChangeVo visaChangeVO);


    @Select(" select * from visa_change_information s1" +
            " left join base_project s2 on s1.base_project_id = s2.id" +
            " left join visa_apply_change_information s3 on s1.apply_change_info_id = s3.id")
    List<VisaChange> findByInfoOne();


    @Select(" select * from visa_change_information s1" +
            " left join base_project s2 on s1.base_project_id = s2.id" +
            " left join visa_apply_change_information s3 on s1.apply_change_info_id = s3.id" +
            " where" +
            " s1.base_project_id = #{baseProjectId}" +
            " and change_num =#{changNum}")
    List<VisaChange> findByInfoTwo();


    @Select(
            "SELECT " +
                    " outsourcing_amount" +
                    "  FROM " +
                    " visa_change_information s1," +
                    " base_project s2" +
                    " where" +
                    " s1.base_project_id = s2.id" +
                    " and" +
                    " (s2.district=#{district} or  #{district}  = '')" +
                    " and" +
                    " s1.create_time>=#{startTime}" +
                    " and" +
                    " (s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<VisaChange> totalexpenditure(CostVo2 costVo2);


    @Select("SELECT s1.change_num " +
            "FROM visa_change_information s1" +
            "         LEFT JOIN" +
            "     base_project s2" +
            "     ON s1.base_project_id = s2.id " +
            "order by s1.create_time desc" +
            " limit 1")
    VisaChange selectByChangNum();

    @Select(" SELECT s2.auditor_id from visa_change_information s1" +
            " LEFT JOIN audit_info s2 ON s1.base_project_id = s2.id" +
            " where" +
            " s1.id = #{id}")
    VisaChange selectBuAuditId(@Param("id") String id);
}
