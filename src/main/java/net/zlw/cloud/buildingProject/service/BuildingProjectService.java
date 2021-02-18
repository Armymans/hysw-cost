package net.zlw.cloud.buildingProject.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.buildingProject.mapper.BuildingProjectMapper;
import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.buildingProject.model.vo.BaseVo;
import net.zlw.cloud.buildingProject.model.vo.PageBaseVo;
import net.zlw.cloud.buildingProject.model.vo.ProVo;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.model.AnhuiMoneyinfo;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.WujiangMoneyInfo;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/11 11:48
 **/
@Service
public class BuildingProjectService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BuildingProjectMapper buildingProjectMapper;

    @Autowired
    private BaseProjectService baseProjectService;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    private DesignInfoMapper designInfoMapper;
    @Autowired
    private VisaChangeMapper visaChangeMapper;
    @Autowired
    private ProgressPaymentInformationDao progressPaymentInformationDao;

    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;


    @Value("${audit.wujiang.sheji.designHead}")
    private String wjsjh;
    @Value("${audit.wujiang.sheji.designManager}")
    private String wjsjm;
    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh;
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm;

    @Value("${audit.wuhu.sheji.designHead}")
    private String whsjh;
    @Value("${audit.wuhu.sheji.designManager}")
    private String whsjm;
    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh;
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm;

    /**
     * @Author Armyman
     * @Description //查询可被选为合并项目的建设项目
     * @Date 11:56 2020/10/11
     **/
    public List<BuildingProject> findBuildingProject() {
        return buildingProjectMapper.findBuildingProject();
    }

    /**
     * @Author Armyman
     * @Description //建设项目合并
     * @Date 14:23 2020/10/11
     **/
    public void buildingProjectMerge(String ids, String id) {
        //建设项目id
        BuildingProject buildingProject = buildingProjectMapper.selectById(id);
        if (buildingProject != null) {
            buildingProject.setMergeFlag("1");
            buildingProjectMapper.updateByPrimaryKeySelective(buildingProject);
        }
        //工程项目ids
        String[] split = ids.split(",");
        for (String thisId : split) {
            BaseProject baseProject = baseProjectDao.findById(thisId);
            BaseProject baseProject1 = baseProjectDao.findBaseProject(thisId);
            if (baseProject1 != null){
                 throw new RuntimeException("合并项目已存在,请重新选择");
            }else if (baseProject != null ) {
                baseProject.setBuildingProjectId(id);
                baseProject.setMergeFlag("0");
                baseProject.setUpdateTime(sdf.format(new Date()));
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }
//            else{
//                new RuntimeException("项目信息错误，请确认后合并");
//            }
        }
    }

    /**
     * @Author Armyman
     * @Description // 建设项目还原
     * @Date 11:56 2020/10/11
     **/
    public void buildingProjectReduction(String id) {
        BuildingProject buildingProject = buildingProjectMapper.selectById(id);
        if (buildingProject != null) {
            buildingProject.setMergeFlag("2");
            buildingProjectMapper.updateByPrimaryKeySelective(buildingProject);
        }
        //工程项目ids
        List<BaseProject> baseProject = baseProjectService.findByBuildingProject(id);
        if (baseProject.size() > 0) {
            for (BaseProject project : baseProject) {
//                project.setId(UUID.randomUUID().toString().replace("-",""));
                project.setBuildingProjectId(null);
                project.setMergeFlag("1");
                project.setUpdateTime(sdf.format(new Date()));
//                baseProjectService.updateProject(project);
//                baseProjectDao.updateByPrimaryKey(project);
                baseProjectDao.updateByPrimaryKeySelective(project);
            }
        }
    }

    // 删除
    public void deleteBuilding(String id, UserInfo userInfo, HttpServletRequest request) {
        BuildingProject buildingProject = buildingProjectMapper.selectOneBuilding(id);
        //如果删除的项目存在已合并，提示 ‘已有关联项目，无法删除’
            if ("1".equals(buildingProject.getMergeFlag())){
                throw new RuntimeException("已有关联项目，无法删除");
            }else {
                buildingProject.setDelFlag("1");
                buildingProjectMapper.deleteBuilding(id);
                // 操作日志
                String userId = userInfo.getId();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                operationLog.setName(userId);
                operationLog.setType("1"); //建设项目
                operationLog.setContent(memberManage.getMemberName()+"删除"+buildingProject.getBuildingProjectName()+"项目【"+buildingProject.getId()+"】");
                operationLog.setDoObject(buildingProject.getId()); // 项目标识
                operationLog.setStatus("0");
                operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);
            }
    }

    public List<BaseVo> selectBaseProjectList(String id) {
        List<BaseVo> BaseProjectVo = buildingProjectMapper.selectBaseProjectList(id);
        if (BaseProjectVo.size() > 0){
            for (BaseVo thisVo : BaseProjectVo) {
                if (StringUtils.isEmpty(thisVo.getId())){
                    return null;
                }
                //签证变更累计金额
                Example example = new Example(VisaChange.class);
                example.createCriteria().andEqualTo("baseProjectId",thisVo.getId()) //基本信息表id
                                        .andEqualTo("state","0");
                List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
                if (visaChanges.size() >0){
                    BigDecimal visaNum = new BigDecimal(0);
                    for (VisaChange thisVisa : visaChanges) {
                        visaNum = visaNum.add(thisVisa.getAmountVisaChange());
                    }
                    thisVo.setCumulativeChangeAmount(visaNum+"");
                }
                // 进度款
                Example example1 = new Example(ProgressPaymentInformation.class);
                example1.createCriteria().andEqualTo("baseProjectId",thisVo.getId()) //基本信息表id
                        .andEqualTo("delFlag","0");
                List<ProgressPaymentInformation> informations = progressPaymentInformationDao.selectByExample(example1);
                if (informations.size() > 0){
                    BigDecimal proNum = new BigDecimal(0); //进度款次数
                    BigDecimal proAmount = new BigDecimal(0); // 合同金额
                    for (ProgressPaymentInformation thisInfo : informations) {
                        if (thisInfo.getChangeNum() != null){
                            proNum = proNum.add(new BigDecimal(thisInfo.getChangeNum()));
                        }
                        if (thisInfo.getContractAmount() != null){
                            proAmount = proAmount.add(thisInfo.getContractAmount());
                        }
                    }
                    thisVo.setContractAmount(proAmount+"");
                    thisVo.setCumulativePaymentTimes(proNum+"");
                }
                // 造价金额
                if (thisVo.getAmountCost() != null && !"".equals(thisVo.getAmountCost())){
                    thisVo.setAmountCost(thisVo.getActualAmount());
                }else {
                    thisVo.setAmountCost("/");
                }
                // 合同金额
                if (thisVo.getContractAmount() != null && !"".equals(thisVo.getContractAmount())){
                    thisVo.setContractAmount(thisVo.getContractAmount());
                }else {
                    thisVo.setContractAmount("/");
                }
                // 进度款支付次数累计
                if (thisVo.getCumulativePaymentTimes() != null && !"".equals(thisVo.getCumulativePaymentTimes())){
                    thisVo.setCumulativePaymentTimes(thisVo.getCumulativePaymentTimes());
                }else {
                    thisVo.setCumulativePaymentTimes("/");
                }
                // 跟踪审计CEA金额
                if (thisVo.getCeaTotalMoney() != null && !"".equals(thisVo.getCeaTotalMoney())){
                    thisVo.setCeaTotalMoney(thisVo.getCeaTotalMoney());
                }else {
                    thisVo.setCeaTotalMoney("/");
                }
                //  签证/变更累计金额（元）
                if (thisVo.getCumulativeChangeAmount() != null && !"".equals(thisVo.getCumulativeChangeAmount())){
                    thisVo.setCumulativeChangeAmount(thisVo.getCumulativeChangeAmount());
                }else {
                    thisVo.setCumulativeChangeAmount("/");
                }
                // 结算审定金额（元）
                if (thisVo.getAuthorizedNumber() != null && !"".equals(thisVo.getAuthorizedNumber())){
                    thisVo.setAuthorizedNumber(thisVo.getAuthorizedNumber());
                }else {
                    thisVo.setAuthorizedNumber("/");
                }
                // 设计费
                if (thisVo.getActualAmount() != null && !"".equals(thisVo.getActualAmount())){
                    thisVo.setActualAmount(thisVo.getActualAmount());
                }else {
                    thisVo.setActualAmount  ("/");
                }
            }
        }
        return BaseProjectVo;
    }

    public List<ProVo> selectBaseProjectFindAll(PageBaseVo pageVo, String id) {
        List<ProVo> baseList = baseProjectDao.selectBaseProjectFindAll(pageVo);
            for (ProVo thisVo : baseList) {
                Example example = new Example(DesignInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",thisVo.getId());
//                                        .andEqualTo("status","0");
                DesignInfo designInfo = designInfoMapper.selectOneByExample(example);
                if (designInfo!=null){
                    // 如果是安徽
                    if (!"4".equals(thisVo.getDistrict())) {
                        AnhuiMoneyinfo anhuiMoneyinfo = baseProjectDao.selectByAnHuiOfficialReceipts(designInfo.getId());
                        if (anhuiMoneyinfo != null) {
                            thisVo.setOfficialReceipts(anhuiMoneyinfo.getRevenue());
                        }
                    } else {
                        WujiangMoneyInfo wujiangMoneyInfo = baseProjectDao.selectByWuJiangOfficialReceipts(designInfo.getId());
                        if (wujiangMoneyInfo != null) {
                            thisVo.setOfficialReceipts(wujiangMoneyInfo.getRevenue());
                        }
                    }
                }

            }


            ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("1");
            String personnel = thoseResponsible.getPersonnel();
            boolean f = false;
            if (personnel!=null){
                String[] split = personnel.split(",");
                for (String s : split) {
                    if (s.equals(id) || id.equals(wjsjh) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(whsjh) || id.equals(whzjh)){
                        f = true;
                    }
                }
            }
            if (f){
                for (ProVo proVo : baseList) {
                    proVo.setFShow("1");
                }
            }

        return baseList;
    }
}
