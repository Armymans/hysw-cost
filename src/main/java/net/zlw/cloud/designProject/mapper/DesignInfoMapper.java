package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.DesignPageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface DesignInfoMapper extends Mapper<DesignInfo> {
    @Select(
                "SELECT " +
                        "s2.id, " +
                        "s1.merge_flag, " +
                        "s2.base_project_id, " +
                        "s1.should_be should_be, " +
                        "s1.customer_phone customerPhone, " +
                        "s1.cea_num, " +
                        "s1.project_num, " +
                        "s1.project_name, " +
                        "s1.desgin_status, " +
                        "s1.district, " +
                        "s1.water_address, " +
                        "s1.construction_unit, " +
                        "s1.contacts, " +
                        "s2.phone, " +
                        "s1.project_nature, " +
                        "s1.design_category, " +
                        "s1.project_category, " +
                        "s1.a_b, " +
                        "s5.design_unit_name designUnitName, " +
                        "s2.isaccount, " +
                        "s2.isdeschange, " +
                        "s2.outsource, " +
                        "s2.outsource_money, " +
                        "s2.designer, " +
                        "s2.take_time, " +
                        "s2.blueprint_start_time, " +
                        "s3.design_change_time , " +
                        "s2.isfinalaccount , " +
                        "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange " +
                        "FROM " +
                        "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id  " +
                        "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id " +
                        "LEFT JOIN audit_info s4 ON s4.base_project_id = s2.id " +
                        "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit " +
                        "where " +
                        "s1.desgin_status ='1' " +
                        "and " +
                        "s2.status= '0' " +
                        "and  " +
                        "s1.del_flag = '0' " +
                        "and " +
                        "s4.audit_result = '0' " +
                        "and " +
                        "(s4.auditor_id = #{userId} or s1.founder_id= #{userId}) " +
                        "and " +
                        "(s1.district = #{district} or #{district} ='') " +
                        "and " +
                        "(s1.design_category =#{designCategory} or #{designCategory} ='') " +
                        "and " +
                        "(s1.project_nature = #{projectNature} or #{projectNature}='') " +
                        "and " +
                        "(s1.should_be = #{shouldBe} or #{shouldBe} ='') " +
                        "and " +
                        "blueprint_start_time>= #{desginStartTime} " +
                        "and  " +
                        "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='')  " +
                        " and " +
                        "(s4.auditor_id = #{currentPeople} or #{currentPeople} = '' ) " +
                        "and " +
                        "(s2.isaccount = #{isaccount} or #{isaccount} ='') " +
                        "and " +
                        "( " +
                        "s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
                        "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                        "s1.project_name like  CONCAT('%',#{keyword},'%')  or " +
                        "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or " +
                        "s1.project_category like  CONCAT('%',#{keyword},'%')  or " +
                        "s2.design_unit like  CONCAT('%',#{keyword},'%')  " +
                        ") " +
//                        "and " +
//                        "(s3.`status`  is null or s3.`status`  = '0') " +
                        "ORDER BY  " +
                        "s1.should_be, " +
                        "s1.create_time DESC"
    )
    List<DesignInfo> designProjectSelect(DesignPageVo pageVo);

    @Select(
            "SELECT " +
                    "s2.id, " +
                    "s1.merge_flag, " +
                    "s2.base_project_id, " +
                    "s1.should_be should_be, " +
                    "s1.customer_phone customerPhone, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.desgin_status, " +
                    "s1.district, " +
                    "s1.water_address, " +
                    "s1.construction_unit, " +
                    "s1.contacts contacts1, " +
                    "s2.phone, " +
                    "s1.project_nature, " +
                    "s1.design_category, " +
                    "s1.project_category, " +
                    "s1.a_b, " +
                    "s5.design_unit_name designUnitName, " +
                    "s2.isaccount, " +
                    "s2.isdeschange, " +
                    "s2.outsource, " +
                    "s2.outsource_money, " +
                    "s2.designer, " +
                    "s2.take_time, " +
                    "s2.blueprint_start_time, " +
                    "s3.design_change_time , " +
                    "s2.isfinalaccount , " +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange " +
                    "FROM " +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id  " +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id " +
                    "LEFT JOIN audit_info s4 ON s4.base_project_id = s2.id " +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit " +
                    "where " +
                    "s1.desgin_status ='1' " +
                    "and " +
                    "s2.status= '0' " +
                    "and  " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "s4.audit_result = '0' " +
                    "and " +
                    "(s1.design_category = #{designCategory} or #{designCategory} ='') " +
                    "and " +
                    "(s1.district = #{district} or #{district} ='') " +
                    "and " +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='') " +
                    "and " +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='') " +
                    "and " +
                    "blueprint_start_time>= #{desginStartTime} " +
                    "and  " +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='')  " +
                    "and " +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='') " +
                    " and " +
                    "(s4.auditor_id = #{currentPeople} or #{currentPeople} = '' ) " +
                    "and " +
                    "( " +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%')  " +
                    ") " +
