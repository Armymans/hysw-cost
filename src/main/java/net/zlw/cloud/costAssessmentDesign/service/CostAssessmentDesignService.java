package net.zlw.cloud.costAssessmentDesign.service;

import net.zlw.cloud.costAssessmentDesign.mapper.CostAssessmentDesingMapper;
import net.zlw.cloud.costAssessmentDesign.model.CostAssessmentDesing;
import net.zlw.cloud.designAssessSettings.model.AssessmentDesign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xulei on 2020/9/18.
 */
@Service
@Transactional
public class CostAssessmentDesignService {
    @Resource
    private CostAssessmentDesingMapper costAssessmentDesingMapper;


    /**
     * @Author Armyman
     * @Description // 根据id查找
     * @Date 21:10 2020/9/20
     **/
    public CostAssessmentDesing findById(String id) {
        CostAssessmentDesing one = costAssessmentDesingMapper.selectByPrimaryKey(id);
        String calculationFormula = one.getCalculationFormula();
        if (calculationFormula != null) {
            String[] split = calculationFormula.split(",");
            if (split.length > 0) {
                one.setParame1(split[0]);
                if (split.length > 1) {
                    one.setParame2(split[1]);
                }
            }
        }
        return one;

    }

    /**
     * @Author Armyman
     * @Description // 保存
     * @Date 21:10 2020/9/18
     **/
    public void update(AssessmentDesign assessmentDesign) {
        CostAssessmentDesing one = costAssessmentDesingMapper.selectByPrimaryKey(assessmentDesign.getId());
        String parame1 = assessmentDesign.getParame1();
        String parame2 = assessmentDesign.getParame2();
        String parame3 = assessmentDesign.getParame3();
        if (parame1 != null && !"".equals(parame1) && parame2 != null && !"".equals(parame2) && parame3 != null && !"".equals(parame3)) {
            one.setCalculationFormula(parame1 + "," + parame2 + "," + parame3);
        }
        one.setRemarkes(assessmentDesign.getRemarkes());
        costAssessmentDesingMapper.updateByExample(one,AssessmentDesign.class);
    }



    public List<CostAssessmentDesing> findAll(){
        List<CostAssessmentDesing> all = costAssessmentDesingMapper.findAll();
        return all;
    }

}


