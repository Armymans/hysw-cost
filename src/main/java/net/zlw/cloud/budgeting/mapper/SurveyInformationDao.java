package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.SurveyInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface SurveyInformationDao extends Mapper<SurveyInformation> {
    
    
    @Select("SELECT " +
            " id, " +
            " survey_date surveyDate, " +
            " investigation_personnel investigationPersonnel, " +
            " price_information_name priceInformationName, " +
            " survey_briefly surveyBriefly, " +
            " ( CASE price_information_nper  " +
            " WHEN 1 THEN '第一期'  " +
            " WHEN 2 THEN '第二期'  " +
            " WHEN 3 THEN '第三期'  " +
            " WHEN 4 THEN '第四期'  " +
            " WHEN 5 THEN '第五期'  " +
            " WHEN 6 THEN '第六期'  " +
            " WHEN 7 THEN '第七期'  " +
            " WHEN 8 THEN '第八期'  " +
            " WHEN 9 THEN '第九期'  " +
            " WHEN 10 THEN '第十期'  " +
            " WHEN 11 THEN '第十一期'  " +
            " WHEN 12 THEN '第十二期'  " +
            " END ) priceInformationNper " +
            "FROM " +
            " survey_information  " +
            "WHERE " +
            " budgeting_id = #{id}")
    SurveyInformation selectByOne(@Param("id") String id);
}