//                    "and " +
//                    "(s3.`status`  is null or s3.`status`  = '0') " +
                    "ORDER BY  " +
                    "s1.should_be, " +
                    "s1.create_time DESC"
    )
    List<DesignInfo> designProjectSelect1(DesignPageVo pageVo);


    @Select(
            "SELECT " +
                    "s2.id, " +
                    "s2.base_project_id, " +
                    "s1.should_be should_be, " +
                    "s1.customer_phone customerPhone, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.merge_flag, " +
                    "s1.project_name, " +
                    "s1.desgin_status, " +
                    "s1.district, " +
                    "s1.water_address, " +
                    "s1.construction_unit, " +
                    "s1.contacts contacts1, " +
                    "s2.phone, " +
                    "s2.outsource, " +
                    "s2.designer, " +
                    "s1.project_nature, " +
                    "s1.design_category, " +
                    "s1.project_category, " +
                    "s1.a_b, " +
                    "s5.design_unit_name designUnitName, " +
                    "s2.isaccount, " +
                    "s2.isdeschange, " +
                    "s2.outsource_money, " +
                    "s2.take_time, " +
                    "s2.blueprint_start_time, " +
                    "s3.design_change_time, " +
                    "s2.isfinalaccount, " +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange " +
                    "FROM " +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id  " +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id " +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit " +
                    "where " +
                    "(s1.desgin_status = #{desginStatus} or #{desginStatus} ='') " +
                    "and " +
                    "(s1.district = #{district} or #{district} ='') " +
                    "and " +
                    "(s1.design_category =#{designCategory} or #{designCategory} ='') " +
                    "and " +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='') " +
                    "and " +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='') " +
                    "and " +
                    "blueprint_start_time>= #{desginStartTime} " +
                    "and  " +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='')  " +
                    "and " +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='') " +
                    "and " +
                    "s2.status= '0' " +
                    "and  " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s2.founder_id = #{userId} or #{userId} = '' )  " +
                    "and " +
                    "( " +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%')  " +
                    ") " +
