package net.zlw.cloud.followAuditing.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TrackAuditInfoDao extends Mapper<TrackAuditInfo> {
    @Select(
            "select\n" +
                    "tai.id id,\n" +
                    "b.cea_num ceaNum,\n" +
                    "b.project_num projectNum,\n" +
                    "b.project_name projectName,\n" +
                    "( CASE b.track_status WHEN '1' THEN '待审核' WHEN '2' THEN '未提交' WHEN '3' THEN '进行中' WHEN '4' THEN '未通过' WHEN '5' THEN '已完成' END ) AS trackStatus,\n" +
                    "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
                    "b.construction_unit constructionUnit,\n" +
                    "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,\n" +
                    "(\n" +
                    "CASE\n" +
                    "b.design_category\n" +
                    "WHEN '1' THEN\n" +
                    "'市政管道'\n" +
                    "WHEN '2' THEN\n" +
                    "'管网改造'\n" +
                    "WHEN '3' THEN\n" +
                    "'新建小区'\n" +
                    "WHEN '4' THEN\n" +
                    "'二次供水项目'\n" +
                    "WHEN '5' THEN\n" +
                    "'工商户'\n" +
                    "WHEN '6' THEN\n" +
                    "'居民装接水'\n" +
                    "WHEN '7' THEN\n" +
                    "'行政事业'\n" +
                    "END\n" +
                    ") AS designCategory,\n" +
                    "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType,\n" +
                    "b.customer_name customerName,\n" +
                    "b.water_address waterAddress,\n" +
                    "b.a_b aB,\n" +
                    "b.construction_organization constructionOrganization,\n" +
                    "(select writter from track_monthly where track_id = tai.id limit 0,1) writter,\n" +
                    "(select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) fillTime ,\n" +
                    "tai.outsource outsource,\n" +
                    "tai.audit_unit_name_id auditUnitNameId,\n" +
                    "tai.cea_total_money ceaTotalMoney\n" +
                    "from track_audit_info tai\n" +
                    "LEFT JOIN base_project b on tai.base_project_id = b.id\n" +
                    "LEFT JOIN track_application_info ta on ta.track_audit = tai.id\n" +
                    "LEFT JOIN track_monthly tm on tm.track_id = tai.id\n" +
                    "where\n" +
                    "tai.`status` = 0 \n" +
                    "and\n" +
                    "b.del_flag = 0\n" +
                    "and\n" +
                    "(tai.founder_id = #{uid} or #{uid} = '' ) \n" +
                    "and\n" +
                    "(b.district = #{district} or #{district} = '' ) \n" +
                    "and\n" +
                    "(b.project_nature = #{projectNature} or #{projectNature} = '') \n" +
                    "and\n" +
                    "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '' ) \n" +
                    "and\n" +
                    "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) > #{startTime} or #{startTime} = '') \n" +
                    "and\n" +
                    "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) < #{endTIme} or #{endTIme} = '') \n" +
                    "and\n" +
                    "(b.design_category = #{designCategory} or #{designCategory} = '') \n" +
                    "and\n" +
                    "(b.construction_organization = #{constructionOrganization} or #{constructionOrganization} = '') \n" +
                    "and\n" +
                    "(b.track_status = #{trackStatus} or #{trackStatus} = '') \n" +
                    "and\n" +
                    "(\n" +
                    "b.cea_num like concat('%',#{keyword},'%') or\n" +
                    "b.project_num like concat('%',#{keyword},'%') or\n" +
                    "b.project_name like concat ('%',#{keyword},'%') or\n" +
                    "b.construction_unit like concat ('%',#{keyword},'%') or\n" +
                    "b.customer_name like concat ('%',#{keyword},'%') or\n" +
                    "tai.audit_unit_name_id like concat  ('%',#{keyword},'%') or\n" +
                    "b.design_category like concat  ('%',#{keyword},'%') or\n" +
                    "b.construction_organization like concat  ('%',#{keyword},'%')\n" +
                    ")"
    )
    List<ReturnTrackVo> selectTrackList(PageVo pageVo);

    @Select(
            "select\n" +
                    "tai.id id,\n" +
                    "b.cea_num ceaNum,\n" +
                    "b.project_num projectNum,\n" +
                    "b.project_name projectName,\n" +
                    "( CASE b.track_status WHEN '1' THEN '待审核' WHEN '2' THEN '未提交' WHEN '3' THEN '进行中' WHEN '4' THEN '未通过' WHEN '5' THEN '已完成' END ) AS trackStatus,\n" +
                    "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
                    "b.construction_unit constructionUnit,\n" +
                    "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,\n" +
                    "(\n" +
                    "  CASE\n" +
                    "   b.design_category\n" +
                    "   WHEN '1' THEN\n" +
                    "   '市政管道'\n" +
                    "   WHEN '2' THEN\n" +
                    "   '管网改造'\n" +
                    "   WHEN '3' THEN\n" +
                    "   '新建小区'\n" +
                    "   WHEN '4' THEN\n" +
                    "   '二次供水项目'\n" +
                    "   WHEN '5' THEN\n" +
                    "   '工商户'\n" +
                    "   WHEN '6' THEN\n" +
                    "   '居民装接水'\n" +
                    "   WHEN '7' THEN\n" +
                    "   '行政事业'\n" +
                    "  END\n" +
                    "  ) AS designCategory,\n" +
                    "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType,\n" +
                    "b.customer_name customerName,\n" +
                    "b.water_address waterAddress,\n" +
                    "b.a_b aB,\n" +
                    "b.construction_organization constructionOrganization,\n" +
                    "(select writter from track_monthly where track_id = tai.id limit 0,1) writter,\n" +
                    "(select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) fillTime ,\n" +
                    "tai.outsource outsource,\n" +
                    "tai.audit_unit_name_id auditUnitNameId,\n" +
                    "tai.cea_total_money ceaTotalMoney\n" +
                    "from track_audit_info tai\n" +
                    "LEFT JOIN base_project b on tai.base_project_id = b.id\n" +
                    "LEFT JOIN track_application_info ta on ta.track_audit = tai.id\n" +
                    "LEFT JOIN track_monthly tm on tm.track_id = tai.id\n" +
                    "where\n" +
                    "tai.`status` = \"0\" \n" +
                    "and\n" +
                    "b.del_flag = \"0\" \n" +
                    "and\n" +
                    "(b.track_status = #{trackStatus} or #{trackStatus} = '') \n" +
                    "and\n" +
                    "(b.district = #{district} or #{district} = '' ) \n" +
                    "and\n" +
                    "(b.project_nature = #{projectNature} or #{projectNature} = '') \n" +
                    "and\n" +
                    "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '' ) \n" +
                    "and\n" +
                    "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) > #{startTime} or #{startTime} = '') \n" +
                    "and\n" +
                    "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) < #{endTIme} or #{endTIme} = '') \n" +
                    "and\n" +
                    "(b.design_category = #{designCategory} or #{designCategory} = '') \n" +
                    "and\n" +
                    "(b.construction_organization = #{constructionOrganization} or #{constructionOrganization} = '') \n" +
                    "and\n" +
                    "(\n" +
                    "b.cea_num like concat('%',#{keyword},'%') or\n" +
                    "b.project_num like concat('%',#{keyword},'%') or\n" +
                    "b.project_name like concat ('%',#{keyword},'%') or\n" +
                    "b.construction_unit like concat ('%',#{keyword},'%') or\n" +
                    "b.customer_name like concat ('%',#{keyword},'%') or\n" +
                    "tai.audit_unit_name_id like concat  ('%',#{keyword},'%') or\n" +
                    "b.design_category like concat  ('%',#{keyword},'%') or\n" +
                    "b.construction_organization like concat  ('%',#{keyword},'%')\n" +
                    ")"
    )
    List<ReturnTrackVo> selectTrackList1(PageVo pageVo);

    @Select(
            "SELECT  " +
                    "outsource_money " +
                    "FROM  " +
                    "track_audit_info s1, " +
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
    List<TrackAuditInfo> totalexpenditure(CostVo2 costVo2);

    @Select("SELECT " +
            "            a.audit_opinion auditOpinion, " +
            "            ( CASE a.audit_result WHEN '0' THEN '未审批' WHEN '1' THEN '通过' WHEN '2' THEN '未通过' END ) auditResult, " +
            "            m.member_name memberName, " +
            "            a.audit_time auditTime, " +
            "            a.create_time createTime  " +
            "            FROM " +
            "            audit_info a, " +
            "            member_manage m  " +
            "            WHERE " +
            "            a.auditor_id = m.id " +
            "      and a.status= '0' " +
            "            and  " +
            "             (a.base_project_id = #{id} or #{id} = '')")
    List<AuditInfoVo> findAllAuditInfosByTrackId(@Param("id") String id);
}
