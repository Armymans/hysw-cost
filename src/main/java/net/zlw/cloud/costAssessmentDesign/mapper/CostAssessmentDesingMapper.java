package net.zlw.cloud.costAssessmentDesign.mapper;

import net.zlw.cloud.costAssessmentDesign.model.CostAssessmentDesing;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by xulei on 2020/9/18.
 */
@org.apache.ibatis.annotations.Mapper
public interface CostAssessmentDesingMapper extends Mapper<CostAssessmentDesing> {

    @Select("select * from cost_assessment_design where del_flag = '0'")
    public List<CostAssessmentDesing> findAll();
}