//                    "and " +
//                    "(s3.`status`  is null or s3.`status`  = '0') " +
                    "ORDER BY  " +
                    "s1.should_be, " +
                    "s1.create_time DESC"
    )
    List<DesignInfo> designProjectSelect3(DesignPageVo pageVo);

    @Select(
            "SELECT " +
                    "s2.id, " +
                    "s2.base_project_id, " +
                    "s1.should_be should_be," +
                    "s1.customer_phone customerPhone, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.merge_flag, " +
                    "s1.desgin_status, " +
                    "s1.district, " +
                    "s1.water_address, " +
                    "s1.construction_unit, " +
                    "s1.contacts contacts1, " +
                    "s2.phone, " +
                    "s2.outsource, " +
                    "s2.designer, " +
                    "s1.project_nature, " +
                    "s1.design_category, " +
                    "s1.project_category, " +
                    "s1.a_b, " +
                    "s5.design_unit_name designUnitName, " +
                    "s2.isaccount, " +
                    "s2.isdeschange, " +
                    "s2.outsource_money, " +
                    "s2.take_time, " +
                    "s2.blueprint_start_time, " +
                    "s3.design_change_time, " +
                    "s2.isfinalaccount, " +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange " +
                    "FROM " +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id  " +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id " +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit " +
                    "where " +
                    "(s1.desgin_status = #{desginStatus} or #{desginStatus} ='') " +
                    "and " +
                    "(s1.district = #{district} or #{district} ='') " +
                    "and " +
                    "(s1.design_category =#{designCategory} or #{designCategory} ='') " +
                    "and " +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='') " +
                    "and " +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='') " +
                    "and " +
                    "blueprint_start_time>= #{desginStartTime} " +
                    "and  " +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='')  " +
                    "and " +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='') " +
                    "and " +
                    "s2.status= '0' " +
                    "and  " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s2.founder_id = #{userId} or #{userId} = '' )  " +
                    "and " +
                    "( " +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%')  " +
                    ")" +
//                    "and " +
//                    "(s3.`status`  is null or s3.`status`  = '0') " +
                    "ORDER BY " +
                    "s1.should_be, " +
                    "s1.create_time DESC"
    )
    List<DesignInfo> designProjectSelect2(DesignPageVo pageVo);

    @Select(
            "SELECT " +
                    "s2.id, " +
                    "s2.base_project_id, " +
                    "s1.should_be should_be, " +
                    "s1.customer_phone customerPhone, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.merge_flag, " +
                    "s1.project_name, " +
                    "s1.desgin_status, " +
                    "s1.district, " +
                    "s1.water_address, " +
                    "s1.construction_unit, " +
                    "s1.contacts contacts1, " +
                    "s2.phone, " +
                    "s2.outsource, " +
                    "s2.designer, " +
                    "s1.project_nature, " +
                    "s1.design_category, " +
                    "s1.project_category, " +
                    "s1.a_b, " +
                    "s5.design_unit_name designUnitName, " +
                    "s2.isaccount, " +
                    "s2.isdeschange, " +
                    "s2.outsource_money, " +
                    "s2.take_time, " +
                    "s2.blueprint_start_time, " +
                    "s3.design_change_time, " +
                    "s2.isfinalaccount, " +
                    "s2.founder_id, " +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange " +
                    "FROM " +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id  " +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id " +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit " +
                    "where " +
                    "s1.desgin_status = '4' " +
                    "and " +
                    "s2.status= '0' " +
                    "and  " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} ='') " +
                    "and " +
                    "(s1.design_category =#{designCategory} or #{designCategory} ='') " +
                    "and " +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='') " +
                    "and " +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='') " +
                    "and " +
                    "blueprint_start_time>= #{desginStartTime} " +
                    "and  " +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='')  " +
                    "and " +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='') " +
                    "and " +
                    "( " +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or " +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%')  " +
                    ") " +
