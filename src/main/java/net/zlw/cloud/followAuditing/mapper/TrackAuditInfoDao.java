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
    @Select("select  " +
            "tai.id id, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( CASE b.track_status WHEN '1' THEN '待审核' WHEN '2' THEN '未提交' WHEN '3' THEN '进行中' WHEN '4' THEN '未通过' WHEN '5' THEN '已完成' END ) AS trackStatus, " +
            "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
            "b.construction_unit constructionUnit, " +
            "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
            "( " +
            "  CASE " +
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
            "  END  " +
            "  ) AS designCategory, " +
            "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
            "b.customer_name customerName, " +
            "b.water_address waterAddress, " +
            "b.a_b aB, " +
            "b.construction_organization constructionOrganization, " +
            "(select writter from track_monthly where track_id = tai.id limit 0,1) writter, " +
            "(select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) fillTime , " +
            "tai.outsource outsource,  " +
            "tai.audit_unit_name_id auditUnitNameId, " +
            "tai.cea_total_money ceaTotalMoney " +
            "from track_audit_info tai  " +
            "LEFT JOIN base_project b on tai.base_project_id = b.id " +
            "LEFT JOIN track_application_info ta on ta.track_audit = tai.id " +
            "LEFT JOIN track_monthly tm on tm.track_id = tai.id " +
            "LEFT JOIN audit_info ai on ai.base_project_id = tai.id " +
            "where " +
            "(ai.auditor_id = #{uid} or #{uid} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '' ) and  " +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) > #{startTime} or #{startTime} = '') and  " +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) < #{endTIme} or #{endTIme} = '') and  " +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and  " +
            "(b.construction_organization = #{constructionOrganization} or #{constructionOrganization} = '') and  " +
            "(b.track_status = #{trackStatus} or #{trackStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or  " +
            "b.project_num like concat('%',#{keyword},'%') or  " +
            "b.project_name like concat ('%',#{keyword},'%') or  " +
            "b.construction_unit like concat ('%',#{keyword},'%') or  " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "tai.audit_unit_name_id like concat  ('%',#{keyword},'%') or  " +
            "b.design_category like concat  ('%',#{keyword},'%') or  " +
            "b.construction_organization like concat  ('%',#{keyword},'%')  " +
            ") and  " +
            "tai.`status` = \"0\" and  " +
            "b.del_flag = \"0\"  ")
    List<ReturnTrackVo> selectTrackList(PageVo pageVo);

    @Select("select  " +
            "tai.id id, " +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "b.project_name projectName, " +
            "( CASE b.track_status WHEN '1' THEN '待审核' WHEN '2' THEN '未提交' WHEN '3' THEN '进行中' WHEN '4' THEN '未通过' WHEN '5' THEN '已完成' END ) AS trackStatus, " +
            "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, " +
            "b.construction_unit constructionUnit, " +
            "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature, " +
            "( " +
            "  CASE " +
            "   b.design_category  " +
            "   WHEN '1' THEN " +
            "   '市政管道'  " +
            "   WHEN '2' THEN " +
            "   '管网改造'  " +
            "   WHEN '3' THEN " +
            "   '新建小区'  " +
            "   WHEN '4' THEN " +
            "   '二次供水项目'  " +
            "   WHEN '5' THEN " +
            "   '工商户'  " +
            "   WHEN '6' THEN " +
            "   '居民装接水'  " +
            "   WHEN '7' THEN " +
            "   '行政事业'  " +
            "  END  " +
            "  ) AS designCategory, " +
            "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType, " +
            "b.customer_name customerName, " +
            "b.water_address waterAddress, " +
            "b.a_b aB, " +
            "b.construction_organization constructionOrganization, " +
            "(select writter from track_monthly where track_id = tai.id limit 0,1) writter, " +
            "(select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) fillTime , " +
            "tai.outsource outsource,  " +
            "tai.audit_unit_name_id auditUnitNameId, " +
            "tai.cea_total_money ceaTotalMoney " +
            "from track_audit_info tai  " +
            "LEFT JOIN base_project b on tai.base_project_id = b.id " +
            "LEFT JOIN track_application_info ta on ta.track_audit = tai.id " +
            "LEFT JOIN track_monthly tm on tm.track_id = tai.id " +
            "where " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '' ) and  " +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) > #{startTime} or #{startTime} = '') and  " +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) < #{endTIme} or #{endTIme} = '') and  " +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and  " +
            "(b.construction_organization = #{constructionOrganization} or #{constructionOrganization} = '') and  " +
            "(b.track_status = #{trackStatus} or #{trackStatus} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or  " +
            "b.project_num like concat('%',#{keyword},'%') or  " +
            "b.project_name like concat ('%',#{keyword},'%') or  " +
            "b.construction_unit like concat ('%',#{keyword},'%') or  " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "tai.audit_unit_name_id like concat  ('%',#{keyword},'%') or  " +
            "b.design_category like concat  ('%',#{keyword},'%') or  " +
            "b.construction_organization like concat  ('%',#{keyword},'%')  " +
            ") and  " +
            "tai.`status` = \"0\" and  " +
            "b.del_flag = \"0\"  ")
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
