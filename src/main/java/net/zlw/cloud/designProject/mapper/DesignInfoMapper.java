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
                "SELECT\n" +
                        "s2.id,\n" +
                        "s1.merge_flag,\n" +
                        "s2.base_project_id,\n" +
                        "s1.should_be should_be,\n" +
                        "s1.cea_num,\n" +
                        "s1.project_num,\n" +
                        "s1.project_name,\n" +
                        "s1.desgin_status,\n" +
                        "s1.district,\n" +
                        "s1.water_address,\n" +
                        "s1.construction_unit,\n" +
                        "s2.contacts,\n" +
                        "s2.phone,\n" +
                        "s1.project_nature,\n" +
                        "s1.design_category,\n" +
                        "s1.project_category,\n" +
                        "s1.a_b,\n" +
                        "s5.design_unit_name designUnitName,\n" +
                        "s2.isaccount,\n" +
                        "s2.isdeschange,\n" +
                        "s2.outsource,\n" +
                        "s2.outsource_money,\n" +
                        "s2.designer,\n" +
                        "s2.take_time,\n" +
                        "s2.blueprint_start_time,\n" +
                        "s3.design_change_time ,\n" +
                        "s2.isfinalaccount ,\n" +
                        "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange\n" +
                        "FROM\n" +
                        "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id \n" +
                        "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id\n" +
                        "LEFT JOIN audit_info s4 ON s4.base_project_id = s2.id\n" +
                        "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit\n" +
                        "where\n" +
                        "(s1.desgin_status =#{desginStatus} or #{desginStatus} = '')\n" +
                        "and\n" +
                        "(s1.district = #{district} or #{district} ='')\n" +
                        "and\n" +
                        "(s1.design_category =#{designCategory} or #{designCategory} ='')\n" +
                        "and\n" +
                        "(s1.project_nature = #{projectNature} or #{projectNature}='')\n" +
                        "and\n" +
                        "(s1.should_be = #{shouldBe} or #{shouldBe} ='')\n" +
                        "and\n" +
                        "blueprint_start_time>= #{desginStartTime}\n" +
                        "and \n" +
                        "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='') \n" +
                        "and\n" +
                        "(s2.isaccount = #{isaccount} or #{isaccount} ='')\n" +
                        "and\n" +
                        "s2.status= '0'\n" +
                        "and \n" +
                        "s1.del_flag = '0'\n" +
                        "and\n" +
                        "s4.auditor_id = #{userId}\n" +
                        "and\n" +
                        "s4.audit_result = '0'\n" +
                        "and\n" +
                        "(\n" +
                        "s1.cea_num like CONCAT('%',#{keyword},'%') or \n" +
                        "s1.project_num like CONCAT('%',#{keyword},'%') or\n" +
                        "s1.project_name like  CONCAT('%',#{keyword},'%')  or\n" +
                        "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or\n" +
                        "s1.project_category like  CONCAT('%',#{keyword},'%')  or\n" +
                        "s2.design_unit like  CONCAT('%',#{keyword},'%') \n" +
                        ")" +
                        " ORDER BY " +
                        " s1.should_be "
    )
    List<DesignInfo> designProjectSelect(DesignPageVo pageVo);



    @Select(
            "SELECT\n" +
                    "s2.id,\n" +
                    "s2.base_project_id,\n" +
                    "s1.should_be should_be,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.merge_flag,\n" +
                    "s1.desgin_status,\n" +
                    "s1.district,\n" +
                    "s1.water_address,\n" +
                    "s1.construction_unit,\n" +
                    "s2.contacts,\n" +
                    "s2.phone,\n" +
                    "s2.outsource,\n" +
                    "s2.designer,\n" +
                    "s1.project_nature,\n" +
                    "s1.design_category,\n" +
                    "s1.project_category,\n" +
                    "s1.a_b,\n" +
                    "s5.design_unit_name designUnitName,\n" +
                    "s2.isaccount,\n" +
                    "s2.isdeschange,\n" +
                    "s2.outsource_money,\n" +
                    "s2.take_time,\n" +
                    "s2.blueprint_start_time,\n" +
                    "s3.design_change_time ,\n" +
                    "s2.isfinalaccount ,\n" +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange\n" +
                    "FROM\n" +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id \n" +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id\n" +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit\n" +
                    "where\n" +
                    "s1.desgin_status = #{desginStatus}\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} ='')\n" +
                    "and\n" +
                    "(s1.design_category =#{designCategory} or #{designCategory} ='')\n" +
                    "and\n" +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='')\n" +
                    "and\n" +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='')\n" +
                    "and\n" +
                    "blueprint_start_time>= #{desginStartTime}\n" +
                    "and \n" +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='') \n" +
                    "and\n" +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='')\n" +
                    "and\n" +
                    "s2.status= '0'\n" +
                    "and \n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "s2.founder_id = #{userId}\n" +
                    "and\n" +
                    "(\n" +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or \n" +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    " ORDER BY " +
                    " s1.should_be "
    )
    List<DesignInfo> designProjectSelect2(DesignPageVo pageVo);


    @Select(
            "SELECT\n" +
                    "s2.id,\n" +
                    "s2.base_project_id,\n" +
                    "s1.should_be should_be,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_num,\n" +
                    "s1.merge_flag,\n" +
                    "s1.project_name,\n" +
                    "s1.desgin_status,\n" +
                    "s1.district,\n" +
                    "s1.water_address,\n" +
                    "s1.construction_unit,\n" +
                    "s2.contacts,\n" +
                    "s2.phone,\n" +
                    "s2.outsource,\n" +
                    "s2.designer,\n" +
                    "s1.project_nature,\n" +
                    "s1.design_category,\n" +
                    "s1.project_category,\n" +
                    "s1.a_b,\n" +
                    "s5.design_unit_name designUnitName,\n" +
                    "s2.isaccount,\n" +
                    "s2.isdeschange,\n" +
                    "s2.outsource_money,\n" +
                    "s2.take_time,\n" +
                    "s2.blueprint_start_time,\n" +
                    "s3.design_change_time,\n" +
                    "s2.isfinalaccount,\n" +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange\n" +
                    "FROM\n" +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id \n" +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id\n" +
                    "LEFT JOIN  design_unit_management s5 ON s5.id = s2.design_unit\n" +
                    "where\n" +
                    "(s1.desgin_status = #{desginStatus} or #{desginStatus} ='')\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} ='')\n" +
                    "and\n" +
                    "(s1.design_category =#{designCategory} or #{designCategory} ='')\n" +
                    "and\n" +
                    "(s1.project_nature = #{projectNature} or #{projectNature}='')\n" +
                    "and\n" +
                    "(s1.should_be = #{shouldBe} or #{shouldBe} ='')\n" +
                    "and\n" +
                    "blueprint_start_time>= #{desginStartTime}\n" +
                    "and \n" +
                    "(blueprint_start_time<=#{desginEndTime} or  #{desginEndTime} ='') \n" +
                    "and\n" +
                    "(s2.isaccount = #{isaccount} or #{isaccount} ='')\n" +
                    "and\n" +
                    "s2.status= '0'\n" +
                    "and \n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s2.founder_id = #{userId} or #{userId} = #{adminId} ) \n" +
                    "and\n" +
                    "(\n" +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or \n" +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_name like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s1.construction_unit like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s1.project_category like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s2.design_unit like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    " ORDER BY " +
                    " s1.should_be "
    )
    List<DesignInfo> designProjectSelect3(DesignPageVo pageVo);


    @Select(
            "SELECT\n" +
                    "s2.id,\n" +
                    "s2.base_project_id,\n" +
                    "s1.should_be should_be,\n" +
                    "s1.cea_num,\n" +
                    "s1.merge_flag,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.desgin_status,\n" +
                    "s1.district,\n" +
                    "s1.water_address,\n" +
                    "s1.construction_unit,\n" +
                    "s2.contacts,\n" +
                    "s2.phone,\n" +
                    "s2.outsource,\n" +
                    "s2.designer,\n" +
                    "s1.project_nature,\n" +
                    "s1.design_category,\n" +
                    "s1.project_category,\n" +
                    "s1.a_b,\n" +
                    "s2.design_unit,\n" +
                    "s2.isaccount,\n" +
                    "s2.isdeschange,\n" +
                    "s2.outsource_money,\n" +
                    "s2.take_time,\n" +
                    "s2.blueprint_start_time,\n" +
                    "s3.design_change_time ,\n" +
                    "s2.isfinalaccount ,\n" +
                    "( CASE WHEN s3.design_change_time IS NULL THEN '否' ELSE '是' END ) AS ischange\n" +
                    "FROM\n" +
                    "design_info s2 LEFT JOIN design_change_info s3 ON s2.id = s3.design_info_id \n" +
                    "LEFT JOIN base_project s1 ON s2.base_project_id = s1.id\n" +
                    "where\n" +
                    "s1.desgin_status = '2'\n" +
                    "and\n" +
                    "s2.status= '0'\n" +
                    "and \n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "s2.id = #{id}"
    )
    DesignInfo designProjectSelectOne(@Param("id") String id);

    @Update(
            "update  design_info \n" +
                    "set \n" +
                    "status = \"1\"\n" +
                    "where \n" +
                    "id=#{id}"
    )
    void deleteProject(@Param("id") String id);

    @Update(
            "update  \n" +
                    "design_info \n" +
                    "set \n" +
                    "isfinalaccount = \"0\"\n" +
                    "where\n" +
                    "id = #{id};"
    )
    void updateFinalAccount(@Param("id") String id);


    @Select(
            "SELECT \n" +
                    "outsource_money\n" +
                    "FROM \n" +
                    "design_info s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(s2.district=#{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<DesignInfo> totalexpenditure(CostVo2 costVo2);

    @Select(
            "select \n" +
                    "count(*)\n" +
                    "from\n" +
                    "design_info s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(district = #{district} or #{district} =  '')"
    )
    Integer designInfoCount(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.merge_flag,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.construction_unit,\n" +
                    "s1.district,\n" +
                    "s2.blueprint_start_time,\n" +
                    "s2.designer,\n" +
                    "s2.outsource,\n" +
                    "s2.design_unit\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "design_info s2 \n" +
                    "where\n" +
                    " s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s2.blueprint_start_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.blueprint_start_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(\n" +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_name like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.district like CONCAT('%',#{keyword},'%') or\n" +
                    "s2.designer like CONCAT('%',#{keyword},'%') \n" +
                    ")"
    )
    List<DesignInfo> desginCensusList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.merge_flag,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.design_category,\n" +
                    "s1.construction_unit,\n" +
                    "s1.district,\n" +
                    "s2.blueprint_start_time,\n" +
                    "s2.designer,\n" +
                    "s2.outsource,\n" +
                    "s2.design_unit\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "design_info s2 \n" +
                    "where\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "s2.designer = #{designer}\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "(s1.design_category = #{designCategory} or #{designCategory} = '')\n" +
                    "and\n" +
                    "s2.blueprint_start_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.blueprint_start_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(\n" +
                    "s1.cea_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_num like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.project_name like CONCAT('%',#{keyword},'%') or\n" +
                    "s1.district like CONCAT('%',#{keyword},'%') or\n" +
                    "s2.designer like CONCAT('%',#{keyword},'%') \n" +
                    ")"
    )
    List<DesignInfo> desginCensusListByDesigner(CostVo2 costVo2);
}
