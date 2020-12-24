package net.zlw.cloud.designAssessSettings.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.designAssessSettings.mapper.DesignAssessSettingsMapper;
import net.zlw.cloud.designAssessSettings.model.AssessmentDesign;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description 设计-考核设计 事务层
 * @Date 2020/9/18 21:04
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class DesignAssessSettingsService {

    @Autowired
    private DesignAssessSettingsMapper designAssessSettingsMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    private MemberManageDao memberManageDao;


    /**
     * @Author Armyman
     * @Description // 根据id查找
     * @Date 21:10 2020/9/18
     **/
    public AssessmentDesign findById(String id) {
        AssessmentDesign one = designAssessSettingsMapper.selectByPrimaryKey(id);
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
    public void update(AssessmentDesign assessmentDesign, UserInfo userInfo, HttpServletRequest request) {
        AssessmentDesign one = designAssessSettingsMapper.selectByPrimaryKey(assessmentDesign.getId());
        String parame1 = assessmentDesign.getParame1();
        String parame2 = assessmentDesign.getParame2();
        String parame3 = assessmentDesign.getParame3();
        if (parame1 != null && !"".equals(parame1) && parame2 != null && !"".equals(parame2) && parame3 != null && !"".equals(parame3)) {
            one.setCalculationFormula(parame1 + "," + parame2 + "," + parame3);
        }
        one.setRemarkes(assessmentDesign.getRemarkes());
        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("9"); //考核设置-设计
        operationLog.setContent(memberManage.getMemberName()+"修改了"+assessmentDesign.getAssessmentName()+"【"+assessmentDesign.getId()+"】");
        operationLog.setDoObject(assessmentDesign.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
        designAssessSettingsMapper.updateByPrimaryKeySelective(one);
    }
}
