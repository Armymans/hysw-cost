package net.zlw.cloud.snsEmailFile.controller;

import com.sun.javafx.PlatformUtil;
import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.mapper.DepManageMapper;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.mapper.SysCompanyMapper;
import net.zlw.cloud.snsEmailFile.model.SysCompany;
import net.zlw.cloud.snsEmailFile.util.EhrJdbc;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Configuration
@Transactional
public class EHRTimer {


	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private MemberManageDao sysUserDao;
	@Autowired
	private SysCompanyMapper sysCompanyDao;
	@Autowired
	private DepManageMapper deptDao;

	/**
     * 取值范围 秒 0 - 59 分 0 - 59 时 0 - 23 日 0 - 31 按实际月份来 月 0 - 11 天 1 - 7 周几 年 1970
     * -2099 如果多个时间段用，隔开 0 0 14,16 * * ？ 代表每天下午2点和4点执行 0 0 14-16 * * ？ 代表每天下午2点到4点执行
     * 也就是 14 15 16 执行
     */
    @Scheduled(cron = "0 0 0 * * ?") // 秒 分 时 日 月 天 年
    public void ehrDataTimer() {
		EhrJdbc ehrJdbc = new EhrJdbc();
		try {
			ResultSet employeeInfoSimplify = ehrJdbc.getEmployeeInfoSimplify();
			ResultSet orgInfo = ehrJdbc.getOrgInfo();
			List<SysCompany> companyList = sysCompanyDao.selectAll();

			List<DepManage> deptList = deptDao.selectAll();

			List<MemberManage> userList = sysUserDao.selectAll();

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
						sysCompany.setStatus("1");
					}
					String orgName = orgInfo.getString("OrgName");
					String parentOrgCode = orgInfo.getString("ParentOrgCode");
					sysCompany.setId(orgCode);
					sysCompany.setCompanyCode(orgCode);
					sysCompany.setName(orgName);
					sysCompany.setParentId(parentOrgCode);
					if(!companyList.contains(sysCompany)){
                        companyList.add(sysCompany);
                    }
				} else {
					String orgCode = orgInfo.getString("OrgCode");
					
					System.out.println("orgCode==============="+orgCode);
					
					Optional<DepManage> optional = deptList.stream().filter(d -> StringUtils.equals(d.getId(), orgCode)).findFirst();
					DepManage platDept;
					if (optional.isPresent()) {
						System.out.println("1");
						platDept = optional.get();
					} else {
						System.out.println("2");
						platDept = new DepManage();
					}
					String orgId = orgInfo.getString("OrgID");
					String orgName = orgInfo.getString("OrgName");

					platDept.setId(orgCode);
					platDept.setDepCode(orgCode);
					platDept.setDepName(orgName);
					platDept.setDepPrincipal("0");
					platDept.setCompanyId(orgId);
					platDept.setStatus("0");
					platDept.setCreateTime(sdf.format(new Date()));
					deptList.add(platDept);
					if(!deptList.contains(platDept)){
                        deptDao.insertSelective(platDept);
                    }
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
					user.setMemberRoleId("zlwrole100244");
					user.setStatus("0");
					user.setCreateDate(DateUtil.getDateTime());
					user.setStatus("1");
				}

				String userName = employeeInfoSimplify.getString("EmpName");
				String userAccount = employeeInfoSimplify.getString("Account");
				String orgId = employeeInfoSimplify.getString("OrgCode");
				String email = employeeInfoSimplify.getString("Email");
				String phone = employeeInfoSimplify.getString("Mobile");


				user.setId(id);
				user.setMemberName(userName);
				user.setEmail(email);
				user.setMemberAccount(userAccount);
				
				Optional<DepManage> deptOptional = deptList.stream().filter(dept -> StringUtils.equals(dept.getId(), orgId)).findFirst();
				
				if("000".equals(orgId)){
					user.setCompanyId(orgId);
				}else{
					if(deptOptional.isPresent()) {
						DepManage platDept = deptOptional.get();
						user.setCompanyId(platDept.getCompanyId());
					}
				}
				user.setPhone(phone);
				userList.add(user);
			}

			// 保存公司
			for (SysCompany platSysCompany : companyList) {
			    if(!companyList.contains(platSysCompany)){
                    sysCompanyDao.insertSelective(platSysCompany);
                }
			}

			// 保存用户
			for (MemberManage platSysUser : userList) {
			    if(!userList.contains(platSysUser)){
                    sysUserDao.insertSelective(platSysUser);
                }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}