//                    "and " +
//                    "(s3.`status`  is null or s3.`status`  = '0') " +
                    "ORDER BY  " +
                    "s1.should_be, " +
                    "s1.create_time DESC"
    )
    List<DesignInfo> designProjectSelect4(DesignPageVo pageVo);
    @Select(
            "SELECT   " +
                    "  s2.id,   " +
                    "  s2.base_project_id,   " +
                    "  s2.founder_id,   " +
                    "  s1.cea_num,   " +
                    "  s1.merge_flag,   " +
                    "  s1.project_num,   " +
                    "  s1.project_name,   " +
                    "  s1.desgin_status,   " +
                    "  s1.water_address,   " +
                    "  s1.construction_unit,   " +
                    "  s2.contacts,   " +
                    "  s2.phone,   " +
                    "  s2.designer,   " +
                    "  s1.a_b,   " +
                    "  s2.design_unit,   " +
                    "  s2.isaccount,   " +
                    "  s2.isdeschange,   " +
                    "  s2.outsource_money,   " +
                    "  s2.take_time,   " +
                    "  s2.blueprint_start_time,   " +
                    "  s2.isfinalaccount,   " +
                    " ( CASE s1.should_be WHEN '0' THEN '是' WHEN '1' THEN '否' END ) AS shouldBe,   " +
                    " ( CASE s1.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,   " +
                    " ( CASE s1.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,   " +
                    " ( CASE s2.outsource WHEN '0' THEN '是' WHEN '1' THEN '否' END ) AS outsource,   " +
                    " (   " +
                    "CASE   " +
                    "  s1.design_category    " +
                    "  WHEN '1' THEN   " +
                    " '市政管道'    " +
                    "  WHEN '2' THEN   " +
                    " '管网改造'    " +
                    "  WHEN '3' THEN   " +
                    " '新建小区'    " +
                    "  WHEN '4' THEN   " +
                    " '二次供水项目'    " +
                    "  WHEN '5' THEN   " +
                    " '工商户'    " +
                    "  WHEN '6' THEN   " +
                    " '居民装接水'    " +
                    "  WHEN '7' THEN   " +
                    " '行政事业'    " +
                    "END    " +
                    " ) AS designCategory,   " +
                    " ( CASE s1.project_category WHEN '1' THEN '住宅区配套' WHEN '2' THEN '商业区配套' WHEN '3' THEN '3工商区配套' END ) AS projectCategory,   " +
                    " ( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange    " +
                    "FROM   " +
                    "  design_info s2   " +
                    "  LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id   " +
                    "  LEFT JOIN base_project s1 ON s2.base_project_id = s1.id    " +
                    "WHERE   " +
                    "  s1.desgin_status = '2'    " +
                    "  AND s2.STATUS = '0'    " +
                    "  AND s1.del_flag = '0'    " +
                    "  AND s2.id = #{id}"
    )
    DesignInfo designProjectSelectOne(@Param("id") String id);

    @Update(
            "update  design_info  " +
                    "set  " +
                    "status = \"1\" " +
                    "where  " +
                    "id=#{id}"
    )
    void deleteProject(@Param("id") String id);

    @Update(
            "update   " +
                    "design_info  " +
                    "set  " +
                    "isfinalaccount = \"0\" " +
                    "where " +
                    "id = #{id};"
    )
    void updateFinalAccount(@Param("id") String id);


    @Select(
            "SELECT  " +
                    "outsource_money " +
                    "FROM  " +
                    "design_info s1, " +
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
    List<DesignInfo> totalexpenditure(CostVo2 costVo2);

    @Select(
            "select  " +
                    "count(*) " +
                    "from " +
                    "design_info s1, " +
                    "base_project s2 " +
                    "where " +
                    "s1.base_project_id = s2.id " +
                    "and " +
                    "(district = #{district} or #{district} =  '')"
    )
    Integer designInfoCount(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.merge_flag, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.construction_unit, " +
                    "s1.district, " +
                    "s2.blueprint_start_time, " +
                    "s2.designer, " +
                    "s2.outsource, " +
                    "s2.design_unit " +
                    "FROM " +
                    "base_project s1, " +
                    "design_info s2  " +
                    "where " +
                    " s1.id = s2.base_project_id " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s2.blueprint_start_time >= #{startTime} " +
                    "and " +
                    "(s2.blueprint_start_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "( " +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or " +
                    "s1.project_name like CONCAT('%',#{keyword},'%') or " +
                    "s1.district like CONCAT('%',#{keyword},'%') or " +
                    "s2.designer like CONCAT('%',#{keyword},'%')  " +
                    ")"
    )
    List<DesignInfo> desginCensusList(CostVo2 costVo2);

    @Select("SELECT  " +
            "    s1.id,  " +
            "    s1.cea_num,  " +
            "    s1.merge_flag,  " +
            "    s1.project_num,  " +
            "    s1.project_name,   " +
            "    s1.construction_unit,  " +
            "    s2.blueprint_start_time,  " +
            "    s2.designer,   " +
            "    s2.design_unit,  " +
            "    (CASE s1.design_category  " +
            "     WHEN '1' THEN '市政管道'  " +
            "     WHEN '2' THEN '管网改造'  " +
            "     WHEN '3' THEN '新建小区'  " +
            "     WHEN '4' THEN '二次供水项目'  " +
            "     WHEN '5' THEN '工商户'  " +
            "     WHEN '6' THEN '居民装接水'  " +
            "     WHEN '7' THEN '行政事业'  " +
            "     END) designCategory,  " +
            "    (CASE s1.district  " +
            "     WHEN '1' THEN '芜湖'  " +
            "     WHEN '2' THEN '马鞍山'  " +
            "     WHEN '3' THEN '江北'  " +
            "     WHEN '4' THEN '吴江'  " +
            "   END) district,  " +
            "    (CASE s2.outsource  " +
            "     WHEN '0' THEN '是'  " +
            "     WHEN '1' THEN '否'  " +
            "   END) outsource  " +
            "    FROM  " +
            "    base_project s1,  " +
            "    design_info s2   " +
            "    where              " +
            "    s1.id = s2.base_project_id  " +
            "    and  " +
            "    s2.designer = #{designer}  " +
            "    and  " +
            "    (s1.district = #{district} or #{district} = '')  " +
            "    and  " +
            "    (s1.design_category = #{designCategory} or #{designCategory} = '')  " +
            "    and  " +
            "    s2.blueprint_start_time >= #{startTime}  " +
            "    and  " +
            "    (s2.blueprint_start_time <= #{endTime} or #{endTime} = '')  " +
            "    and  " +
            "    s1.del_flag = '0'  " +
            "    and  " +
            "    (  " +
            "    s1.cea_num like CONCAT('%',#{keyword},'%') or  " +
            "    s1.project_num like CONCAT('%',#{keyword},'%') or  " +
            "    s1.project_name like CONCAT('%',#{keyword},'%') or  " +
            "    s1.district like CONCAT('%',#{keyword},'%') or  " +
            "    s2.designer like CONCAT('%',#{keyword},'%')   " +
            "    )")
    List<DesignInfo> desginCensusListByDesigner(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "count(*) " +
                    "FROM " +
                    "design_info s1  " +
                    "LEFT JOIN audit_info s2 ON s1.id = s2.base_project_id " +
                    "WHERE " +
                    "s1.`status` = '0' " +
                    "and " +
                    "s2.`status` = '0' " +
                    "and " +
                    "s2.audit_result = '0' " +
                    "and " +
                    "s2.change_flag = '1' " +
                    "and " +
                    "(s2.auditor_id = #{id} or #{id} = '')"
    )
    Integer designReviewedCount(String id);
    @Select(
            "SELECT " +
                    "count(*) " +
                    "FROM " +
                    "design_info s1  " +
                    "LEFT JOIN audit_info s2 ON s1.id = s2.base_project_id " +
                    "WHERE " +
                    "s1.`status` = '0' " +
                    "and " +
                    "s2.`status` = '0' " +
                    "and " +
                    "s2.audit_result = '0' " +
                    "and " +
                    "s2.change_flag = '0' " +
                    "and " +
                    "(s2.auditor_id = #{id} or #{id} = '')"
    )
    Integer designChangeReviewedCount(String id);

    @Select("SELECT " +
            "design_unit_name  designUnitName " +
            "FROM " +
            " `design_unit_management`  " +
            "WHERE " +
            " del_flag = '0' and  " +
            " id = #{id}")
    String findDesignUnit( @Param("id") String designUnit);
}
