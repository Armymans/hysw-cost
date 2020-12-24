package net.zlw.cloud.costAssessmentDesign.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.costAssessmentDesign.mapper.CostAssessmentDesingMapper;
import net.zlw.cloud.costAssessmentDesign.model.CostAssessmentDesing;
import net.zlw.cloud.designAssessSettings.model.AssessmentDesign;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xulei on 2020/9/18.
 */
@Service
@Transactional
public class CostAssessmentDesignService {
    @Resource
    private CostAssessmentDesingMapper costAssessmentDesingMapper;
    @Resource
    private MemberService memberService;
    @Resource
    private OperationLogDao operationLogDao;
    @Resource
    private MemberManageDao memberManageDao;


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
                    if (split.length > 2) {
                        one.setParame3(split[2]);
                        if (split.length > 3) {
                            one.setParame4(split[3]);
                            if (split.length > 4) {
                                one.setParame5(split[4]);
                            }
                        }
                    }
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
    public void update(AssessmentDesign assessmentDesign, UserInfo userInfo , HttpServletRequest request) {
        CostAssessmentDesing one = costAssessmentDesingMapper.selectByPrimaryKey(assessmentDesign.getId());
        String parame1 = assessmentDesign.getParame1();
        String parame2 = assessmentDesign.getParame2();
        String parame3 = assessmentDesign.getParame3();
        String parame4 = assessmentDesign.getParame4();
        String parame5 = assessmentDesign.getParame5();
        if (parame1 != null && !"".equals(parame1) && parame2 != null && !"".equals(parame2)) {
            one.setCalculationFormula(parame1 + "," + parame2);
        }
        if (parame1 != null && !"".equals(parame1) && parame2 != null && !"".equals(parame2) && parame3 != null && !"".equals(parame3) && parame4 != null && !"".equals(parame4) && parame5 != null && !"".equals(parame5)) {
            one.setCalculationFormula(parame1 + "," + parame2 + "," + parame3 + "," + parame4 + "," + parame5);
        }
        one.setRemarkes(assessmentDesign.getRemarkes());

        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("10"); //考核设置-造价
        operationLog.setContent(memberManage.getMemberName()+"修改了"+one.getAssessmentName()+"【"+one.getId()+"】");
        operationLog.setDoObject(one.getId()+""); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
        costAssessmentDesingMapper.updateByPrimaryKeySelective(one);
    }



    public List<CostAssessmentDesing> findAll(){
        List<CostAssessmentDesing> all = costAssessmentDesingMapper.findAll();
        return all;
    }

}


