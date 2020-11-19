package net.zlw.cloud.snsEmailFile.controller;

import com.sun.javafx.PlatformUtil;
import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.clearProject.mapper.CallForBidsMapper;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.mapper.DepManageMapper;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.mapper.EmOrgMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.mapper.SysCompanyMapper;
import net.zlw.cloud.snsEmailFile.model.EmOrg;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.SysCompany;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.snsEmailFile.util.CaiGouJdbc;
import net.zlw.cloud.snsEmailFile.util.EhrJdbc;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@Transactional
//public class EHRTimer {
public class EHRTimer implements InitializingBean {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MemberManageDao sysUserDao;
    @Autowired
    private SysCompanyMapper sysCompanyDao;
    @Autowired
    private DepManageMapper deptDao;
    @Autowired
    private EmOrgMapper emOrgMapper;
    @Autowired
    private MkyUserMapper mkyUserMapper;
    @Autowired
    private CallForBidsMapper callForBidsMapper;
    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 取值范围 秒 0 - 59 分 0 - 59 时 0 - 23 日 0 - 31 按实际月份来 月 0 - 11 天 1 - 7 周几 年 1970
     * -2099 如果多个时间段用，隔开 0 0 14,16 * * ？ 代表每天下午2点和4点执行 0 0 14-16 * * ？ 代表每天下午2点到4点执行
     * 也就是 14 15 16 执行
     */
//    @Scheduled(cron = "0 0 0 * * ?") // 秒 分 时 日 月 天 年 每天十二点执行
    public void ehrDataTimer() {
        EhrJdbc ehrJdbc = new EhrJdbc();
        try {
            ResultSet employeeInfoSimplify = ehrJdbc.getEmployeeInfoSimplify();
            ResultSet orgInfo = ehrJdbc.getOrgInfo();
            //本地数据库
            List<SysCompany> companyList = sysCompanyDao.selectAll();

            List<DepManage> deptList = deptDao.selectAll();

            List<MemberManage> userList = sysUserDao.selectAll();

            //要保存的
            List<SysCompany> companyList2 = new ArrayList<>();

            List<DepManage> deptList2 = new ArrayList<>();

            List<MemberManage> userList2 = new ArrayList<>();

            while (orgInfo.next()) {
                String orgLevel = orgInfo.getString("OrgLevel");
                if (StringUtils.equals(orgLevel, "一级") || StringUtils.equals(orgLevel, "二级")) {
                    String orgCode = orgInfo.getString("OrgCode");
                    Optional<SysCompany> companyOptional = companyList.stream().filter(d -> StringUtils.equals(d.getId(), orgCode)).findFirst();
                    SysCompany sysCompany;
                    if (companyOptional.isPresent()) {
                        sysCompany = companyOptional.get();
                    } else {
                        sysCompany = new SysCompany();
                        sysCompany.setIdentifyFlag("1");
                        sysCompany.setType("2");
                        sysCompany.setBusinessType("1");
                        sysCompany.setCreateDate(DateUtil.getDateTime());
                        sysCompany.setStatus("0");
                    }
                    String orgName = orgInfo.getString("OrgName");
                    String id = orgInfo.getString("OrgCode");
                    String parentOrgCode = orgInfo.getString("ParentOrgCode");
                    if (id.equals("hywater")) {
                        sysCompany.setId("org8047");
                        sysCompany.setCompanyCode("org8047");
                    } else {
                        sysCompany.setId(id);
                        sysCompany.setCompanyCode(orgCode);
                    }
                    sysCompany.setName(orgName);
                    if (parentOrgCode != null && parentOrgCode.equals("hywater")) {
                        sysCompany.setParentId("org8047");
                    }
                    companyList2.add(sysCompany);
                } else {
                    String orgCode = orgInfo.getString("OrgCode");
                    Optional<DepManage> optional = deptList.stream().filter(d -> StringUtils.equals(d.getId(), orgCode)).findFirst();
                    DepManage platDept;
                    if (optional.isPresent()) {
                        platDept = optional.get();
                    } else {
                        platDept = new DepManage();
                    }
                    String parentOrgCode = orgInfo.getString("ParentOrgCode");
                    String orgName = orgInfo.getString("OrgName");

                    platDept.setId(orgCode);
                    platDept.setDepCode(orgCode);
                    platDept.setDepName(orgName);
                    platDept.setDepPrincipal("0");
                    platDept.setCompanyId(parentOrgCode);
                    platDept.setStatus("0");
                    platDept.setCreateTime(sdf.format(new Date()));
                    deptList2.add(platDept);
                }
            }
            while (employeeInfoSimplify.next()) {
                String id = employeeInfoSimplify.getString("EmpNo");
                Optional<MemberManage> userOptional = userList.stream().filter(u -> StringUtils.equals(u.getId(), id)).findFirst();
                MemberManage user;
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                } else {
                    user = new MemberManage();
//                    user.setMemberRoleId("zlwrole100244");
                    user.setStatus("0");
                    user.setCreateDate(DateUtil.getDateTime());
                }

                String userName = employeeInfoSimplify.getString("EmpName");
                String workType = employeeInfoSimplify.getString("WorkType");
                String userAccount = employeeInfoSimplify.getString("Account");
                String orgId = employeeInfoSimplify.getString("OrgCode");
                String email = employeeInfoSimplify.getString("Email");
                String gender = employeeInfoSimplify.getString("Gender");
                String phone = employeeInfoSimplify.getString("Mobile");


                user.setId(id);
                user.setMemberName(userName);
                user.setEmail(email);
                if (userAccount != null && userAccount.contains("\\")) {
                    String[] split = userAccount.split("\\\\");
                    userAccount = split[1];
                }
                user.setMemberAccount(userAccount);
                if ("男".equals(gender)) {
                    user.setMemberSex("0");
                } else if ("女".equals(gender)) {
                    user.setMemberSex("1");
                }

                Optional<DepManage> deptOptional = deptList.stream().filter(dept -> StringUtils.equals(dept.getId(), orgId)).findFirst();

                user.setCompanyId(orgId);
                //判断是吴江还是芜湖
                if (orgId != null && !"".equals(orgId)) {
                    String s = orgId.substring(0, 3);
                    //吴江
                    if ("001".equals(s)) {
                        user.setWorkType("2");
                    } else {
                        //芜湖
                        user.setWorkType("1");
                    }
                }
//                if ("000".equals(orgId)) {
//                    user.setCompanyId(orgId);
//                    user.setDepAdmin("1");
//                } else {
//                    if (deptOptional.isPresent()) {
//                        DepManage platDept = deptOptional.get();
//                        user.setCompanyId(orgId);
//                    }
//                }
                user.setPhone(phone);
                userList2.add(user);
            }

            // 保存公司
            for (SysCompany platSysCompany : companyList2) {
                SysCompany sysCompany = sysCompanyDao.selectByPrimaryKey(platSysCompany.getId());
                if (sysCompany != null) {
                    sysCompanyDao.updateByPrimaryKeySelective(platSysCompany);
                } else {
                    sysCompanyDao.insertSelective(platSysCompany);
                }
            }

            // 保存用户
            for (MemberManage platSysUser : userList2) {
                MemberManage memberManage = sysUserDao.selectByPrimaryKey(platSysUser.getId());
                if (memberManage != null) {
                    sysUserDao.updateByPrimaryKeySelective(platSysUser);
                } else {
                    sysUserDao.insertSelective(platSysUser);
                }
            }

            //保存部门
            for (DepManage depManage : deptList2) {
                DepManage depManage1 = deptDao.selectByPrimaryKey(depManage.getId());
                if (depManage1 != null) {
                    deptDao.updateByPrimaryKeySelective(depManage);
                } else {
                    deptDao.insertSelective(depManage);
                }
            }


            //将造价数据库同步到低代码数据库
            List<SysCompany> company = sysCompanyDao.selectAll();
            List<DepManage> dept = deptDao.selectAll();
            List<MemberManage> user = sysUserDao.selectAll();

            for (SysCompany sysCompany : company) {
                EmOrg emOrg = new EmOrg();
                emOrg.setId(sysCompany.getId());
                emOrg.setName(sysCompany.getName());
                emOrg.setPid(sysCompany.getParentId());
                emOrg.setCompanyId("com21");
                emOrg.setCreateDate(sdf.format(new Date()));
                emOrg.setDelFlag("0");
                emOrg.setOrgType("1");
                EmOrg emOrg1 = emOrgMapper.selectByPrimaryKey(sysCompany.getId());
                if (emOrg1 != null) {
                    emOrgMapper.updateByPrimaryKeySelective(emOrg);
                } else {
                    emOrgMapper.insertSelective(emOrg);
                }
            }

            for (DepManage depManage : dept) {
                EmOrg emOrg = new EmOrg();
                emOrg.setId(depManage.getId());
                emOrg.setName(depManage.getDepName());
                if (depManage.getCompanyId().equals("hywater")) {
                    emOrg.setPid("org8047");
                } else {
                    emOrg.setPid(depManage.getCompanyId());
                }
                emOrg.setCompanyId("com21");
                emOrg.setCreateDate(sdf.format(new Date()));
                emOrg.setDelFlag("0");
                emOrg.setOrgType("2");
                EmOrg emOrg1 = emOrgMapper.selectByPrimaryKey(depManage.getId());
                if (emOrg1 != null) {
                    emOrgMapper.updateByPrimaryKeySelective(emOrg);
                } else {
                    emOrgMapper.insertSelective(emOrg);
                }
            }

            for (MemberManage memberManage : user) {
                if (memberManage.getMemberAccount() != null && !"".equals(memberManage.getMemberAccount()) && !memberManage.getMemberAccount().contains("hysw")) {
                    MkyUser mkyUser = new MkyUser();
                    mkyUser.setId(memberManage.getId());
                    mkyUser.setUserCode(memberManage.getMemberAccount());
                    mkyUser.setUserName(memberManage.getMemberName());
                    mkyUser.setPasswd("123456");
                    mkyUser.setSalt("123456");
                    mkyUser.setEmail(memberManage.getEmail());
                    mkyUser.setUserType("2");
                    mkyUser.setComId("com21");
                    mkyUser.setOrgId(memberManage.getCompanyId());
                    mkyUser.setPhone(memberManage.getPhone());
                    mkyUser.setCreateDatetime(sdf.format(new Date()));
                    mkyUser.setUpdateDatetime(sdf.format(new Date()));
                    mkyUser.setPhone(memberManage.getPhone());
                    mkyUser.setStatus("1");
                    mkyUser.setDelFlag("0");
                    MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(mkyUser.getId());
                    if (mkyUser1 != null) {
                        mkyUserMapper.updateByPrimaryKeySelective(mkyUser);
                    } else {
                        mkyUserMapper.insertSelective(mkyUser);
                    }
                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @Author Armyman
     * @Description //获取芜湖招标采购数据
     * @Date 16:58 2020/11/16
     **/
    //@Scheduled(cron = "0 30 0 * * ?") // 秒 分 时 日 月 天 年  每天十二点半执行
    public void CaiGouDataTimer() {
        CaiGouJdbc caiGouJdbc = new CaiGouJdbc();
        try {
            //芜湖招标信息
            ResultSet bidInfoSimplify = caiGouJdbc.getBidInfoSimplify();
            while (bidInfoSimplify.next()) {
                CallForBids callForBids = new CallForBids();
                String projectCode = bidInfoSimplify.getString("project_code");
                String companyId = bidInfoSimplify.getString("company_id");
                String userId = bidInfoSimplify.getString("user_id");
                String archiveProjectName = bidInfoSimplify.getString("archive_project_name");
                callForBids.setBidProjectNum(projectCode);
                callForBids.setBidProjectName(archiveProjectName);
                callForBids.setStatus("0");
                //获取中标的数据
                ResultSet winInfoSimplify = caiGouJdbc.getWinInfoSimplify(projectCode);
                while (winInfoSimplify.next()) {
                    String bidderName = winInfoSimplify.getString("bidder_name");
                    String winBidPrice = winInfoSimplify.getString("win_bid_price");
                    String applyCompanyId = winInfoSimplify.getString("apply_company_id");
                    callForBids.setBidWinner(bidderName);
                    callForBids.setBidMoney(winBidPrice);
                    //投标人
                    ResultSet companyInfoSimplify = caiGouJdbc.getCompanySimplify(companyId);
                    while (companyInfoSimplify.next()) {
                        String companyName = companyInfoSimplify.getString("name");
                        callForBids.setTenderer(companyName);
                        callForBids.setConstructionUnit(companyName);
                        //代理机构
                        String replace = UUID.randomUUID().toString().replace("-", "");
                        ResultSet userInfoSimplify = caiGouJdbc.getUserInfoSimplify(userId);
                        if (userInfoSimplify.next()) {
                            while (userInfoSimplify.next()) {
                                ResultSet companyInfoSimplify2 = caiGouJdbc.getCompanySimplify(companyId);
                                while (companyInfoSimplify2.next()) {
                                    String name = companyInfoSimplify2.getString("name");
                                    callForBids.setProcuratorialAgency(name);
                                }
                            }
                        } else {
                            callForBids.setProcuratorialAgency("无");
                        }
                        callForBids.setId(replace);
                        CallForBids callForBid = callForBidsMapper.selectByProjectNum(projectCode);
                        if(callForBid != null){
                            callForBids.setId(callForBid.getId());
                            callForBidsMapper.updateByPrimaryKeySelective(callForBids);
                        }else{
                            callForBidsMapper.insert(callForBids);
                            //招标文件数据
                            ResultSet bidFileSimplify = caiGouJdbc.getBidFileSimplify(projectCode);
                            while (bidFileSimplify.next()){
                                String fileName = bidFileSimplify.getString("file_name");
                                String filePath = bidFileSimplify.getString("file_path");
                                String id = bidFileSimplify.getString("id");
                                String fileType = bidFileSimplify.getString("file_type");
                                String fileSource = bidFileSimplify.getString("file_source");
                                String[] split = fileName.split("\\.");
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setId(id);
                                fileInfo.setPlatCode(replace);
                                fileInfo.setName(split[0]);
                                fileInfo.setFileName(split[0]);
                                fileInfo.setFilePath(filePath);
                                fileInfo.setType(fileType);
                                fileInfo.setFileType(split[1]);
                                fileInfo.setStatus("0");
//                                if("wh".equals(fileSource)){
                                    fileInfo.setFileSource("3");
//                                }
                                fileInfoService.insert(fileInfo);
                            }
                            //中标文件数据
                            ResultSet bidProSimplify = caiGouJdbc.getBidProSimplify(projectCode,applyCompanyId);
                            while (bidProSimplify.next()){
                                String bidder = bidProSimplify.getString("bidder");
                                ResultSet bidWinFileSimplify = caiGouJdbc.getBidWinFileSimplify(bidder);
                                while (bidWinFileSimplify.next()){
                                    String fileName = bidWinFileSimplify.getString("file_name");
                                    String id = bidWinFileSimplify.getString("id");
                                    String filePath = bidWinFileSimplify.getString("file_path");
                                    String fileType = bidWinFileSimplify.getString("file_type");
                                    String fileSource = bidWinFileSimplify.getString("file_source");
                                    String[] split = fileName.split("\\.");
                                    FileInfo fileInfo = new FileInfo();
                                    fileInfo.setId(id);
                                    fileInfo.setPlatCode(replace);
                                    fileInfo.setName(split[0]);
                                    fileInfo.setFileName(split[0]);
                                    fileInfo.setFilePath(filePath);
                                    fileInfo.setType(fileType);
                                    fileInfo.setFileType(split[1]);
                                    fileInfo.setStatus("0");
//                                    if("wh".equals(fileSource)){
                                        fileInfo.setFileSource("3");
//                                    }
                                    fileInfoService.insert(fileInfo);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        ehrDataTimer();
//        CaiGouDataTimer();
    }
}